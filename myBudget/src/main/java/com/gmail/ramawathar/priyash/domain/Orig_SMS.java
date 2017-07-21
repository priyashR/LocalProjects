package com.gmail.ramawathar.priyash.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Table(name="bgt_orig_sms")  
@Entity
public class Orig_SMS {

	@Id
    @GeneratedValue
    @Column(name="SMS_ID")
	private Long sms_id;
    
    @Column(name="MESSAGE")
    @NotEmpty
	private String message;

	
    public Long getSms_id() {
		return sms_id;
	}

	public void setSms_id(Long sms_id) {
		this.sms_id = sms_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
