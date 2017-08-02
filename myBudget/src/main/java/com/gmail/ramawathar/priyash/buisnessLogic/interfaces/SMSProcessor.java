package com.gmail.ramawathar.priyash.buisnessLogic.interfaces;

import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public interface SMSProcessor {
	public Bgt_trxns processMessage(Orig_SMS processSMS, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository);
}
