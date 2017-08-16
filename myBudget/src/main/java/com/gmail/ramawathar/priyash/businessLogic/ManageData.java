package com.gmail.ramawathar.priyash.businessLogic;

import com.gmail.ramawathar.priyash.domain.Bgt_categories;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
import com.gmail.ramawathar.priyash.repository.Bgt_categoriesRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_notificationsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class ManageData {
	
	public void assocNotificationToCat(Bgt_notificationsRepository allNotifcations, Bgt_user_third_partyRepository userThirdPartyRepo, Bgt_categoriesRepository bgt_categoriesRepository, AssociationsHandler params, Bgt_notifications n){
		
		LookupData lookup = new LookupData();
		String thirdParty = lookup.getThirdParty(params.getReference().toUpperCase());
		
		//check valid category
		boolean validCategory = false;
		for (Bgt_categories category : bgt_categoriesRepository.findByCategory(params.getCategory().toUpperCase())) {
			validCategory = true; 
		}
		if (!(validCategory)){
			n.setNotification_status("ERROR");
			n.setNotification_desc("Category not found");
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
		
		//process all notifications that where not categorised - TO DO
		
	}

}
