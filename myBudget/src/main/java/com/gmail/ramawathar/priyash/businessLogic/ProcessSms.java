package com.gmail.ramawathar.priyash.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.inject.Inject;

import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class ProcessSms {
	


	
	private Bgt_user_third_partyRepository bgt_user_third_partyRepository;
	
	private Orig_SMS o_sms;
	
	public ProcessSms(Orig_SMS o_sms){
		this.o_sms = o_sms;
	}
	
	public ProcessSms(Orig_SMS o_sms, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		this.o_sms = o_sms;
		this.bgt_user_third_partyRepository = bgt_user_third_partyRepository;
	}
	
	public Bgt_trxns process(Bgt_notifications n){
		
		Bgt_trxns trxn = new Bgt_trxns();
		String sms = o_sms.getMessage();
		
		if (sms.toUpperCase().startsWith("ABSA")) {
			trxn = processABSA(sms,n);
		}
		
		return trxn;
	}
	
	private Bgt_trxns processABSA(String processSMS, Bgt_notifications n){

		Bgt_trxns trxn = new Bgt_trxns();
		String sms = processSMS.toUpperCase().replace("ABSA:", "ABSA,");
		sms = sms.replace(", ", ",");
		String payType= "PUR";
		
		StringTokenizer defaultTokenizer = new StringTokenizer(sms,",");
		String balance = "";
        int pos = 0;
        
        trxn.setUser_email(o_sms.getUser_email());

        
        String token;
        try{
	        while ((defaultTokenizer.hasMoreTokens()) && (!(n.getNotification_type().equalsIgnoreCase("ERROR"))))
	        {
	        	token = defaultTokenizer.nextToken();
	        	pos++;
	        	switch (pos){
	        	case 2:
	        		//verify user account and if not found set the notification type to ERROR
	        		if (token.substring(0,3).equalsIgnoreCase("BAL")){
						n.setNotification_type("ERROR");
						n.setNotification_desc("Balance updates are not currently handled by the application");
						n.setNotification_action("INVESTIGATE");
		        		break;
	        		}
	        		trxn.setUser_account(token);
	        		break;
	        	case 3:
	        		switch(token.toUpperCase()){
	        		case "PUR":
	        			trxn.setTrxn_type("O");
	        			break;
	        		case "DEP":
	        			trxn.setTrxn_type("I");
	        			break;
		        	default:
						n.setNotification_type("ERROR");
						n.setNotification_desc("Unknown transaction type - cannot proceed");
						n.setNotification_action("INVESTIGATE");
		        		break;
	        		}
	        		break;
	        	case 4:
	        		DateFormat formatter = new SimpleDateFormat("dd/MM/yy"); 
	        		Date tranDate = new Date(1);
					try {
						tranDate = formatter.parse(token.substring(0,8));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						n.setNotification_type("ERROR");
						n.setNotification_desc("Problem with the transaction date - cannot proceed");
						n.setNotification_action("INVESTIGATE");
						e.printStackTrace();
					} 
	        		trxn.setTrxn_date(tranDate);
	        		
	        		if ((token.substring(9, 12)).equalsIgnoreCase("SET")){
	        			payType = "SET";
	        		}
	        			System.out.println("payType: "+payType);
	        		break;
	        	case 5:
	        		trxn.setUser_third_party(token.toUpperCase());
	                //lookup category here
	                trxn.setCategory(getCategory(token.toUpperCase(),n));
	                
	        		break;
	        	case 6:
	        		BigDecimal money = new BigDecimal(token.substring(1).replaceAll(",", ""));
	        		trxn.setTrxn_amount(money.abs());
	        		break;
	        	case 7:
	        		balance = token;
	        		break;
	        	case 8:
	        		balance = balance+token;
	        		break;
	        	case 9:
	        		balance = balance+token;
	        		break;
	        	default:
	        		break;
	        	}
	        	//PR System.out.println(token);
	        }	
	        
	        if (payType.equalsIgnoreCase("PUR")){
	        	balance = balance.substring(17,balance.indexOf(". HELP "));
	        }else{
	        	balance = balance.substring(11,balance.indexOf(". HELP "));
	        }
			BigDecimal money = new BigDecimal(balance);
			trxn.setTrxn_balance(money);
        }
        catch (Exception e){
			n.setNotification_type("ERROR");
			n.setNotification_desc("Critical issue: "+e.getMessage());
			n.setNotification_action("INVESTIGATE");
        }
		
		return trxn;
	}
	
	private String getCategory(String thirdParty, Bgt_notifications n){
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
	
	/*public static void main(String args[]){
		Orig_SMS o_sms = new Orig_SMS();
		o_sms.setMessage("Absa: CCRD2011, Pur, 23/07/17 PURCHASE, C#BP FOURWAYS, R512.54, Total Avail Bal R13,009.11. Help 0860553553; RAMAWPR001");
		ProcessSms p = new ProcessSms(o_sms);
		p.process();
		
	}*/

}
