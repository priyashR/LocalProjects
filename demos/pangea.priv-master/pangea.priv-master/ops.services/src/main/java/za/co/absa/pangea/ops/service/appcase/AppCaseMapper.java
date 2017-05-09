/*********************************************
 * Copyright 2016 Absa ©
 * 02 Aug 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.service.appcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import za.co.absa.pangea.ops.domain.appcase.AppCase;

/**
 * The Class AppCaseDTOMapper.
 */
@Component
public class AppCaseMapper {

	private static Logger logger = LoggerFactory.getLogger(AppCaseMapper.class);

//	@Autowired
//	private DealRepository dealRepository;
//	@Autowired
//	private StepTypeRepository stepTypeRepository;
//	@Autowired
//	private ProductTypeRepository productTypeRepository;
//	@Autowired
//	private CustomerRepository customerRepository;
//	@Autowired
//	private CaseCustomerRepository caseCustomerRepository;
//	@Autowired
//	private PreCheckRepository preCheckRepository;
//	@Autowired
//	private CustomerDTOMapper mapper;

//	/**
//	 * Gets the dto from entity.
//	 *
//	 * @param entity the entity
//	 * @param dto the dto
//	 * @param mappit the mappit
//	 * @return the dto from entity
//	 */
//	public AppCaseDTO getDtoFromEntity(Cases entity, AppCaseDTO dto, boolean mappit) {
//		if (dto == null || !mappit) {
//			dto = new AppCaseDTO();
//		}
//		if (mappit) {
//			dto.setId(entity.getId());
//			dto.setStepCode(entity.getStep() != null ? entity.getStep().getCode() : null);
//			dto.setProductClassificationTypeCode(
//					entity.getProductClassification() != null ? entity.getProductClassification().getId() : null);
//			dto.setProductTypeCode(entity.getProductType() != null ? entity.getProductType().getId() : null);
//			dto.setProductCode(entity.getProductType() != null ? entity.getProductType().getProduct().getId() : null);
//
//			if (entity.getCustomer() != null) {
//				CaseCustomerDTO customerDTO = null;
//				customerDTO = (CaseCustomerDTO) mapCaseCustomer(customerDTO, entity.getCustomer());
//				customerDTO.setId(entity.getCustomer().getId());
//				dto.setCustomerDTO(customerDTO);
//			}
//
//			dto.getPreChecks().clear();
//			for (CasePreCheck preCheck : entity.getPrechecks()) {
//				dto.getPreChecks().add(new CasePreCheckDTO(preCheck.getPreCheck().getId(),
//						preCheck.getPreCheck().getCode(), preCheck.getSelectedOption().getCode()));
//			}
//
//			dto.setStandardTemplateCode(entity.getStandardTemplateCode());
//			dto.setWithEngagement(entity.getWithEngagement());
//			dto.setDeliveryTypeCode(entity.getDelivery() == null ? null : entity.getDelivery().name());
//
//			dto.setDealId(entity.getDeal() != null ? entity.getDeal().getId() : null);
//		}
//
//		return dto;
//	}
//
//	/**
//	 * Gets the entity from dto.
//	 *
//	 * @param entity the entity
//	 * @param dto the dto
//	 * @return the entity from dto
//	 */
//	public Cases getEntityFromDto(Cases entity, AppCaseDTO dto) {
//		if (entity == null) {
//			entity = new Cases();
//		}
//		entity.setStep(dto.getStepCode() != null ? stepTypeRepository.findOne(dto.getStepCode()) : null);
//		entity.setProductType(
//				dto.getProductTypeCode() != null ? productTypeRepository.findOne(dto.getProductTypeCode()) : null);
//		entity.setProductClassification(
//				entity.getProductType() != null ? entity.getProductType().getProductClassification() : null);
//
//		if (entity.getCustomer() == null && dto.getCustomerId() != null) {
//			CaseCustomer caseCustomer = null;
//			CaseCustomerDTO customerDTO = new CaseCustomerDTO();
//			customerDTO.setId(dto.getCustomerId());
//			caseCustomer = (CaseCustomer) mapCaseCustomer(customerDTO, caseCustomer);
//			entity.setCustomer(caseCustomer);
//		} else {
////			updateContact(entity.getCustomer().getContacts().iterator().next(),
////					dto.getCustomerDTO().getDefaultContact());
//		}
//
//		List<CasePreCheck> preChecks = new ArrayList<>();
//
//		for (CasePreCheckDTO preCheckDTO : dto.getPreChecks()) {
//
//			PreCheck preCheck = preCheckRepository.findOne(preCheckDTO.getId());
//
//			for (PreCheckOption option : preCheck.getOptions()) {
//				if (preCheckDTO.getSelectedvalue() != null) {
//					if (preCheckDTO.getSelectedvalue().equals(option.getCode())) {
//						boolean found = false;
//						for (CasePreCheck pCheck : entity.getPrechecks()) {
//							if (pCheck.getPreCheck().getId() == preCheckDTO.getId()) {
//								found = true;
//								pCheck.setSelectedOption(option);
//								preChecks.add(pCheck);
//								break;
//							}
//						}
//						if (!found) {
//							CasePreCheck casePreCheck = new CasePreCheck();
//							casePreCheck.setCases(entity);
//							casePreCheck.setPreCheck(preCheck);
//							casePreCheck.setSelectedOption(option);
//							preChecks.add(casePreCheck);
//						}
//					}
//				}
//			}
//		}
//		entity.getPrechecks().addAll(preChecks);
//		entity.getPrechecks().retainAll(preChecks);
//
//		entity.setStandardTemplateCode(dto.getStandardTemplateCode());
//		entity.setWithEngagement(dto.getWithEngagement());
//		entity.setDelivery(
//				dto.getDeliveryTypeCode() == null ? null : DeliveryMethod.fromCode(dto.getDeliveryTypeCode()));
//
//		entity.setDeal(dto.getDealId() != null ? dealRepository.getOne(dto.getDealId()) : null);
//
//		return entity;
//	}
//
//	/**
//	 * Map case customer.
//	 *
//	 * @param dto the dto
//	 * @param entity the entity
//	 * @return the object
//	 */
//	private Object mapCaseCustomer(CaseCustomerDTO dto, CaseCustomer entity) {
//		if (entity == null && dto != null && dto.getId() != null) {
//			Customer customer = customerRepository.findOne(dto.getId());
//			if (customer != null) {
//				CaseCustomer caseCustomer = new CaseCustomer();
//
//				caseCustomer.setId(null);
//				caseCustomer.setAddresses(new ArrayList<>());
//				caseCustomer.setContacts(new ArrayList<>());
//				caseCustomer.setCustomerAccounts(new ArrayList<>());
//
//				Contact contactDest = new Contact();
//				Contact contactFrom = customer.getContacts().iterator().next();
//				contactDest.setId(null);
//				contactDest.setContactType(contactFrom.getContactType());
//				contactDest.setMobileNumber(contactFrom.getMobileNumber());
//				contactDest.setOfficeNumber(contactFrom.getOfficeNumber());
//				contactDest.setEmail(contactFrom.getEmail());
//				contactDest.setAlternativeEmail(contactFrom.getAlternativeEmail());
//				contactDest.setFax(contactFrom.getFax());
//				contactDest.setName(contactFrom.getName());
//				contactDest.setMethod(contactFrom.getMethod());
//				contactDest.setCustomer(caseCustomer);
//
//				caseCustomer.getContacts().add(contactDest);
//				caseCustomer.setRelatedCustomer(customer);
//				caseCustomer.setPriority(customer.getPriority());
//				caseCustomer.setName(customer.getName());
//				caseCustomer.setIdType(customer.getIdType());
//				caseCustomer.setIdNumber(customer.getIdNumber());
//				caseCustomer.setKycStatus(customer.getKycStatus());
//				caseCustomer.setRmName(customer.getRmName());
//
//				entity = caseCustomer;
//				return entity;
//			}
//
//		}
//		if (dto == null && entity != null && entity.getId() != null) {
//			CaseCustomer customer = caseCustomerRepository.findOne(entity.getId());
//			dto = new CaseCustomerDTO();
//			dto.setId(customer.getId());
//			dto.setName(customer.getName());
//			if(customer.getIdType() != null) {
//				dto.setIdTypeCode(customer.getIdType().getDescription());
//			}
//			dto.setIdNumber(customer.getIdNumber());
//			dto.setKycStatus(customer.getKycStatus());
//			dto.setRmName(customer.getRmName());
////			dto.setDefaultContact(customer.getContacts().size() > 0 ? customer.getContacts().iterator().next() : null);
////			dto.setDefaultAccount(customer.getCustomerAccounts().size() > 0
////					? mapAccount2Dto(customer.getCustomerAccounts().iterator().next()) : null);
//			if(customer.getRelatedCustomer() != null) {
//				dto.setRelatedCustomerId(customer.getRelatedCustomer().getId());
//			}
//			dto.setPriority(customer.getPriority());
//			return dto;
//		}
//		return null;
//	}
//
//	/**
//	 * Map account2 dto.
//	 *
//	 * @param account the account
//	 * @return the account dto
//	 */
//	private AccountDTO mapAccount2Dto(Account account) {
//		AccountDTO accountDTO = new AccountDTO();
//		accountDTO.setId(account.getId());
//		accountDTO.setAccountNo(account.getAccountNo());
//		accountDTO.setCurrency(account.getCurrency());
//		accountDTO.setBank(
//				new BankDTO(account.getBank().getId(), account.getBank().getCode(), account.getBank().getDescription(),
//						account.getBank().getCurrencyType(), account.getBank().getCountryOfBusiness()));
//		accountDTO.setBranch(new BranchDTO(account.getBranch().getId(), account.getBranch().getCode(),
//				account.getBranch().getName()));
//		accountDTO.setIsDefaultAccount(true);
//		return accountDTO;
//	}
//
//	/**
//	 * Update contact.
//	 *
//	 * @param contact the contact
//	 * @param newContact the new contact
//	 * @return the contact
//	 */
//	private Contact updateContact(Contact contact, Contact newContact) {
//		contact.setName(newContact.getName());
//		contact.setEmail(newContact.getEmail());
//		contact.setOfficeNumber(newContact.getOfficeNumber());
//		contact.setMobileNumber(newContact.getMobileNumber());
//		contact.setFax(newContact.getFax());
//		contact.setMethod(newContact.getMethod());
//		contact.setContactType(newContact.getContactType());
//		contact.setAlternativeEmail(newContact.getAlternativeEmail());
//		return contact;
//	}

