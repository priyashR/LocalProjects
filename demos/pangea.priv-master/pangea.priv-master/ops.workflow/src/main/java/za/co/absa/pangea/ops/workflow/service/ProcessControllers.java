/*********************************************
 * Copyright 2016 Absa ©
 * 13 Aug 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.workflow.service;

import org.activiti.engine.task.Attachment;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import za.co.absa.pangea.ops.workflow.process.ProcessInitializer;

import java.io.InputStream;
import java.util.*;

/**
 * The Class ProcessControllers.
 */
@RestController
public class ProcessControllers {

	private static final Logger LOG = Logger.getLogger(ProcessControllers.class);

	@Autowired
	private ProcessServices service;

	@Autowired
	private ProcessInitializer initializer;

	/**
	 * Handle resource not found exception.
	 *
	 * @param ex the ex
	 */
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public void handleResourceNotFoundException(NotFoundException ex) {
		LOG.warn("user requested a resource which didn't exist", ex);
	}

	/**
	 * Attachments.
	 *
	 * @param processId the process id
	 * @return the list
	 */
	@RequestMapping(value = "/processes/{processId}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public List<ProcessAttachment> attachments(@PathVariable("processId") String processId) {

		return service.attachments(processId);
	}

	/**
	 * Content.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/attachments/{attachmentId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<InputStreamResource> content(@PathVariable("attachmentId") String id) {

		Attachment attachment = service.getAttachment(id);

		if (attachment == null) {
			throw new NotFoundException("Attachment not found");
		}
		InputStream content = service.readAttachment(attachment);

		if (content == null) {
			throw new NotFoundException("Attachment content not found");
		}

		InputStreamResource iss = new InputStreamResource(content);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(attachment.getType()));
		headers.set("Content-Disposition", "inline; filename=" + attachment.getName().replace(" ", "_"));

		return new ResponseEntity<>(iss, headers, HttpStatus.OK);
	}

	/**
	 * Update.
	 *
	 * @param processAttachment the process attachment
	 * @param id the id
	 */
	@RequestMapping(value = "/attachments/{attachmentId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@RequestBody ProcessAttachment processAttachment, @PathVariable("attachmentId") String id) {
		service.update(processAttachment, id);
	}

	/**
	 * Query tasks.
	 *
	 * @param status the status
	 * @param size the size
	 * @param index the index
	 * @param authentication the authentication
	 * @return the search result
	 */
	@RequestMapping(value = "/custom/query/tasks", method = RequestMethod.GET, produces = "application/json")
	public SearchResult queryTasks(@RequestParam("status") String status,
			@RequestParam(value="size", required = false, defaultValue = "-1") int size,
			@RequestParam(value="startIndex", defaultValue = "0") int index,
			@RequestParam(value="searchFilter") String searchFilter,
			Authentication authentication) {
		return service.queryTasks(status, size, index, searchFilter, authentication);
	}

	/**
	 * Creates the dummy process.
	 */
	@RequestMapping(value = "/custom/dummy/case", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void createDummyProcess() {
		initializer.startCaseProcess();
	}

	/**
	 * Creates the dummy swift process.
	 */
	@RequestMapping(value = "/custom/dummy/swift", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void createDummySwiftProcess() {
		initializer.startSWIFTProcess();
	}
	
	@RequestMapping(value = "/process/{id}/escalations", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void processEscalations(@PathVariable("id") String processId, @RequestBody Map<String, Object> request) {
		List<Object> escalations = (List<Object>) request.get("escalations");
		List<String> vars = new ArrayList<String>();
		for (Object entry : escalations) {
			Map<String, Object> res = (Map<String, Object>) entry;
			String name = (String) res.get("name");
			vars.add(name);
		}
		service.processEscalations(processId, vars);
	}

	@RequestMapping(value = "/user/settings", method = RequestMethod.GET)
	public UserSettingsDTO defaultTab(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		Set<String> groups = new HashSet<>(authorities.size());
		for (GrantedAuthority grantedAuthority : authorities) {
			LOG.info("Authority: " + grantedAuthority.getAuthority());
			groups.add(grantedAuthority.getAuthority().replaceAll("ROLE_", "").toLowerCase());
		}
		
		UserSettingsDTO result = new UserSettingsDTO();
		 
		if (groups.contains("maker")) {
			result.setDefaultTab("wip");
		} else if (groups.contains("checker")) {
			result.setDefaultTab("release");
		} else {
			result.setDefaultTab("new");
		}

		return result;
	}
}