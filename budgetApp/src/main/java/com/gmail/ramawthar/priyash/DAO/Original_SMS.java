package com.gmail.ramawthar.priyash.DAO;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TRXN_original_SMS database table.
 * 
 */
@Entity
@Table(name="TRXN_original_SMS")
@NamedQuery(name="Original_SMS.findAll", query="SELECT o FROM Original_SMS o")
public class Original_SMS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int TRXN_original_SMS_id;

	private String original_SMS;

	private int TRXN_id;

	public Original_SMS() {
	}

	public int getTRXN_original_SMS_id() {
		return this.TRXN_original_SMS_id;
	}

	public void setTRXN_original_SMS_id(int TRXN_original_SMS_id) {
		this.TRXN_original_SMS_id = TRXN_original_SMS_id;
	}

	public String getOriginal_SMS() {
		return this.original_SMS;
	}

	public void setOriginal_SMS(String original_SMS) {
		this.original_SMS = original_SMS;
	}

	public int getTRXN_id() {
		return this.TRXN_id;
	}

	public void setTRXN_id(int TRXN_id) {
		this.TRXN_id = TRXN_id;
	}

}