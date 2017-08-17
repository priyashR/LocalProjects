package com.gmail.ramawathar.priyash.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;

public interface Bgt_notificationsRepository  extends CrudRepository<Bgt_notifications, Long> {

	List<Bgt_notifications> findByTrxnId(Long TrxnId);
}
