package com.gmail.ramawathar.priyash.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;

public class ProcessSms {
	

	
	private Orig_SMS o_sms;
	
	public ProcessSms(Orig_SMS o_sms){
		this.o_sms = o_sms;
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
		
		StringTokenizer defaultTokenizer = new StringTokenizer(sms,",");
		String balance = "";
        int pos = 0;
        
        trxn.setUser_email(o_sms.getUser_email());
        //lookup category here
        trxn.setCategory("Petrol");
        
        String token;
        try{
	        while ((defaultTokenizer.hasMoreTokens()) && (!(n.getNotification_type().equalsIgnoreCase("ERROR"))))
	        {
	        	token = defaultTokenizer.nextToken();
	        	pos++;
	        	switch (pos){
	        	case 1:
	        		//verify user account and if not found set the notification type to ERROR
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
						n.setNotification_action("INVESTIGATION_REQUIRED");
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
						n.setNotification_action("INVESTIGATION_REQUIRED");
						e.printStackTrace();
					} 
	        		trxn.setTrxn_date(tranDate);
	        		break;
	        	case 5:
	        		trxn.setUser_third_party(token);
	        		break;
	        	case 6:
	        		BigDecimal money = new BigDecimal(token.substring(1).replaceAll(",", ""));
	        		trxn.setTrxn_amount(money);
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
	        
			balance = balance.substring(17,balance.indexOf(". HELP "));
			BigDecimal money = new BigDecimal(balance);
			trxn.setTrxn_balance(money);
        }
        catch (Exception e){
			n.setNotification_type("ERROR");
			n.setNotification_desc("Critical issue: "+e.getMessage());
			n.setNotification_action("INVESTIGATION_REQUIRED");
        }
		
		return trxn;
	}
	
	/*public static void main(String args[]){
		Orig_SMS o_sms = new Orig_SMS();
		o_sms.setMessage("Absa: CCRD2011, Pur, 23/07/17 PURCHASE, C#BP FOURWAYS, R512.54, Total Avail Bal R13,009.11. Help 0860553553; RAMAWPR001");
		ProcessSms p = new ProcessSms(o_sms);
		p.process();
		
	}*/

}
