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
		//remove the number commas here
		String smsCleaned  = "";
		boolean ignore = false;
		for (int i = 0; i < sms.length(); i++){
			//System.out.println("i: "+i);
		    char c = sms.charAt(i);    
		    if ((i-1>=0)&&(i+1!=sms.length())){
				//System.out.println("checking bounds");
		    	if (c==','){
					//System.out.println("checking for comma");
		    		if ((Character.isDigit(sms.charAt(i-1)))&(Character.isDigit(sms.charAt(i+1)))){

						//System.out.println("found number with comma ");
						//System.out.println("sms.charAt(i-1) "+sms.charAt(i-1));
						//System.out.println("sms.charAt(i+1) "+sms.charAt(i+1));
		    			ignore = true;
		    		}
		    	}
		    }
		    if (!(ignore))
		    	smsCleaned = smsCleaned+c;
		    ignore = false;
		}
		
		System.out.println("smsCleaned: "+smsCleaned);
		System.out.println("sms       : "+sms);
		
		//remove the spaces after tokens
		smsCleaned = smsCleaned.replace(", ", ",");
		String payType= "";
    	String endSearch = ". HELP ";
		
		StringTokenizer defaultTokenizer = new StringTokenizer(smsCleaned,",");
        int pos = 0;
        
        trxn.setUser_email(o_sms.getUser_email());

        String token;
        try{
    	   
    	   if (defaultTokenizer.countTokens() < 6){
    		   
    		   if(smsCleaned.toUpperCase().contains("MAY INCL. UNCLEARED AMOUNT")){
	        		n.setNotification_type("ERROR");
					n.setNotification_desc("Balance updates are not currently handled by the application");
					n.setNotification_action("INVESTIGATE");
    		   }else if((smsCleaned.toUpperCase().contains("RESERVED"))&&((smsCleaned.toUpperCase().contains("FOR A PURCHASE")))){
    			   while ((defaultTokenizer.hasMoreTokens()) && (!(n.getNotification_type().equalsIgnoreCase("ERROR"))))
	   	        	{
	   		        	token = defaultTokenizer.nextToken();
	   		        	pos++;
	   		        	switch (pos){
	   		        	case 2:
	   		        		System.out.println(2);
	   		        		//verify user account and if not found set the notification type to ERROR
	   		        		if (token.substring(0,3).equalsIgnoreCase("BAL")){
	   	
	   			        		n.setNotification_type("ERROR");
	   							n.setNotification_desc("Balance updates are not currently handled by the application");
	   							n.setNotification_action("INVESTIGATE");
	   			        	}else{
	   		        		trxn.setUser_account(token);
	   		        		}
	   		        		System.out.println("account: "+token);
	   		        		break;
	   		        	case 3:
	   		        		System.out.println(3);

	   		        		trxn.setTrxn_type("O");
	   		        		System.out.println("tran type: "+trxn.getTrxn_type());
	   		        		DateFormat formatter = new SimpleDateFormat("dd/MM/yy"); 
	   		        		Date tranDate = new Date(1);
	   						try {
	   							tranDate = formatter.parse(token.substring(0,8));
	   						} catch (ParseException e) {
	   							n.setNotification_type("ERROR");
	   							n.setNotification_desc("Problem with the transaction date - cannot proceed");
	   							n.setNotification_action("INVESTIGATE");
	   							e.printStackTrace();
	   						} 
	   		        		trxn.setTrxn_date(tranDate);
	   		        		System.out.println("tranDate: "+tranDate);
	   		        		payType = "PUR";
	   		        		
	   		        		//get third party
	   		        		trxn.setUser_third_party(token.toUpperCase().substring(9, token.toUpperCase().indexOf(" RESERVED")));
	   		        		
	   		        		trxn.setCategory(getCategory(trxn.getUser_third_party(),n));
	   		        	    //trxn.setCategory("UNCTEGORISED");	   		        		
	   		        		System.out.println("setUser_third_party: "+trxn.getUser_third_party());
	   		        		

	   		        		//get amount
	   		        		System.out.println();
	   		        		BigDecimal money = new BigDecimal(token.toUpperCase().substring(token.toUpperCase().indexOf(" RESERVED")+11, token.toUpperCase().indexOf(" FOR A PURCHASE")));
	   		        		trxn.setTrxn_amount(money.abs());
	   		        		System.out.println("amount: "+money.abs());

	   		        		//get balance
	   		        		System.out.println((token.toUpperCase().substring(token.toUpperCase().indexOf("YOUR AVAILABLE BALANCE")+25, token.toUpperCase().indexOf(" HELP "))));//, token.toUpperCase().indexOf(" Help "))));
	   		        		BigDecimal moneyBalance = new BigDecimal((token.toUpperCase().substring(token.toUpperCase().indexOf("YOUR AVAILABLE BALANCE")+25, token.toUpperCase().indexOf(" HELP "))));
	   		        		trxn.setTrxn_balance(moneyBalance.abs());
	   		        		System.out.println("amount: "+moneyBalance.abs());	   	     		
	   		        		
	   		        		break;
	   		        	default:
	   		        		break;
	   		        	}
	   		        	//System.out.println(token);
	   		        }	
    		   }else{
	        		n.setNotification_type("ERROR");
					n.setNotification_desc("Uncatered message pattern");
					n.setNotification_action("INVESTIGATE");    			   
    		   }
    		   
    	   }else{	    	   
	    		while ((defaultTokenizer.hasMoreTokens()) && (!(n.getNotification_type().equalsIgnoreCase("ERROR"))))
	        	{
		        	token = defaultTokenizer.nextToken();
		        	pos++;
		        	switch (pos){
		        	case 2:
		        		System.out.println(2);
		        		//verify user account and if not found set the notification type to ERROR
		        		if (token.substring(0,3).equalsIgnoreCase("BAL")){
	
			        		n.setNotification_type("ERROR");
							n.setNotification_desc("Balance updates are not currently handled by the application");
							n.setNotification_action("INVESTIGATE");
			        	}else{
		        		trxn.setUser_account(token);
		        		}
		        		System.out.println("account: "+token);
		        		break;
		        	case 3:
		        		System.out.println(3);
		        		switch(token.toUpperCase()){
		        		case "PUR":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "TRANSFER":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "PMNT":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "DEP":
		        			endSearch = " HELP ";
		        			trxn.setTrxn_type("I");
		        			break;
			        	default:
							n.setNotification_type("ERROR");
							n.setNotification_desc("Unknown transaction type - cannot proceed");
							n.setNotification_action("INVESTIGATE");
			        		break;
		        		}
		        		System.out.println("tran type: "+trxn.getTrxn_type());
		        		break;
		        	case 4:
		        		System.out.println(4);
		        		DateFormat formatter = new SimpleDateFormat("dd/MM/yy"); 
		        		Date tranDate = new Date(1);
						try {
							tranDate = formatter.parse(token.substring(0,8));
						} catch (ParseException e) {
							n.setNotification_type("ERROR");
							n.setNotification_desc("Problem with the transaction date - cannot proceed");
							n.setNotification_action("INVESTIGATE");
							e.printStackTrace();
						} 
		        		trxn.setTrxn_date(tranDate);
		        		
		        		payType = token.substring(9, 12).toUpperCase();

		        		System.out.println("payType: "+payType);
		        		System.out.println("tran date: "+tranDate);
		        		break;
		        	case 5:
		        		System.out.println(5);
		        		trxn.setUser_third_party(token.toUpperCase());
		                //lookup category here
		                trxn.setCategory(getCategory(token.toUpperCase(),n));
		        		//trxn.setCategory("UNCTEGORISED");
		        		break;
		        	case 6:
		        		System.out.println(6);
		        		BigDecimal money = new BigDecimal(token.substring(1).replaceAll(",", ""));
		        		trxn.setTrxn_amount(money.abs());
		        		System.out.println("amount: "+money.abs());
		        		break;
		        	case 7:
		        		System.out.println(7);
		        		String balance = token;
				        if ((payType.equalsIgnoreCase("PUR"))||(payType.equalsIgnoreCase("AUT"))){
				        	System.out.println("PUR or AUTH: "+balance);
				        	balance = balance.substring(17,balance.indexOf(endSearch));
							BigDecimal moneyBalance = new BigDecimal(balance);
							trxn.setTrxn_balance(moneyBalance.abs());
			        		System.out.println("amount: "+moneyBalance.abs());
				        }else if(payType.equalsIgnoreCase("SET")){
				        	System.out.println("SET:"+endSearch);
				        	System.out.println("balance string:"+balance);		        	
				        	balance = balance.substring(11,balance.indexOf(endSearch));
							BigDecimal moneyBalance = new BigDecimal(balance);
							trxn.setTrxn_balance(moneyBalance.abs());
			        		System.out.println("amount: "+moneyBalance.abs());
				        }
		        		break;
		        	default:
		        		break;
		        	}
		        	//System.out.println(token);
		        }	
    	   }
    	}
        catch (Exception e){
        	System.out.println("error");
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
		Bgt_notifications n = new Bgt_notifications();
		o_sms.setMessage("Absa: CHEQ4993, 29/07/17 Spar Pineslopes Spar Gauteng reserved R16,992.32 for a purchase. Your available balance: R9,964.17 Help 0860553553; RAMAWPR001");
		ProcessSms p = new ProcessSms(o_sms);
		n.setNotification_type("INFO");
		p.process(n);
		
	}*/

}
