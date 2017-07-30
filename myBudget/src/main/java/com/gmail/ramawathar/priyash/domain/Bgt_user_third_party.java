package com.gmail.ramawathar.priyash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Table(name="bgt_user_third_party")  
@Entity
@IdClass(Bgt_user_third_party.class)
public class Bgt_user_third_party implements Serializable{
	
	@Id
	@Column(name="USER_EMAIL")
	private String user_email;
	@Id	
	@Column(name="USER_THIRD_PARTY")
	private String userThirdParty;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="USER_THIRD_PARTY_DESC")
	private String user_third_party_desc;

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUser_third_party() {
		return userThirdParty;
	}

	public void setUser_third_party(String user_third_party) {
		this.userThirdParty = user_third_party;
	}

	public String getUser_third_party_desc() {
		return user_third_party_desc;
	}

	public void setUser_third_party_desc(String user_third_party_desc) {
		this.user_third_party_desc = user_third_party_desc;
	}

}
