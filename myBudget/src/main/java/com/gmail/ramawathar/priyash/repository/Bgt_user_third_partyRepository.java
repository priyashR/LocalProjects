package com.gmail.ramawathar.priyash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;

public interface Bgt_user_third_partyRepository extends CrudRepository<Bgt_user_third_party, Long>  {
	
	List<Bgt_user_third_party> findByUserThirdParty(String userThirdParty);
}
