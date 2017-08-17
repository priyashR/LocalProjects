package com.gmail.ramawathar.priyash.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="bgt_notification")  
@Entity
public class Bgt_notifications {
	
	@Id
    @GeneratedValue
    @Column(name="NOTIFICATION_ID")
	private Long notification_id;
	
	public Long getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(Long notification_id) {
		this.notification_id = notification_id;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getNotification_type() {
		return notification_type;
	}

	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}

	public String getNotification_desc() {
		return notification_desc;
	}

	public void setNotification_desc(String notification_desc) {
		this.notification_desc = notification_desc;
	}

	public String getNotification_orig_sms() {
		return notification_orig_sms;
	}

	public void setNotification_orig_sms(String notification_orig_sms) {
		this.notification_orig_sms = notification_orig_sms;
	}

	public String getNotification_action() {
		return notification_action;
	}

	public void setNotification_action(String notification_action) {
		this.notification_action = notification_action;
	}

	public String getNotification_status() {
		return notification_status;
	}

	public void setNotification_status(String notification_status) {
		this.notification_status = notification_status;
	}

	@Column(name="USER_EMAIL")
	private String user_email;

	@Column(name="NOTIFICATION_TYPE")
	private String notification_type;

	@Column(name="NOTIFICATION_DESC")
	private String notification_desc;

	@Column(name="NOTIFICATION_ORIG_SMS")
	private String notification_orig_sms;

	@Column(name="NOTIFICATION_ACTION")
	private String notification_action;

	@Column(name="NOTIFICATION_STATUS")
	private String notification_status;
	
	public Long getTrxnId() {
		return trxnId;
	}

	public void setTrxnId(Long trxnId) {
		this.trxnId = trxnId;
	}

	@Column(name="TRXN_ID")
	private Long trxnId;

}
