package com.gmail.ramawathar.priyash.businessLogic;

import java.util.ArrayList;


import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.SMSProcessor;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class ProcessSms {
	
	private Bgt_user_third_partyRepository bgt_user_third_partyRepository;
	private Orig_SMS o_sms;
	//private LookupData lookup= new LookupData();
	public ProcessSms(Orig_SMS o_sms){
		this.o_sms = o_sms;
	}
	
	public ProcessSms(Orig_SMS o_sms, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		this.o_sms = o_sms;
		this.bgt_user_third_partyRepository = bgt_user_third_partyRepository;
	}
	
	public ArrayList<Bgt_trxns> process(Bgt_notifications n){
		
		ArrayList<Bgt_trxns> trxn = new ArrayList<Bgt_trxns>();
		String sms = o_sms.getMessage();
		SMSProcessor bank = null;
		
		if (sms.toUpperCase().startsWith("ABSA")) {
			bank = new ABSAMessage();
		}else if (sms.toUpperCase().startsWith("STANDARD")) {
			bank = new StandardBankMessage();
		}

		trxn = bank.processMessage(this.o_sms, n, this.bgt_user_third_partyRepository);
		return trxn;
	}
		
	/*public static void main(String args[]){
		Orig_SMS o_sms = new Orig_SMS();
		Bgt_notifications n = new Bgt_notifications();
		o_sms.setMessage("Absa: CCRD2011, Wthdr, 09/08/17 CASH ADVANCE ABSA ATM, ABSA ATM MERCHANTS PTY LTPRETO, R250.00, Total Avail Bal R15,855.00. Help 0860553553; RAMAWPR001");
		ProcessSms p = new ProcessSms(o_sms);
		n.setNotification_type("INFO");
		p.process(n);
		
	}*/

}
