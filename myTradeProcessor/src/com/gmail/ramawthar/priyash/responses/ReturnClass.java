package com.gmail.ramawthar.priyash.responses;

public class ReturnClass {
	
	private String status;
	private String description;
	
	public ReturnClass(String status, String description) {
		super();
		this.status = status;
		this.description = description;
	}

	public ReturnClass(String status) {
		super();
		this.status = status;
	}

	public ReturnClass() {
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
