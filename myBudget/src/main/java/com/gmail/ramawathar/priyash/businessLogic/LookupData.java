package com.gmail.ramawathar.priyash.businessLogic;

import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.Lookup;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class LookupData implements Lookup{
	
	public String category(String thirdParty, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		boolean found = false;
		String cat = "UNCATEGORISED";
		try{
			for (Bgt_user_third_party thirdPartyRec : bgt_user_third_partyRepository.findByUserThirdParty(thirdParty)) {
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

}
