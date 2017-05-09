package za.co.absa.pangea.ops.workflow.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProcessServices {

	private static final Logger LOG = Logger.getLogger(ProcessServices.class);
	
	private static final String statusVarName = "status";

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ManagementService managementService;

	@Autowired
	private HistoryService historyService;

	public List<ProcessAttachment> attachments(String processId) {

		List<Attachment> attachments = taskService.getProcessInstanceAttachments(processId);

		List<ProcessAttachment> results = new ArrayList<>(attachments.size());

		for (Attachment attachment : attachments) {
			ProcessAttachment processAttachment = new ProcessAttachment();
			processAttachment.setId(attachment.getId());
			processAttachment.setContentUrl(null);
			processAttachment.setDescription(attachment.getDescription());
			processAttachment.setName(attachment.getName());
			processAttachment.setMediaType(attachment.getType());

			String varId = getAttachmentVariableName(attachment.getId());
			Long typeId = (Long) runtimeService.getVariable(processId, varId);
			if (typeId != null) {
				processAttachment.setTypeId(typeId);
			}

			results.add(processAttachment);
		}

		return results;
	}

	public Attachment getAttachment(String id) {
		return taskService.getAttachment(id);
	}

	public InputStream readAttachment(Attachment attachment) {
		return taskService.getAttachmentContent(attachment.getId());
	}

	public void update(ProcessAttachment processAttachment, String id) {
		Attachment attachment = taskService.getAttachment(id);
		attachment.setDescription(processAttachment.getDescription());

		taskService.saveAttachment(attachment);

		String varId = getAttachmentVariableName(id);

		runtimeService.setVariable(attachment.getProcessInstanceId(), varId, processAttachment.getTypeId());

	}

	private String getAttachmentVariableName(String id) {
		return String.format("attachmentType_%s", id);
	}

	public SearchResult queryTasks(String status, int size, int index,String searchFilter, Authentication authentication) {
		TaskCriteria criteria = new TaskCriteria();

		if ("wip".equals(status)) {
			criteria.addProcessVariable(statusVarName, "case");
			criteria.addProcessVariable(statusVarName, "application");
			criteria.addProcessVariable(statusVarName, "amend");
			criteria.excludeExecutionVariable("escalation");
		} else if ("completed".equals(status) || "unsuccessful".equals(status)) {
			HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
			query.variableValueEquals(statusVarName, status);
			query.orderByProcessInstanceId().desc();
			
			if (size >= 0) {
				List<Response> page = createResponse(query.listPage(index, size));
				return new SearchResult(page, query.count(), index + size);
			}
			List<Response> list = createResponse(query.list());
			return new SearchResult(list, list.size(), 0);
		} else if ("escalated".equals(status)) {
			criteria.containsExecutionVariable("escalation");
		} else {
			if ("release".equals(status)) {
				criteria.addProcessVariable(statusVarName, "review");
				criteria.excludeVariable("maker", authentication.getName());
			} else {
				criteria.addProcessVariable(statusVarName, status);
			}
		}

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<String> groups = new ArrayList<>(authorities.size());
		for (GrantedAuthority grantedAuthority : authorities) {
			LOG.info("Authority: " + grantedAuthority.getAuthority());
			groups.add(grantedAuthority.getAuthority().replaceAll("ROLE_", ""));
		}

		criteria.addGroups(groups);
		
		if (size >= 0) {
			criteria.limit(size).offset(index);
		}
		
		if (searchFilter!=null && !searchFilter.isEmpty()){
			criteria.addSearchValue(searchFilter);
		}
		
		NativeTaskQuery query = criteria.buildQuery(managementService, taskService);

		if (size >= 0) {
			List<Response> page = createResponse(query.list());
			criteria.count(true);
			query = criteria.buildQuery(managementService, taskService);
			return new SearchResult(page, query.count(), index + size);
		}

		List<Response> list = createResponse(query.list());
		return new SearchResult(list, list.size(), 0);

	}

	private List<Response> createResponse(List<HistoricProcessInstance> processes) {
		List<Response> results = new ArrayList<>(processes.size());

		for (HistoricProcessInstance procInstance : processes) {
			Response response = new Response(procInstance);
			results.add(response);
		}

		return results;
	}

	private List<Response> createResponse(Collection<Task> tasks) {
		List<Response> results = new ArrayList<>(tasks.size());

		for (Task task : tasks) {
			results.add(new Response(task));
		}
		return results;
	}

	@Transactional(readOnly=false)
	public void processDealTasks(Long dealId, String expectedStatus, String newStatus) {
		TaskCriteria taskCriteria = new TaskCriteria();
		taskCriteria.addProcessVariable(statusVarName, expectedStatus);
		taskCriteria.addProcessVariable("dealId", dealId.toString());
		
		NativeTaskQuery query = taskCriteria.buildQuery(managementService, taskService);
		
		List<Task> tasks = query.list();
		if (tasks.isEmpty()) {
			throw new NotFoundException(String.format("Task with dealId %s and status %s not found", dealId, expectedStatus));			
		}
		
		if (tasks.size() > 1) {
			throw new RuntimeException(String.format("Too many tasks to update [dealId=%s, status=%s]", dealId, expectedStatus));
		}
		
		Task task = tasks.iterator().next();
		
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("status", newStatus);
		taskService.complete(task.getId(), taskVariables);
	}
	
	@Transactional(readOnly=false)
	public void processEscalations(String processReference, List<String> escalations) {
		Execution execution = runtimeService.createExecutionQuery()
				.processInstanceId(processReference)
				.messageEventSubscriptionName("Escalation Message")
				.singleResult();
		
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("escalations", escalations);
				
		runtimeService.messageEventReceived("Escalation Message", execution.getId(), vars);
	}
}
