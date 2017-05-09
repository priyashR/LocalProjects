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
package za.co.absa.pangea.ops.workflow.process;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class ProcessInitializer.
 */
@Service
@Transactional
public class ProcessInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(ProcessInitializer.class);

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	/**
	 * Instantiates a new process initializer.
	 */
	public ProcessInitializer() {
		super();
	}

	/**
	 * Start case process.
	 */
	public void startCaseProcess() {
		Map<String, Object> vars = new HashMap<>();
		vars.put("subject", "A BGI Message");
		vars.put("sender", "test@test.com");
		vars.put("recipients", "bgicenter@bgicentre.co.za");
		vars.put("incomingAttachmentCount", 1);
		vars.put("channel", "EMAIL");
		vars.put("status", "new");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseProcess", vars);
		String procId = processInstance.getId();

		InputStream fis = this.getClass().getResourceAsStream("/static/Gtee Application.pdf");

		taskService.createAttachment("application/pdf", null, processInstance.getProcessInstanceId(), "attachment.pdf",
				"attachment.pdf", fis);

		LOG.info(String.format("Started process with id %s", procId));

		Map<String, Object> processVariables = processInstance.getProcessVariables();

		for (Map.Entry<String, Object> var : processVariables.entrySet()) {
			LOG.info("Variable %s val %s", var.getKey(), var.getValue());
		}
	}

	/**
	 * Start swift process.
	 */
	public void startSWIFTProcess() {

		String swiftFile = "/static/meridian-swift.xml";
		
		InputStream fis = this.getClass().getResourceAsStream(swiftFile);

		Element element = getXMLElementFromFile(fis);

		String sender = getString("SenderAddress", element);
		String recipient = getString("DestinationAddress", element);
		String meridianMessageType = getString("MeridianMessageType", element);
		String externalMessageType = getString("ExternalMessageType", element);
		String hostReference = getString("HostReference", element);

		Map<String, Object> vars = new HashMap<>();
		vars.put("subject", "Inbound SWIFT");
		vars.put("sender", sender);
		vars.put("recipient", recipient);
		vars.put("incomingAttachmentCount", 1);
		vars.put("status", "new");
		vars.put("channel", "SWIFT");
		vars.put("MeridianMessageType", meridianMessageType);
		vars.put("ExternalMessageType", externalMessageType);
		vars.put("HostReference", hostReference);
		
		
		

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("caseProcess", vars);
		String procId = processInstance.getId();

		fis = this.getClass().getResourceAsStream(swiftFile);

		taskService.createAttachment("application/xml", null, processInstance.getProcessInstanceId(), "swift.xml",
				hostReference, fis);

		LOG.info(String.format("Started process with id %s", procId));

		Map<String, Object> processVariables = processInstance.getProcessVariables();

		for (Map.Entry<String, Object> var : processVariables.entrySet()) {
			LOG.info("Variable %s val %s", var.getKey(), var.getValue());
		}
	}

	/**
	 * Gets the XML element from file.
	 *
	 * @param fis
	 *            the file input stream
	 * @return the XML element from file
	 */
	private Element getXMLElementFromFile(InputStream fis) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(fis);
			return document.getDocumentElement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the string.
	 *
	 * @param tagName
	 *            the tag name
	 * @param element
	 *            the element
	 * @return the string
	 */
	private String getString(String tagName, Element element) {
		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(0).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(0).getNodeValue();
			}
		}

		return null;
	}

}
