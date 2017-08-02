package com.gmail.ramawathar.priyash.buisnessLogic.interfaces;

import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

public interface Lookup {
	public String category(String thirdParty, Bgt_notifications n, Bgt_user_third_partyRepository bgt_user_third_partyRepository);
}
