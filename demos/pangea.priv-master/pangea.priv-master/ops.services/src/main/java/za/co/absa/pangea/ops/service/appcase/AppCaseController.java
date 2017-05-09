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
package za.co.absa.pangea.ops.service.appcase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class AppCaseController.
 */
@RestController
@RequestMapping("/case")
public class AppCaseController {

//	@Autowired
//	CaseRepository caseRepository;	
//	@Autowired
//	CaseCustomerRepository caseCustomerRepository;
//	@Autowired
//	private AccountRepository accountRepository;
	
//	@Autowired
//	private EscalationResolverService escalationsService;
	
	@Autowired
	private AppCaseService appCaseService;

//	/**
//	 * Save case instance.
//	 *
//	 * @param data
//	 *            the data
//	 * @return the case dto
//	 */
//	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public AppCaseDTO saveAppCaseInstance(@RequestBody AppCaseDTO data) {
//		data.setId(null);
//		return mapper.getDtoFromEntity2(caseService.save(mapper.getEntityFromDto2(data, null)));
//		Cases cases = caseRepository.save(mapper.getEntityFromDto(null, data));
//
//		linkDefaultAccount(cases, data);
//
//		cases = caseRepository.findOne(cases.getId());
//
//		return mapper.getDtoFromEntity(cases, null, true);
//	}

//	/**
//	 * Link default account.
//	 *
//	 * @param cases
//	 *            the cases
//	 * @param data
//	 *            the data
//	 */
//	private void linkDefaultAccount(Cases cases, CaseDTO data) {
//		Account accountDest = new Account();
//		Account account = accountRepository.findOne(data.getCustomerDTO().getDefaultAccount().getId());
//
//		try {
//			BeanUtils.copyProperties(accountDest, account);
//		} catch (IllegalAccessException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//		accountDest.setId(null);
//		accountDest.setCustomer(cases.getCustomer());
//
//		accountDest = accountRepository.save(accountDest);
//
//		cases.getCustomer().getCustomerAccounts().add(accountDest);
//	}

	/**
	 * Save case instance.
	 *
	 * @param id
	 *            the id
	 * @param data
	 *            the data
	 * @return the case dto
	 */
	@RequestMapping(value = "/{id}", method = { RequestMethod.PUT,
			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
	public AppCaseDTO saveAppCaseInstance(@PathVariable("id") Long id, @RequestBody AppCaseDTO data) {
		return appCaseService.save(id, data);		
	}

//	/**
//	 * Cases.
//	 *
//	 * @return the list
//	 */
//	@RequestMapping(method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<CaseDTO> cases() {
//		return caseService.findAll().stream().map((caseInstance) ->
//				mapper.getDtoFromEntity(caseInstance, null, true)).collect(Collectors.toList());
//	}

//	/**
//	 * Case instance.
//	 *
//	 * @param id
//	 *            the id
//	 * @return the case dto
//	 */
//	@RequestMapping(value = "/{id}", method = { RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public CaseDTO caseInstance(@PathVariable("id") Long id) {
//		return mapper.getDtoFromEntity(caseRepository.getOne(id), null, true);
//	}

//	/**
//	 * Case customer.
//	 *
//	 * @param id
//	 *            the id
//	 * @return the customer
//	 */
//	@RequestMapping(value = "/{id}/customer", method = {
//			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public Customer caseCustomer(@PathVariable("id") Long id) {
//		Cases cases = caseRepository.getOne(id);
//		return cases.getCustomer();
//	}

//	/**
//	 * Create and link a case customer
//	 */
//	@RequestMapping(value = "/{id}/customer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public CaseCustomerDTO caseCustomer(@PathVariable("id") Long id, @RequestBody CaseCustomerDTO data) {
//		Cases cases = caseRepository.getOne(id);
//		CaseCustomer caseCustomer = mapper.getEntityFromCustomerDto(data, cases.getCustomer());
//		caseCustomerRepository.save(caseCustomer);
//		cases.setCustomer(caseCustomer);
//		caseRepository.save(cases);
//		return mapper.getDtoFromCustomerEntity(cases.getCustomer());
//	}

//	/**
//	 * Gets the escalations.
//	 *
//	 * @param id
//	 *            the id
//	 * @return the escalations
//	 */
//	@RequestMapping(value = "/{id}/escalations", method = {
//			RequestMethod.GET }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public List<EscalationDTO> getEscalations(@PathVariable("id") Long id) {
//		List<EscalationDTO> results = new ArrayList<>();
//
//		Cases _case = caseRepository.findOne(id);
//
//		PreCheckEscalation escalationParams = new PreCheckEscalation(PrecheckPhase.CASE_FINAL, _case);
//
//		List<PrecheckCategoryLink> escalations = escalationsService.getCasePreCheckEscalations(escalationParams);
//
//		for (PrecheckCategoryLink link : escalations) {
//
//			EscalationDTO dto = new EscalationDTO();
//			dto.setCode(link.getPreCheck().getCode());
//			dto.setDescription(link.getPreCheck().getDescription());
//			//dto.setTemplateTypeCode(link.getEscalationTemplateType().getCode());
//			results.add(dto);
//
//		}
//
//		return results;
//	}

//	@Autowired
//	private EventService service;
//	
//	@RequestMapping(value = "/{id}/prechecks", method = {
//			RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE)
//	public void prechecks(@PathVariable("id") Long id) {
//		Cases _case = caseRepository.findOne(id);
//
//		PreCheckEscalation escalationParams = new PreCheckEscalation(PrecheckPhase.CASE_FINAL, _case);
//
//		List<PrecheckCategoryLink> escalations = escalationsService.getCasePreCheckEscalations(escalationParams);
//		
//		if (escalations != null && !escalations.isEmpty()) {
//			WorkflowEvent event = new WorkflowEvent();
//			event.setCaseId(id);
//			event.setDealId(0L);
//			event.setName("precheck");
//			event.setEscalations(escalations.stream().map(t -> t.getPreCheck().getCode()).collect(Collectors.toList()));
//
//			service.handle(event);
//		}
//	}
}