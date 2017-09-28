package com.gmail.ramawthar.priyash.responses;

import com.gmail.ramawthar.priyash.analysis.ResultantData;

public class ReturnClass {
	
	private String status;
	private String description;
	private ResultantData rd;
	
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

	public ResultantData getRd() {
		return rd;
	}

	public void setRd(ResultantData rd) {
		this.rd = rd;
	}
	
	

}
