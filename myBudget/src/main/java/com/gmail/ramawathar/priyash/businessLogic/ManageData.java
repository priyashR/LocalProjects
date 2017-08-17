package com.gmail.ramawathar.priyash.businessLogic;

import com.gmail.ramawathar.priyash.domain.Bgt_categories;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
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
		Bgt_trxns currentTrxn = new Bgt_trxns();
		valid = false;
		for (Bgt_trxns trxn : bgt_trxnsRepository.findByTrxnId((long) Long.parseLong(params.getTransNumber()))){
			valid = true; 
			currentTrxn = trxn;
		}
		if (!(valid)){
			params.setStatus("ERROR");
			params.setStatusDesc("Transaction not found");
			return;
		}		
		
		//look up the edited third party
		LookupData lookup = new LookupData();
		String thirdParty = lookup.getThirdParty(currentTrxn.getUser_third_party());
		
		//check valid category
		String currentCategory = params.getCategory().toUpperCase();
		valid = false;
		for (Bgt_categories category : bgt_categoriesRepository.findByCategory(currentCategory)) {
			valid = true; 
		}
		if (!(valid)){
			params.setStatus("ERROR");
			params.setStatusDesc("Category not found");
			return;
		}
		
		//check if thirdparty exists in the user third party mapping
		//remove thirdparty if exists above
		//add new thirdparty and category mapping - TO DO
		Bgt_user_third_party userThirdPartyRec = null;
		for (Bgt_user_third_party thirdPartyRec : userThirdPartyRepo.findByUserThirdParty(thirdParty)) {
			userThirdPartyRec = thirdPartyRec;
		}
		
		if (userThirdPartyRec != null){
			userThirdPartyRepo.delete(userThirdPartyRec);		
			userThirdPartyRec.setCategory(currentCategory);
		}else{
			userThirdPartyRec.setUser_email("PRIYASH.RAMAWTHAR@GMAIL.COM");
			userThirdPartyRec.setUser_third_party("thirdParty");
			userThirdPartyRec.setUser_third_party_desc(currentTrxn.getUser_third_party());
			userThirdPartyRec.setCategory(currentCategory);
		}
		userThirdPartyRepo.save(userThirdPartyRec);
		
		//update transaction
		//update notification by trans id
		
		//update all other transactions by reference
		//update all other notifications by reference trans id
		
	}

}
