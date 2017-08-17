package com.gmail.ramawathar.priyash.businessLogic;

import com.gmail.ramawathar.priyash.domain.Bgt_categories;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
import com.gmail.ramawathar.priyash.repository.Bgt_categoriesRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_notificationsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_trxnsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class ManageData {
	
	public void assocNotificationToCat(	Bgt_notificationsRepository allNotifcations, 
										Bgt_user_third_partyRepository userThirdPartyRepo, 
										Bgt_categoriesRepository bgt_categoriesRepository, 
										Bgt_trxnsRepository bgt_trxnsRepository,
										CategoryParamPOJO params){

		boolean valid = false;
		//get the reference from the transaction 
		valid = false;
		for (Bgt_trxns trxn : Bgt_trxnsRepository.findByCategory(params.getCategory().toUpperCase())) {
			valid = true; 
		}
		if (!(valid)){
			params.setStatus("ERROR");
			params.setStatusDesc("Category not found");
			return;
		}		
		
		//look up the edited third party
		LookupData lookup = new LookupData();
		String thirdParty = lookup.getThirdParty(params.getReference().toUpperCase());
		
		//check valid category
		valid = false;
		for (Bgt_categories category : bgt_categoriesRepository.findByCategory(params.getCategory().toUpperCase())) {
			valid = true; 
		}
		if (!(valid)){
			params.setStatus("ERROR");
			params.setStatusDesc("Category not found");
			return;
		}
		
		//check if thirdparty exists in the user third party mapping
		//if exists remove thirdparty if exists above
		Bgt_user_third_party userThirdPartyRec = null;
		for (Bgt_user_third_party thirdPartyRec : userThirdPartyRepo.findByUserThirdParty(thirdParty)) {
			userThirdPartyRec = thirdPartyRec; 
		}
		
		if (userThirdPartyRec != null){
			userThirdPartyRepo.delete(userThirdPartyRec);
		}
		
		//add new thirdparty and category mapping - TO DO
		
		//process all notifications that where not categorised - if can be categorised then set the status to "NONE"
		
	}

}
