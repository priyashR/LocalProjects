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
		try{
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
				String currentFullThirdParty = currentTrxn.getUserThirdParty();
				String thirdParty = lookup.getThirdParty(currentFullThirdParty);
				
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
				//add new thirdparty and category mapping
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
					userThirdPartyRec.setUser_third_party_desc(currentFullThirdParty);
					userThirdPartyRec.setCategory(currentCategory);
				}
				userThirdPartyRepo.save(userThirdPartyRec);
				
				//update transaction
				bgt_trxnsRepository.delete(currentTrxn);
				currentTrxn.setCategory(currentCategory);
				bgt_trxnsRepository.save(currentTrxn);
				
				//update notification by trans id
				for (Bgt_notifications notification : allNotifcations.findByTrxnId(currentTrxn.getTrxnId())){
					allNotifcations.delete(notification);
					notification.setNotification_status("UPDATED");
					allNotifcations.save(notification);
				}
				
				//update all other transactions by reference
				for (Bgt_trxns trxn : bgt_trxnsRepository.findByUserThirdParty(currentFullThirdParty)){
					valid = true; 
					bgt_trxnsRepository.delete(trxn);
					trxn.setCategory(currentCategory);
					bgt_trxnsRepository.save(trxn);
					
					for (Bgt_notifications notification : allNotifcations.findByTrxnId(trxn.getTrxnId())){
						allNotifcations.delete(notification);
						notification.setNotification_status("UPDATED");
						allNotifcations.save(notification);
					}
				}
			}catch (Exception e){
				params.setStatus("Error");
				params.setStatusDesc(e.getMessage());
			}
		
}
	
	public void createUpdateCategory(	Bgt_categoriesRepository bgt_categoriesRepository, 
										CategoryParamPOJO params){
		try{	
				boolean valid = false;
				String paramCat = params.getCategory().toUpperCase();
				String paramCatParent = params.getCat_parent().toUpperCase();
				String paramCatParentDesc = "";
				
				for (Bgt_categories category : bgt_categoriesRepository.findByCategory(paramCatParent)) {
					paramCatParentDesc = category.getCat_desc();
					valid = true; 
				}
				if (!(valid)){
					params.setStatus("ERROR");
					params.setStatusDesc("Category parent not found");
					return;
				}
				
				valid = false;
				for (Bgt_categories category : bgt_categoriesRepository.findByCategory(paramCat)) {
					bgt_categoriesRepository.delete(category);
					category.setCat_parent(paramCatParent);
					//category.setCat_desc(params.getCat_desc());
					bgt_categoriesRepository.save(category);
					valid = true; 
				}
				
				if (!(valid)){
					Bgt_categories category = new Bgt_categories();
					category.setIs_default_cat("N");
					category.setUser_email("PRIYASH.RAMAWTHAR@GMAIL.COM");
					category.setCategory(paramCat);
					category.setCat_parent(paramCatParent);
					category.setCat_desc(params.getCat_desc());
					bgt_categoriesRepository.save(category);
					
				}	
			
			}catch (Exception e){
				params.setStatus("Error");
				params.setStatusDesc(e.getMessage());
			}
}

}
