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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareData {
	
	String inputPath;
	String outputPath;
	String processedPath;
	String errorPath;
	
	ReturnClass rc = new ReturnClass("Init");
	
	public PrepareData(String inputPath, String processedPath, String errorPath, String outputPath) {
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
		this.processedPath = processedPath;
		this.errorPath = errorPath;
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
	            	//copy the file to processed folder
	            	copyFile(inputPath, processedPath, fileEntry.getName());
	            }else{
	            	//copy the file to error folder
	            	copyFile(inputPath, errorPath, fileEntry.getName());
	            }
	            deleteFile(inputPath, fileEntry.getName());
	            rc.setStatus("Init");
	            
	    }
		
		return rc;
		
	}
	
	private String getDate(File fileEntry, ReturnClass rc){
		
		
		String year = fileEntry.getName().substring(15,17);
		String month = fileEntry.getName().substring(17,19);
		String day = fileEntry.getName().substring(19,21);
		month = new DateFormatSymbols().getShortMonths()[Integer.parseInt(month)-1];
		rc.setStatus("Success");
		return day+"-"+month+"-"+year;
		
	}
	
	private ReturnClass processFile(File fileEntry){
		
        try (BufferedReader b = new BufferedReader(new FileReader(fileEntry))){
        	
        	String fileDate = getDate(fileEntry,rc);
        	rc.addLog("File date: "+fileDate);
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine, fileDate);
            }
            b.close();
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
		String vol = "";
		
		int pos = 0;
		
		while (defaultTokenizer.hasMoreTokens())
		{
			token = defaultTokenizer.nextToken();
			pos++;
			//ignore first line
			if (!token.equalsIgnoreCase("Instr.")){
				//token = token.replace("c", "");
				//System.out.println("token: "+token);
			        	switch (pos){
			        	case 1:
			        		instrument = token;
			        		break;
				        case 3:
				        	token = token.replace("c", "");
				        	high = token;
			        		break;
				        case 4:
				        	token = token.replace("c", "");
				        	low = token;
			        		break;
				        case 5:
				        	token = token.replace("c", "");
				        	open = token;
			        		break;
				        case 6:
				        	token = token.replace("c", "");
				        	close = Integer.parseInt(open) + Integer.parseInt(token) +"";
			        		break;
				        case 7:
				        	vol = token;
			        		break;
			        	default:
			        		break;		        	
			        	}
					}
				else{
					break;
				}
			}
	    
	    System.out.println(instrument+" "+high+" "+low+" "+open+" "+close+" "+vol);
	    if (!instrument.equalsIgnoreCase("")){
	    	writeToMasterFile(instrument, high, low, open, close, vol, fileDate);
	    }
	        	
		rc.setStatus("Success");
		return rc;
		
	}
	
	private ReturnClass writeToMasterFile(String instrument, 
										  String high, 
										  String low, 
										  String open, 
										  String close,
										  String vol,
										  String fileDate){	
		rc.setStatus("Success");
		try {
			
			Path path = Paths.get(outputPath+instrument+".txt");
			List<String> lines;
			
				lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	
	
			String newLine = fileDate+","+open+","+close+","+high+","+low+","+vol;  
	
			lines.add(newLine);
			
			//keep it to around +-4 years of data max
			if (lines.size()>1052)
				lines.remove(1);
			Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}	
		return rc;		
	}

	
	private ReturnClass copyFile(String currPath, String newPath, String file){
    	try{

     	   //File afile =new File(currPath+"\\"+file);
     	   
     	   //afile.renameTo(new File(newPath+"\\"+file));
     	   Path source = Paths.get(currPath+"\\"+file);
     	   Path destination = Paths.get(newPath+"\\"+file);
     	   
     	   List<String> lines;
			
     	   lines = Files.readAllLines(source, StandardCharsets.UTF_8);
     	   
     	   Files.write(destination, lines, StandardCharsets.UTF_8);
     	   
     	   lines.clear();

     	   System.out.println(currPath+"\\"+file);
     	   System.out.println(newPath+"\\"+file);

     	}catch(Exception e){
     		rc.setStatus("Error");
     		rc.setDescription(e.getMessage());
     		e.printStackTrace();
     	}
		return rc;
	}	
	
	
	private ReturnClass deleteFile(String currPath, String file){
    	try{
    		
    		Path source = Paths.get(currPath+"\\"+file);
    		Files.deleteIfExists(source);
    		

     	}catch(Exception e){
     		rc.setStatus("Error");
     		rc.setDescription(e.getMessage());
     		e.printStackTrace();
     	}
		return rc;
	}	
	
	private ReturnClass rollbackToDate(String instrument, String fileDate){	
	rc.setStatus("Success");
	try {
	
		//Path path = Paths.get(outputPath+instrument+".txt");
		Path path = Paths.get(outputPath+instrument);
		List<String> lines;
		
		ArrayList<Integer> indexesToRemove= new ArrayList<Integer>();
		
		lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		String token = "not found";
		for (int i = 0; i < lines.size(); i++){
			if (token.equalsIgnoreCase("FOUND")){
				indexesToRemove.add(i);
			}else{
				StringTokenizer defaultTokenizer = new StringTokenizer(lines.get(i),",");
				token = defaultTokenizer.nextToken();
				if (token.equalsIgnoreCase(fileDate)){
					token = "FOUND";
				}
			}
			
		}
		
		//remove the found indexes
		for (int j = 0; j<indexesToRemove.size(); j++){
			System.out.println("remove index: "+indexesToRemove.get(0));
			System.out.println("line: "+lines.get(indexesToRemove.get(0)));
			lines.remove(lines.get(indexesToRemove.get(0)));
		}
		
		for (int j = 0; j<lines.size(); j++){
			System.out.println(lines.get(j));
		}		
		
		Files.write(path, lines, StandardCharsets.UTF_8);
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		rc.setStatus("Error");
		rc.setDescription(e.getMessage());
		e.printStackTrace();
	}	
	return rc;		
	}
	
	public ReturnClass rollbackAllFiles(String fileDate){
		
		File folder = new File(outputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	            System.out.println(fileEntry.getName());
	    		rollbackToDate(fileEntry.getName(),fileDate);
	    }
		
		return rc;
		
	}	
	
//	public static void main(String [] args){
//		System.out.println("prepare the files for R");
//		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
//				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
//				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
//										 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
//
//		System.out.println(pd.processNewData().getStatus());
//	}
	
//	public static void main(String [] args){
//	System.out.println("prepare the files for R");
//	PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
//			 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
//			 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
//									 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
//
//	//System.out.println(pd.rollbackToDate("ASC.txt","03-Nov-17").getStatus());
//	System.out.println(pd.rollbackAllFiles("03-Nov-17").getStatus());
//}
	
}
