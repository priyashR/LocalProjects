package za.co.absa.pangea.ops.workflow.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.TaskInfo;
import org.activiti.rest.common.util.DateToStringSerializer;
import org.activiti.rest.service.api.engine.variable.RestVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Response {

	protected String id;
	protected String url;
	protected String owner;
	protected String source;
	protected String assignee;
	protected String delegationState;
	protected String name;
	protected String description;
	@JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
	protected Date createTime;
	@JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
	protected Date dueDate;
	protected int priority;
	protected boolean suspended;
	protected String taskDefinitionKey;
	protected String tenantId;
	protected String category;
	protected String formKey;

	// References to other resources
	protected String parentTaskId;
	protected String parentTaskUrl;
	protected String executionId;
	protected String executionUrl;
	protected String processInstanceId;
	protected String processInstanceUrl;
	protected String processDefinitionId;
	protected String processDefinitionUrl;

	protected List<RestVariable> variables = new ArrayList<>();

	public Response(TaskInfo task) {
		setId(task.getId());
		setOwner(task.getOwner());
		setAssignee(task.getAssignee());
		setName(task.getName());
		setDescription(task.getDescription());
		setCreateTime(task.getCreateTime());
		setDueDate(task.getDueDate());
		setPriority(task.getPriority());
		setTaskDefinitionKey(task.getTaskDefinitionKey());
		setParentTaskId(task.getParentTaskId());
		setExecutionId(task.getExecutionId());
		setCategory(task.getCategory());
		setProcessInstanceId(task.getProcessInstanceId());
		setProcessDefinitionId(task.getProcessDefinitionId());
		setTenantId(task.getTenantId());
		setFormKey(task.getFormKey());
	}

	public Response(HistoricProcessInstance processInstance) {
		setId(processInstance.getId());
		setOwner(processInstance.getStartUserId());
		setAssignee(processInstance.getStartUserId());
		setName(processInstance.getName());
		setDescription(processInstance.getDescription());
		setCreateTime(processInstance.getStartTime());
		setExecutionId(processInstance.getId());
		setProcessInstanceId(processInstance.getId());
		setProcessDefinitionId(processInstance.getProcessDefinitionId());
		setTenantId(processInstance.getTenantId());
	}

	protected String getDelegationStateString(DelegationState state) {
		String result = null;
		if (state != null) {
			result = state.toString().toLowerCase();
		}
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getDelegationState() {
		return delegationState;
	}

	public void setDelegationState(String delegationState) {
		this.delegationState = delegationState;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String getParentTaskUrl() {
		return parentTaskUrl;
	}

	public void setParentTaskUrl(String parentTaskUrl) {
		this.parentTaskUrl = parentTaskUrl;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getExecutionUrl() {
		return executionUrl;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setExecutionUrl(String executionUrl) {
		this.executionUrl = executionUrl;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessInstanceUrl() {
		return processInstanceUrl;
	}

	public void setProcessInstanceUrl(String processInstanceUrl) {
		this.processInstanceUrl = processInstanceUrl;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionUrl() {
		return processDefinitionUrl;
	}

	public void setProcessDefinitionUrl(String processDefinitionUrl) {
		this.processDefinitionUrl = processDefinitionUrl;
	}

	public List<RestVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<RestVariable> variables) {
		this.variables = variables;
	}

	public void addVariable(RestVariable variable) {
		variables.add(variable);
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
