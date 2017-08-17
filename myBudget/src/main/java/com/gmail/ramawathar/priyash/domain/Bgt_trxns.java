package com.gmail.ramawathar.priyash.domain;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Table(name="bgt_trxns")  
@Entity
public class Bgt_trxns {
	
	@Id
    @GeneratedValue
    @Column(name="TRXN_ID")	
	private Long trxnId;
	
    @Column(name="USER_EMAIL")
    @NotEmpty
	private String user_email;
    
    @Column(name="USER_ACCOUNT")
    @NotEmpty
	private String user_account;

    @Column(name="TRXN_AMOUNT")
    @NotNull
	private BigDecimal trxn_amount;
    
    @Column(name="TRXN_TYPE")
    @NotEmpty
	private String trxn_type;
    
    @Column(name="TRXN_DATE")
    @Temporal(TemporalType.DATE)
    @NotNull
	private Date trxn_date;
    
    @Column(name="CATEGORY")
    @NotEmpty
	private String category;
    
    @Column(name="USER_THIRD_PARTY")
    @NotEmpty
	private String userThirdParty;


    @Column(name="TRXN_BALANCE")
    private BigDecimal trxn_balance;

	public Long getTrxnId() {
		return trxnId;
	}

	public void setTrxnId(Long trxn_id) {
		this.trxnId = trxn_id;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_account() {
		return user_account;
	}

	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}

	public BigDecimal getTrxn_amount() {
		return trxn_amount;
	}

	public void setTrxn_amount(BigDecimal trxn_amount) {
		this.trxn_amount = trxn_amount;
	}

	public String getTrxn_type() {
		return trxn_type;
	}

	public void setTrxn_type(String trxn_type) {
		this.trxn_type = trxn_type;
	}

	public Date getTrxn_date() {
		return trxn_date;
	}

	public void setTrxn_date(Date trxn_date) {
		this.trxn_date = trxn_date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserThirdParty() {
		return userThirdParty;
	}

	public void setUserThirdParty(String userThirdParty) {
		this.userThirdParty = userThirdParty;
	}

	public BigDecimal getTrxn_balance() {
		return trxn_balance;
	}

	public void setTrxn_balance(BigDecimal trxn_balance) {
		this.trxn_balance = trxn_balance;
	}
    
    
    

}
