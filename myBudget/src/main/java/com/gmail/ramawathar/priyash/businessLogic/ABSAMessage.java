package com.gmail.ramawathar.priyash.businessLogic;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.SMSProcessor;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class ABSAMessage implements SMSProcessor{
	public ArrayList<Bgt_trxns> processMessage(Orig_SMS processSMS, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository){

		LookupData lookup= new LookupData();
		Bgt_trxns trxn = new Bgt_trxns();
		ArrayList<Bgt_trxns> trxnArr = new ArrayList<Bgt_trxns>();
		String sms = processSMS.getMessage().toUpperCase().replace("ABSA:", "ABSA,");
		//remove the number commas here
		String smsCleaned  = "";
		boolean withdrawal = false;
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

    	//Fix hardcodes
    	smsCleaned = smsCleaned.replace("SETTLEMENT/C - NAEDO TRACK INTL,", "SETTLEMENT/C - NAEDO TRACK INTL, HOME LOAN,");
    	smsCleaned = smsCleaned.replace("CASH ADVANCE ABSA ATM,", "CASH ADVANCE ABSA ATM");
    	smsCleaned = smsCleaned.replace("ABSA VF 00087711620", " CAR");
    	
		//remove the spaces after tokens
		smsCleaned = smsCleaned.replace(", ", ",");
		String payType= "";
    	String endSearch = ". HELP ";
		
    	
		
		System.out.println("smsCleaned: "+smsCleaned);
		System.out.println("sms       : "+sms);
		
		StringTokenizer defaultTokenizer = new StringTokenizer(smsCleaned,",");
        int pos = 0;
        
        trxn.setUser_email(processSMS.getUser_email());

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
	   		        		
	   		        		trxn.setCategory(lookup.category(trxn.getUser_third_party(),n, bgt_user_third_partyRepository));
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
	   		        		trxn.setTrxn_balance(moneyBalance);
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
		        		case "SCH T":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "TRANSFER":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "PMNT":
		        			trxn.setTrxn_type("O");
		        			break;
		        		case "WTHDR":
		        			trxn.setTrxn_type("O");
		        			withdrawal = true;
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
		        		
		        		if (withdrawal){
		        			payType = "WTD";
		        		}else{
		        			payType = token.substring(9, 12).toUpperCase();
		        		}

		        		System.out.println("payType: "+payType);
		        		System.out.println("tran date: "+tranDate);
		        		break;
		        	case 5:
		        		System.out.println("5: "+token);
		        		if (withdrawal){
		        			trxn.setUser_third_party("ATM WITDRAWAL");
			        		//trxn.setCategory("UNCTEGORISED");
		        			trxn.setCategory(lookup.category(trxn.getUser_third_party(),n, bgt_user_third_partyRepository));
		        			//set amount as well
			        		System.out.println("setting the amount as well");
			        		BigDecimal money = new BigDecimal(token.substring(1).replaceAll(",", ""));
			        		trxn.setTrxn_amount(money.abs());
			        		System.out.println("amount: "+money.abs());
		        			
		        		}else{
			        		trxn.setUser_third_party(token.toUpperCase());
			                //lookup category here
			                trxn.setCategory(lookup.category(token.toUpperCase(),n, bgt_user_third_partyRepository));
			        		//trxn.setCategory("UNCTEGORISED");
		                }
		        		break;
		        	case 6:
		        		
		        		System.out.println(6+": " + token);
		        		if (withdrawal){
			        		String balance = token.replace("TOTAL AVAIL BAL", "AVAILABLE");
				        	System.out.println("WTD:"+endSearch);
				        	System.out.println("balance string:"+balance);	
				        	//clean out credit card ATM withdrawal
				        	balance = balance.substring(11,balance.indexOf(endSearch));
							BigDecimal moneyBalance = new BigDecimal(balance);
							trxn.setTrxn_balance(moneyBalance);
			        		System.out.println("amount: "+moneyBalance);		        			
		        		}else{
			        		BigDecimal money = new BigDecimal(token.substring(1).replaceAll(",", ""));
			        		trxn.setTrxn_amount(money.abs());
			        		System.out.println("amount: "+money.abs());
		        		}
		        		break;
		        	case 7:
		        		System.out.println(7);
		        		if (withdrawal){
		        			
		        		}else{
			        		String balance = token;
					        if ((payType.equalsIgnoreCase("PUR"))||(payType.equalsIgnoreCase("AUT"))){
					        	System.out.println("PUR or AUTH: "+balance);
					        	balance = balance.substring(17,balance.indexOf(endSearch));
								BigDecimal moneyBalance = new BigDecimal(balance);
								trxn.setTrxn_balance(moneyBalance);
				        		System.out.println("amount: "+moneyBalance.abs());
					        }else if(payType.equalsIgnoreCase("SET")){
					        	System.out.println("SET:"+endSearch);
					        	System.out.println("balance string:"+balance);		        	
					        	balance = balance.substring(11,balance.indexOf(endSearch));
								BigDecimal moneyBalance = new BigDecimal(balance);
								trxn.setTrxn_balance(moneyBalance);
				        		System.out.println("amount: "+moneyBalance.abs());
					        }
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
        	System.out.println("error: "+e.toString());
			n.setNotification_type("ERROR");
			String error = e.toString();
			if (e.toString().length()>580)
				error = e.toString().substring(0,580);
			n.setNotification_desc("Critical issue: "+error);//+e.getMessage().substring(0,580));
			n.setNotification_action("INVESTIGATE");
			//System.out.println(e.toString());
        }
        trxnArr.add(trxn);
		return trxnArr;
		
		
		
	}
	
}
