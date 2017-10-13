package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareData {
	
	String inputPath;
	String outputPath;
	ReturnClass rc = new ReturnClass("Success");
	
	public PrepareData(String inputPath, String outputPath) {
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}

	public String getInputPath() {
		return inputPath;
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public ReturnClass processNewData(){
		
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	            System.out.println(fileEntry.getName());
	            System.out.println(getDate(fileEntry,rc));
	            processFile(fileEntry);
	            
	    }
		
		return rc;
		
	}
	
	private String getDate(File fileEntry, ReturnClass rc){
		
		
		String year = fileEntry.getName().substring(13,15);
		String month = fileEntry.getName().substring(17,19);
		String day = fileEntry.getName().substring(19,21);
		month = new DateFormatSymbols().getShortMonths()[Integer.parseInt(month)-1];
		rc.setStatus("Success");
		return day+"-"+month+"-"+year;
		
	}
	
	private ReturnClass processFile(File fileEntry){
		
        try {

            BufferedReader b = new BufferedReader(new FileReader(fileEntry));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                System.out.println(readLine);
                processFileLine(readLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		
		rc.setStatus("Success");
		return rc;
	}
	
	private ReturnClass processFileLine(String line){
		
		StringTokenizer defaultTokenizer = new StringTokenizer(line,",");
		String token;
		
		String instrument = "";
		String high = "";
		String low = "";
		String open = "";
		String close = "";
		
		int pos = 0;
		
		while (defaultTokenizer.hasMoreTokens())
		{
			token = defaultTokenizer.nextToken();
			pos++;
			//ignore first line
			if (!token.equalsIgnoreCase("Instr.")){
				token = token.replace("c", "");
				System.out.println("token: "+token);
			        	switch (pos){
			        	case 1:
			        		instrument = token;
			        		break;
				        case 3:
				        	high = token;
			        		break;
				        case 4:
				        	low = token;
			        		break;
				        case 5:
				        	open = token;
			        		break;
				        case 6:
				        	close = Integer.parseInt(open) + Integer.parseInt(token) +"";
			        		break;
			        	default:
			        		break;		        	
			        	}
					}
				else{
					break;
				}
			}
	    
	    System.out.println(instrument+" "+high+" "+low+" "+open+" "+close);
	        	
		rc.setStatus("Success");
		return rc;
		
	}
}
