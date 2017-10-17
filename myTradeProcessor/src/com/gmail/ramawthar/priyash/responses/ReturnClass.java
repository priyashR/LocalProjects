package com.gmail.ramawthar.priyash.responses;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReturnClass {
	
	private String status;
	private String description;
	private List<String> log = new ArrayList<String>();
	
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
	
	public void addLog(String logMessage){
		//log.add(logMessage);
		//need to fix
	}
	
	public void writeLog(String path){
		
		try {
			
			Path myPath = Paths.get(path);
			Files.write(myPath, log, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void displayLog(){
		
		for (String temp : log) {
			System.out.println(temp);
		}
	}

}
