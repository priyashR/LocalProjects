package com.gmail.ramawathar.priyash.businessLogic;

public class CategoryParamPOJO {

	private String category;
	private String transNumber;
	private String reference;
	private String cat_parent;
	private String cat_desc;
	private String status;
	private String statusDesc;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getTransNumber() {
		return transNumber;
	}
	public void setTransNumber(String transNumber) {
		this.transNumber = transNumber;
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
	

}
