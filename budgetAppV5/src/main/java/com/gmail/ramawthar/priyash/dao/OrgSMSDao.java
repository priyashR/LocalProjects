package com.gmail.ramawthar.priyash.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gmail.ramawthar.priyash.model.TRXN_original_SMS;
import org.springframework.stereotype.Repository;

@Repository
public class OrgSMSDao {
	@PersistenceContext
	private EntityManager em;

	public void persist(TRXN_original_SMS sms) {
		em.persist(sms);
	}

	public List<TRXN_original_SMS> findAll() {
		return em.createQuery("SELECT Original_SMS FROM TRXN_original_SMS").getResultList();
	}
}
