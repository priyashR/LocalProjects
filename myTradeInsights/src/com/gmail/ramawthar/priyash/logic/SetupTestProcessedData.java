package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//import java.io.IOException;
//import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class SetupTestProcessedData {
	

	private String metaInstFile = "";
	private ArrayList<InstrumentMetaData> instrumentData = new ArrayList<InstrumentMetaData>();
	private ReturnClass rc = new ReturnClass("Init");
	
	private String writeRoot = "";
	private String instance = "";
	
	
	public SetupTestProcessedData(String metaFile){
		this.metaInstFile = metaFile;
	}
	
	/*
	 * Get all the meta data to control the processing to process and push data to the cloud storage
	 */
	
	public ReturnClass readMetaData(String instance, String writeRoot){
		//read instrument data
		
		this.writeRoot = writeRoot;
		this.instance = instance;
		File instrumentFile = new File(metaInstFile);
        try (BufferedReader b = new BufferedReader(new FileReader(instrumentFile))){
        	
        	rc.addLog("Lets begin reading the instrument file metadata");
            System.out.println("read instrument file");
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine, instance);
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
	
	private ReturnClass processFileLine(String line, String instance){

		String token = "";
        int pos = 0;
		//System.out.println(line);
		InstrumentMetaData instrumentMetaData = new InstrumentMetaData();
		StringTokenizer defaultTokenizer = new StringTokenizer(line,"<*>");
		while (defaultTokenizer.hasMoreTokens()){
			token = defaultTokenizer.nextToken();
			pos++;
			switch (pos){
	        	case 1:
	        		instrumentMetaData.setInstrumentName(token.replace("priyash.ramawthar", instance));
	        		break;
	        	case 2:
	        		instrumentMetaData.setInFile(token.replace("priyash.ramawthar", instance));
	        		break;
	        	case 3:
	        		instrumentMetaData.setOutFile(token.replace("priyash.ramawthar", instance));
	        		break;
	        	case 4:
	        		instrumentMetaData.setLastProc(token.replace("priyash.ramawthar", instance));
	        		break;
	        	default:
	        		break;
			}
			//System.out.println("token: "+token);
		}
		if (!(instrumentMetaData.getInstrumentName().equalsIgnoreCase(""))){
			instrumentData.add(instrumentMetaData);		
		}
		
		rc.setStatus("Success");
		return rc;
	}
	
	public ReturnClass createTestData(String date){

		for (int i = 0; i < instrumentData.size(); i++) {

			System.out.println(instrumentData.get(i).getOutFile());
			createNewFile(instrumentData.get(i).getOutFile(), date, instrumentData.get(i).getInstrumentName());
			
		}
		return rc;
	}
	
	private ArrayList<ProcessedInstrumentData> createNewFile(String instrumentDataPath, String date, String instrument){
		//fetch data for each instrument based on the path passed in 
		ArrayList<ProcessedInstrumentData> instrumentDataArray = new ArrayList<ProcessedInstrumentData>();

		String instrumentDataOutTestPath = writeRoot+instrument+"_proc.txt";
		System.out.println("instrumentDataOutTestPath : "+instrumentDataOutTestPath);
		List<String> linesIn = null;
		ArrayList<String> linesOut = new ArrayList<String>();
		try {
			Path path = Paths.get(instrumentDataPath);
			
			linesIn = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			int fileSize = linesIn.size();
			System.out.println("Fize size: "+fileSize);
			for (int j = 0; j < fileSize; j++) {
				String token = "";
				StringTokenizer defaultTokenizer = new StringTokenizer(linesIn.get(j),",");
				
				token = defaultTokenizer.nextToken();
				System.out.println("token: "+token);
				linesOut.add(linesIn.get(j));
				if (token.equalsIgnoreCase(date)){
					j = fileSize;
				}
			}

			System.out.println("Out fize size: "+linesOut.size());
			//write out test data
			Path outPath = Paths.get(instrumentDataOutTestPath);
			System.out.println("Write path: "+instrumentDataOutTestPath);
			
			Files.write(outPath, linesOut);
			
		} catch (Exception e) {
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}	
		return instrumentDataArray;
	}
	
	public void moveFiles(){
		System.out.println("move the files!");
		
		
		try {
			//Runtime.getRuntime().exec("cmd.exe /c C:\\Users\\Priyash\\Dropbox\\trader\\appData\\runnable\\moveFiles.bat");
			Runtime.getRuntime().exec("cmd /c start C:\\Users\\Priyash\\Dropbox\\trader\\appData\\runnable\\moveFiles.bat");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	public static void main(String args[]){
		SetupTestProcessedData sd = new SetupTestProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt");
		sd.readMetaData("priyash", "C:\\Users\\Priyash\\Dropbox\\trader\\appData\\masterdata\\test_files\\testInsightsFiles\\");
		sd.createTestData("\"10-Jan-18\"");
	    
		sd.moveFiles();
		
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Please press enter when the files have completed copying");
	    String name = scanner.next();
		scanner.close();
	    
		AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
						   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\insightMetaData.txt",
						   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\myShares.txt");
		
		ad.readMetaData("priyashteststart");
		ArrayList<String> instruments = new ArrayList<String>();
		instruments.add("ADI");
		//instruments.add("MTN");
		//instruments.add("AFH");
		//instruments.add("EOH");
		ad.createSetInsights(instruments);
	}
}
