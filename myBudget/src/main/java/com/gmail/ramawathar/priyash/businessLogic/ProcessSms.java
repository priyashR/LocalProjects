package com.gmail.ramawathar.priyash.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;

public class ProcessSms {
	

	private Bgt_trxns trxn = new Bgt_trxns();
	private Orig_SMS o_sms;
	
	public ProcessSms(Orig_SMS o_sms){
		this.o_sms = o_sms;
	}
	
	public Bgt_trxns process(){
		
		String sms = o_sms.getMessage();
		
		if (sms.toUpperCase().startsWith("ABSA")) {
			sms = sms.toUpperCase().replace("ABSA:", "ABSA,");
			sms = sms.replace(", ", ",");
	        //StringTokenizer defaultTokenizer = new StringTokenizer(o_sms.getMessage(),",");
			StringTokenizer defaultTokenizer = new StringTokenizer(sms,",");
			String balance = "";
	        int pos = 0;
	        trxn.setUser_email(o_sms.getUser_email());
	        trxn.setCategory("Petrol");
	        String token;
	        while (defaultTokenizer.hasMoreTokens())
	        {
	        	token = defaultTokenizer.nextToken();
	        	pos++;
	        	switch (pos){
	        	case 1:
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
	        			trxn.setTrxn_type("?");
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
			//PR System.out.println("Balance: "+balance);
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
