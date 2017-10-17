package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareData {
	
	String inputPath;
	String outputPath;
	ReturnClass rc = new ReturnClass("Init");
	
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
	            //System.out.println(fileEntry.getName());
	            //System.out.println(getDate(fileEntry, rc));
	    		rc.addLog("Processing file: "+fileEntry.getName());
	            processFile(fileEntry);
	            if (!(rc.getStatus().equalsIgnoreCase("ERROR"))){
	            	//move the file to processed folder
	            }else{
	            	//move the file to error folder
	            }
	            rc.setStatus("Init");
	            
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
        	String fileDate = getDate(fileEntry,rc);
        	rc.addLog("File date: "+fileDate);
            BufferedReader b = new BufferedReader(new FileReader(fileEntry));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine, fileDate);
            }

        } catch (Exception e) {
        	rc.addLog("Error processing the file: "+e.getMessage());
        	rc.setStatus("Error");
        	rc.setDescription(e.getMessage());
            e.printStackTrace();
        }
		
		return rc;
	}
	
	private ReturnClass processFileLine(String line, String fileDate){
		
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
				//System.out.println("token: "+token);
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
	    if (!instrument.equalsIgnoreCase("")){
	    	writeToMasterFile(instrument, high, low, open, close, fileDate);
	    }
	        	
		rc.setStatus("Success");
		return rc;
		
	}
	
	private ReturnClass writeToMasterFile(String instrument, 
										  String high, 
										  String low, 
										  String open, 
										  String close,
										  String fileDate){	
		rc.setStatus("Success");
		try {
			
			Path path = Paths.get(outputPath+instrument+".txt");
			List<String> lines;
			
				lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	
	
			int position = 1;
			String newLine = fileDate+","+open+","+close+","+high+","+low;  
	
			lines.add(position, newLine);
			
			//keep it to around +-4 years of data max
			if (lines.size()>1052)
				lines.remove(lines.size());
			Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}	
		return rc;		
	}
}