	protected void populateEntityFromDTO(AppCase entity, AppCaseDTO dto) {
		entity.setCustomerAccountNumber(dto.getCustomerAccountNumber());
		entity.setCustomerName(dto.getCustomerName());
		entity.setProductId(dto.getProductId());
		entity.setStepCode(dto.getStepCode());
	}
	
//	protected AppCase toEntity(AppCaseDTO dto) {
//		if(existingEntity == null) {
//			existingEntity = new AppCase();
//		}
//		existingEntity.setStep(dto.getStepCode() != null ? stepTypeRepository.findOne(dto.getStepCode()) : null);
//		existingEntity.setProductType(
//				dto.getProductTypeCode() != null ? productTypeRepository.findOne(dto.getProductTypeCode()) : null);
//		existingEntity.setProductClassification(
//				entity.getProductType() != null ? entity.getProductType().getProductClassification() : null);
//		existingEntity.setStandardTemplateCode(dto.getStandardTemplateCode());
//		existingEntity.setWithEngagement(dto.getWithEngagement());
//		existingEntity.setDelivery(
//				dto.getDeliveryTypeCode() == null ? null : DeliveryMethod.fromCode(dto.getDeliveryTypeCode()));
//		return existingEntity;
//	}

	public AppCaseDTO toDTO(AppCase entity) {
		AppCaseDTO dto = new AppCaseDTO();
		dto.setId(entity.getId());
		dto.setStepCode(entity.getStepCode());
		dto.setProductId(entity.getProductId());
		dto.setCustomerName(entity.getCustomerName());
		dto.setCustomerAccountNumber(entity.getCustomerAccountNumber());
		return dto;
	}

//	public CaseCustomer getEntityFromCustomerDto(CaseCustomerDTO dto, CaseCustomer customer) {
//		CaseCustomer caseCustomer = null;
//		if(customer == null || customer.getId() == null) {
//			caseCustomer = new CaseCustomer();
//			caseCustomer.setRelatedCustomer(customerRepository.findOne(dto.getId()));
//		}
//		else {
//			caseCustomer = caseCustomerRepository.findOne(customer.getId());
//		}
//		caseCustomer = (CaseCustomer) mapper.getEntityFromDto(dto, caseCustomer);
//		return caseCustomer;
//	}
//
//	public CaseCustomerDTO getDtoFromCustomerEntity(CaseCustomer customer) {
//		CaseCustomerDTO dto = new CaseCustomerDTO();
//		dto = (CaseCustomerDTO) mapper.customerEntityToDto(customer, dto);
//		dto.setRelatedCustomerId(customer.getRelatedCustomer() == null ? null : customer.getRelatedCustomer().getId());
//		return dto;
//	}
}
