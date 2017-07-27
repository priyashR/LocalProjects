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
    
    @Column(name="USER_NUMBER")
    private String user_number;
    
    @Column(name="SENDER_NUMBER")
    private String sender_number;
    
    @Column(name="USER_EMAIL")
    private String user_email;
	
    public String getUser_number() {
		return user_number;
	}

	public void setUser_number(String user_number) {
		this.user_number = user_number;
	}

	public String getSender_number() {
		return sender_number;
	}

	public void setSender_number(String sender_number) {
		this.sender_number = sender_number;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

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
