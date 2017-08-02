package com.gmail.ramawathar.priyash.businessLogic;

import java.math.BigDecimal;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.inject.Inject;

import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.SMSProcessor;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;
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
	
	public Bgt_trxns process(Bgt_notifications n){
		
		Bgt_trxns trxn = new Bgt_trxns();
		String sms = o_sms.getMessage();
		SMSProcessor bank = null;
		
		if (sms.toUpperCase().startsWith("ABSA")) {
			bank = new ABSAMessage();
		}

		trxn = bank.processMessage(this.o_sms, n, this.bgt_user_third_partyRepository);
		return trxn;
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
