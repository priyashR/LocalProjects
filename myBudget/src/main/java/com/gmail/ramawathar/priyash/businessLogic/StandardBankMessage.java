package com.gmail.ramawathar.priyash.businessLogic;

import java.util.ArrayList;

import com.gmail.ramawathar.priyash.buisnessLogic.interfaces.SMSProcessor;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public class StandardBankMessage implements SMSProcessor{
	public ArrayList<Bgt_trxns> processMessage(Orig_SMS processSMS, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository){
		LookupData lookup= new LookupData();
		Bgt_trxns trxn = new Bgt_trxns();
		ArrayList<Bgt_trxns> trxnArr = new ArrayList<Bgt_trxns>();
		String sms = processSMS.getMessage().toUpperCase().replace("Standard Bank:", "Standard Bank");
		
		

        trxnArr.add(trxn);
		return trxnArr;
	}

}
