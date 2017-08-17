package com.gmail.ramawathar.priyash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Bgt_user_third_party;

public interface Bgt_trxnsRepository extends CrudRepository<Bgt_trxns, Long> {
	
	List<Bgt_trxns> findByTrxnId(Long TrxnId);
}
