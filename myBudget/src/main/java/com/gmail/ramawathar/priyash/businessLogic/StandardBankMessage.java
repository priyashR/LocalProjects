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

public class StandardBankMessage implements SMSProcessor{
	public ArrayList<Bgt_trxns> processMessage(Orig_SMS processSMS, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		
		System.out.println("Process std bank");
		LookupData lookup= new LookupData();
		Bgt_trxns trxn = new Bgt_trxns();
		ArrayList<Bgt_trxns> trxnArr = new ArrayList<Bgt_trxns>();
		String sms = processSMS.getMessage().toUpperCase().replace("STANDARD BANK:", "STANDARD BANK");
		
		if (sms.contains("PURCHASED")){
			sms = sms.replace("ACL BAL", "AVL BAL");
		}
		
		//trxn email
		String email = processSMS.getUser_email();

		System.out.println("Process string: "+sms);
		
		StringTokenizer defaultTokenizer = new StringTokenizer(sms," ");
        int pos = 0;        
        String token;
        boolean debit = false;
    	boolean paid_flag = false;
    	int end_set = 0;
    	String third_party = "";
    	String end_string = "ACL";
    	String prev_token = "";
        try{
        	while ((defaultTokenizer.hasMoreTokens()) && 
        		   (!(n.getNotification_type().equalsIgnoreCase("ERROR"))) &&
        		   (!debit))
        	{
	        	token = defaultTokenizer.nextToken();
	        	pos++;
	        	boolean withdrawn_flag = false;
	        	switch (pos){
	        	case 3:
	        		System.out.println("3: "+token);
	        		if (token.equalsIgnoreCase("DEBIT"))
	        			debit = true;
	        		else{
	        			System.out.println("Not debit - so process");
	        			trxn.setUser_email(email);	
		        		String amount = token;

		        		System.out.println("amount: "+amount);
		        		amount = amount.substring(1);
						BigDecimal money = new BigDecimal(amount);
						trxn.setTrxn_amount(money.abs());
		        		System.out.println("amount: "+money.abs());       		
	        		}
	        		break;
	        	case 4:
	        		System.out.println("4: "+token);
	        		switch (token.toUpperCase()){
	        		case "PURCHASED":
	        			trxn.setTrxn_type("O");
	        			end_string = "AVL";
	        			break;
	        		case "WITHDRAWN":
	        			trxn.setTrxn_type("O");
	        			withdrawn_flag = true;
	        			break;
	        		case "PAID":
	        			paid_flag = true;
	        			break;
		        	default:
						n.setNotification_type("ERROR");
						n.setNotification_desc("Unknown transaction type - cannot proceed");
						n.setNotification_action("INVESTIGATE");
		        	break;
	        			
	        		}
	        		
	        		break;
	        	case 5:
	        		System.out.println("5: "+token);
	        		if (paid_flag){
	        			if (token.equalsIgnoreCase("TO")){
	        				trxn.setTrxn_type("I");
	        				System.out.println("setTrxn_type: "+trxn.getTrxn_type());
	        			}else if (token.equalsIgnoreCase("FROM")){
	        				trxn.setTrxn_type("O");
	        				System.out.println("setTrxn_type: "+trxn.getTrxn_type());
	        			}else {
	        				n.setNotification_type("ERROR");
							n.setNotification_desc("Unknown transaction type - cannot proceed");
							n.setNotification_action("INVESTIGATE");
	        			}
	        		}
	        		break;
	        	case 7:
	        		System.out.println("7: "+token);
	        		trxn.setUser_account(token.substring(0, 4));
    				System.out.println("setUser_account: "+trxn.getUser_account());
	        		break;
	        	default:
	        		break;
	        	}
	        	
	        	if((pos>8) && (end_set == 0)){
	        		third_party = third_party + " " +token;
	        		third_party = third_party.trim();
    				System.out.println("third_party: "+third_party);
	        		if((prev_token.equalsIgnoreCase(end_string))&(token.equalsIgnoreCase("BAL"))){
	        			end_set++;
	        			third_party = third_party.substring(0, third_party.indexOf(" "+end_string+" BAL"));
	        			if (withdrawn_flag){
		        			trxn.setUserThirdParty("ATM WITDRAWAL");
		        			//to put back -> 
		        			trxn.setCategory(lookup.category(trxn.getUserThirdParty(),n, bgt_user_third_partyRepository));       				
	        			}else{
		        			trxn.setUserThirdParty(third_party);
		        			//to put back -> 
		        			trxn.setCategory(lookup.category(trxn.getUserThirdParty(),n, bgt_user_third_partyRepository));  	        				
	        			}
	        		}else{
	        			prev_token = token;
	        		}
    				System.out.println("setUser_third_party: "+trxn.getUserThirdParty());
	        	}else if((pos>8) && (end_set == 1)){
    				System.out.println("balance : "+token);
    				end_set++;
	        		String balance = token;
	        		
		        	balance = balance.substring(1);
					BigDecimal moneyBalance = new BigDecimal(balance);
					trxn.setTrxn_balance(moneyBalance);
	        		System.out.println("balance: "+moneyBalance);	
		        }else if((pos>8) && (end_set == 2)){
	        		System.out.println("setting the date for: "+token);
	        		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	        		Date tranDate = new Date(1);
					try {
						tranDate = formatter.parse(token.substring(0,10));
					} catch (ParseException e) {
						n.setNotification_type("ERROR");
						n.setNotification_desc("Problem with the transaction date - cannot proceed");
						n.setNotification_action("INVESTIGATE");
						e.printStackTrace();
					} 
	        		trxn.setTrxn_date(tranDate);
	        		System.out.println("date is: "+trxn.getTrxn_date());
	        		end_set++;
	        		
	        }
        		
        		
        	}
        	
        	if (!debit){
                trxnArr.add(trxn);
        	}
        	else{
        		String innerToken;
        		System.out.println("Is debit - so process differently");
        		String payments = sms.substring(26, sms.indexOf("FROM ACC")).trim();
        		String other_fields = sms.substring(sms.indexOf("FROM ACC")).trim();
        		System.out.println("Process string: "+sms);
        		System.out.println("Payments string: "+payments);
        		System.out.println("other_fields string: "+other_fields);
        		
        		//get account
        		String account_number = other_fields.substring(9, 13);
        		System.out.println("account_number: "+account_number);
        		
        		//get date
        		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
        		Date tranDate = new Date(1);
				try {
					tranDate = formatter.parse(other_fields.substring(15,25));
				} catch (ParseException e) {
					n.setNotification_type("ERROR");
					n.setNotification_desc("Problem with the transaction date - cannot proceed");
					n.setNotification_action("INVESTIGATE");
					e.printStackTrace();
				} 

        		System.out.println("date is: "+tranDate);
        		
        		//set balance to zero later on
        		
        		//build the transactions
        		StringTokenizer paymentsTokenizer = new StringTokenizer(payments,";");
                while ((paymentsTokenizer.hasMoreTokens()) && 
             		   (!(n.getNotification_type().equalsIgnoreCase("ERROR"))))
             	{
                	trxn = new Bgt_trxns();
                	innerToken = paymentsTokenizer.nextToken().trim();
                	
                	
                	//get amount
                	StringTokenizer tpTokenizer = new StringTokenizer(innerToken," ");
	        		String amount = tpTokenizer.nextToken();

	        		//System.out.println("amount: "+amount);
	        		amount = amount.substring(1);
					BigDecimal money = new BigDecimal(amount);
					trxn.setTrxn_amount(money.abs());
	        		System.out.println("trxn amount: "+trxn.getTrxn_amount()); 
	        		
	        		int amountLen = amount.length();
	        		
	        		//get the third party and category
        			trxn.setUserThirdParty(innerToken.substring(amountLen+1).trim());
        			//to put back -> 
        			trxn.setCategory(lookup.category(trxn.getUserThirdParty(),n, bgt_user_third_partyRepository)); 
        			
	        		System.out.println("setUser_third_party: "+trxn.getUserThirdParty()); 
	        		
	        		//set email
	        		trxn.setUser_email(email);	
	        		System.out.println("setUser_email: "+trxn.getUser_email()); 
	        		
	        		//set account
	        		trxn.setUser_account(account_number);
	        		System.out.println("setUser_account: "+trxn.getUser_account()); 
        			
        			//set tran type
	        		trxn.setTrxn_type("O");
	        		System.out.println("setTrxn_type: "+trxn.getTrxn_type()); 
                	
	        		//set balance to null
	        		trxn.setTrxn_balance(null);
	        		System.out.println("setTrxn_balance: "+trxn.getTrxn_balance()); 
	        		
	        		//set tran date
	        		trxn.setTrxn_date(tranDate);
	        		System.out.println("date is: "+trxn.getTrxn_date());
	        		

	                trxnArr.add(trxn);
             	}
                
                
        	}
        	
        	
        }
        catch (Exception e){
        	System.out.println("error");
			n.setNotification_type("ERROR");
			String error = e.toString();
			if (e.toString().length()>580)
				error = e.toString().substring(0,580);
			n.setNotification_desc("Critical issue: "+error);//+e.getMessage().substring(0,580));
			n.setNotification_action("INVESTIGATE");
			//System.out.println(e.toString());
        }
        
		return trxnArr;
	}

}
