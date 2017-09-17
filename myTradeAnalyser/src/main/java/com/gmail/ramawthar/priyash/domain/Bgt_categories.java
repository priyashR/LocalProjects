package com.gmail.ramawthar.priyash.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Table(name="bgt_categories")  
@Entity
@IdClass(Bgt_categories.class)
public class Bgt_categories implements Serializable{
	
	@Id
	@Column(name="USER_EMAIL")
	private String user_email;
	
	@Id
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="CAT_PARENT")
	private String cat_parent;
	
	@Column(name="CAT_DESC")
	private String cat_desc;
	
	@Column(name="IS_DEFAULT_CAT")
	private String is_default_cat;

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

	public String getCat_parent() {
		return cat_parent;
	}

	public void setCat_parent(String cat_parent) {
		this.cat_parent = cat_parent;
	}

	public String getCat_desc() {
		return cat_desc;
	}

	public void setCat_desc(String cat_desc) {
		this.cat_desc = cat_desc;
	}

	public String getIs_default_cat() {
		return is_default_cat;
	}

	public void setIs_default_cat(String is_default_cat) {
		this.is_default_cat = is_default_cat;
	}
	
	
	
}
