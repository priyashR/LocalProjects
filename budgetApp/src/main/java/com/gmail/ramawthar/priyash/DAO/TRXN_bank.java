package com.gmail.ramawthar.priyash.DAO;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the TRXN_bank database table.
 * 
 */
@Entity
@NamedQuery(name="TRXN_bank.findAll", query="SELECT t FROM TRXN_bank t")
public class TRXN_bank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@SequenceGenerator(name="TRXN_BANK_BANK_ID_GENERATOR" )
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRXN_BANK_BANK_ID_GENERATOR")
	private int bank_id;

	private String bank_name;

	public TRXN_bank() {
	}

	public int getBank_id() {
		return this.bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}

	public String getBank_name() {
		return this.bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

}