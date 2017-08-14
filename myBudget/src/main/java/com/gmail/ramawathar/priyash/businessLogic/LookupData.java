package com.gmail.ramawathar.priyash.businessLogic;

import java.util.StringTokenizer;

import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.Lookup;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class LookupData implements Lookup{
	
	public String category(String thirdParty, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		boolean found = false;
		String cat = "UNCATEGORISED";
		try{
			for (Bgt_user_third_party thirdPartyRec : bgt_user_third_partyRepository.findByUserThirdParty(getThirdParty(thirdParty))) {
				found = true;
				cat = thirdPartyRec.getCategory(); 
			}
		} catch (Exception e){
			e.printStackTrace();
			n.setNotification_type("ACTION");
			n.setNotification_desc("Thirdy party not categorised: "+e);
			n.setNotification_action("INVESTIGATE");
			
		}
		
		if (found){
			return cat; 
		}

		n.setNotification_type("ACTION");
		n.setNotification_desc("Thirdy party not categorised");
		n.setNotification_action("CATEGORISE");
		return cat;
		
		
	}
	
	public String getThirdParty(String thirdParty){
		
		String reference = thirdParty;

		StringTokenizer defaultTokenizer = new StringTokenizer(reference," ");
		int pos = 0;
		String token= "";
		
		if (defaultTokenizer.hasMoreTokens()){
			token = defaultTokenizer.nextToken();
			reference = token;
		}
		
		if ((defaultTokenizer.hasMoreTokens())&&(reference.length() == 1)){
			token = defaultTokenizer.nextToken();
			reference = reference+" "+token;
		}
			
		return reference;
		
		//tokenise
		//if token 1 is 1 character the add the second part to it and return
		//else return first token
		// use this method when checking above and inserting into category too
	}

}
