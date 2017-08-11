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
	/*	
	public static void main(String args[]){
		Orig_SMS o_sms = new Orig_SMS();
		Bgt_notifications n = new Bgt_notifications();
		o_sms.setMessage("Standard Bank: Debit Order R400.00 LIBERTY050 0011858961    52240; R500.00 LIBERTY050 0065385366    52240;  from Acc.3665. 2017-07-01 Query?0860123107");
		ProcessSms p = new ProcessSms(o_sms);
		n.setNotification_type("INFO");
		p.process(n);
		
	}*/

}
