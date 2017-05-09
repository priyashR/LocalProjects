package za.co.absa.pangea.ops.service.appcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.absa.pangea.ops.domain.appcase.AppCase;
import za.co.absa.pangea.ops.domain.appcase.AppCaseRepository;

@Service
public class AppCaseService {
	
	private static Logger logger = LoggerFactory.getLogger(AppCaseService.class);

	@Autowired
	private AppCaseRepository appCaseRepo;
	
	@Autowired
	private AppCaseMapper mapper;

//	public List<Cases> findAll() {
//		return caseRepository.findAll();
//	}

	public AppCaseDTO save(Long appCaseId, AppCaseDTO data) {
		// originally from this service method.
//		return caseRepository.save(instance);
		
		// taken from original controller
		AppCase appCase = null;
		if (appCaseId != null) {
			appCase = appCaseRepo.findOne(appCaseId);
		}
		if (appCase == null) {
			appCase = new AppCase();
		}
		
		mapper.populateEntityFromDTO(appCase, data);
		AppCase savedAppCase = appCaseRepo.save(appCase);
		AppCaseDTO dto = mapper.toDTO(savedAppCase);
		
//		AppCaseDTO dto = new AppCaseDTO();
//		dto.setId(50L);
//		dto.setStepCode(data.getStepCode());
//		dto.setProductId(data.getProductId());
//		dto.setCustomerName(data.getCustomerName());
//		dto.setCustomerAccountNumber(data.getCustomerAccountNumber());
		return dto;
	}
}
