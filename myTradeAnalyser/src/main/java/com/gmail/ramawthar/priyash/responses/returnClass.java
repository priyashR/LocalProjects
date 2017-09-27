package com.gmail.ramawthar.priyash.responses;

public class returnClass {
	
	private String status;
	private String description;
	
	public returnClass(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}

	public returnClass(String status) {
		super();
		this.status = status;
	}

	public returnClass() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
