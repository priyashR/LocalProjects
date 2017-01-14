package com.gmail.ramawthar.priyash.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TRXN_original_SMS {
	
	private String Original_SMS;
	private String TRXN_user_id;
	private String timestamp;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer TRXN_original_SMS_id;
	 
	public TRXN_original_SMS() {}	
	
	public Integer getTRXN_original_SMS_id() {
		return TRXN_original_SMS_id;
	}

	public void setTRXN_original_SMS_id(Integer tRXN_original_SMS_id) {
		TRXN_original_SMS_id = tRXN_original_SMS_id;
	}

	public TRXN_original_SMS(String Original_SMS, String TRXN_user_id, String timestamp) {
		this.Original_SMS = Original_SMS;
		this.TRXN_user_id = TRXN_user_id;
		this.timestamp = timestamp;
	}
	
	
	public String getOriginal_SMS() {
		return Original_SMS;
	}
	public void setOriginal_SMS(String Original_SMS) {
		this.Original_SMS = Original_SMS;
	}
	public String getTRXN_user_id() {
		return TRXN_user_id;
	}
	public void setTRXN_user_id(String TRXN_user_id) {
		this.TRXN_user_id = TRXN_user_id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	

}
