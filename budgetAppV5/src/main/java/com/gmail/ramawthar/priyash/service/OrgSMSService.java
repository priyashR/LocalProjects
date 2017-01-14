package com.gmail.ramawthar.priyash.service;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.gmail.ramawthar.priyash.model.*;
import com.gmail.ramawthar.priyash.dao.*;
@Component
public class OrgSMSService {
	
	@Autowired
	private OrgSMSDao orgSMSDao;

	@Transactional
	public void add(TRXN_original_SMS sms) {
		orgSMSDao.persist(sms);
	}
	
	@Transactional
	public void addAll(Collection<TRXN_original_SMS> smss) {
		for (TRXN_original_SMS sms : smss) {
			orgSMSDao.persist(sms);
		}
	}

	@Transactional(readOnly = true)
	public List<TRXN_original_SMS> listAll() {
		return orgSMSDao.findAll();

	}
}
