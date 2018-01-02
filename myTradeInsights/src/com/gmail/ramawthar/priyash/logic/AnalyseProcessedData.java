package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class AnalyseProcessedData {
	

	private String metaInstFile = "";
	private ArrayList<InstrumentMetaData> instrumentData = new ArrayList<InstrumentMetaData>();
	private ReturnClass rc = new ReturnClass("Init");
	
	
	public AnalyseProcessedData(String metaFile){
		this.metaInstFile = metaFile;
	}
	
	/*
	 * Get all the meta data to control the processing to process and push data to the cloud storage
	 */
	
	public ReturnClass readMetaData(){
		//read instrument data
		File instrumentFile = new File(metaInstFile);
        try (BufferedReader b = new BufferedReader(new FileReader(instrumentFile))){
        	
        	rc.addLog("Lets begin reading the instrument file metadata");
            System.out.println("read instrument file");
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine);
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
	
	private ReturnClass processFileLine(String line){

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
	        		instrumentMetaData.setInstrumentName(token);
	        		break;
	        	case 2:
	        		instrumentMetaData.setInFile(token);
	        		break;
	        	case 3:
	        		instrumentMetaData.setOutFile(token);
	        		break;
	        	case 4:
	        		instrumentMetaData.setLastProc(token);
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
	
	public ReturnClass createInsights(){

		//for (int i = 0; i < instrumentData.size(); i++) {

			System.out.println(instrumentData.get(1).getOutFile());
			analyzeProcessedData(fetchProcessedData(instrumentData.get(1).getOutFile()));
			
		//}
		return rc;
	}
	
	private void analyzeProcessedData(ArrayList<ProcessedInstrumentData> data){
		
		int numberOfLines = 10;//data.size() - 990;
		for (int i = numberOfLines;i < data.size(); i++ ){
			System.out.println(data.get(i).getDate());
		}
		
	}
	
	private ArrayList<ProcessedInstrumentData> fetchProcessedData(String instrumentDataPath){
		//fetch data for each instrument based on the path passed in 
		ArrayList<ProcessedInstrumentData> instrumentDataArray = new ArrayList<ProcessedInstrumentData>();

		List<String> lines = null;
		try {
			String instrumentName = getInstrumentName(instrumentDataPath);
			instrumentDataPath = instrumentDataPath.replace("priyash.ramawthar", "priyash");
			
			Path path = Paths.get(instrumentDataPath);

			
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			

			for (int j = 0; j < lines.size(); j++) {
				String token = "";
				int pos = 0;
				StringTokenizer defaultTokenizer = new StringTokenizer(lines.get(j),",");
				
				ProcessedInstrumentData processedInstrumentData = new ProcessedInstrumentData();
				
				processedInstrumentData.setInstrument(instrumentName);
				//build the ProcessedInstrumentData object here
				while (defaultTokenizer.hasMoreTokens()){
					token = defaultTokenizer.nextToken();
					pos++;
					switch (pos){
						//need to map sma5,rsi14,macd,macdsig
					//"sma5","rsi14","macd","signal"
			        	case 1:
							processedInstrumentData.setDate(token);
			        		break;
	   		        	case 2:
			        		processedInstrumentData.setOpen(token);
	   		        		break;
	   		        	case 3:
			        		processedInstrumentData.setClose(token);
	   		        		break;
	   		        	case 4:
			        		processedInstrumentData.setHigh(token);
	   		        		break;
	   		        	case 5:
			        		processedInstrumentData.setLow(token);
	   		        		break;
	   		        	case 6:
			        		processedInstrumentData.setVol(token);
	   		        		break;
	   		        	case 7:
			        		processedInstrumentData.setSma20(token);
	   		        		break;
	   		        	case 8:
			        		processedInstrumentData.setObv(token);
	   		        		break;
	   		        	case 9:
			        		processedInstrumentData.setSma5(token);
	   		        		break;
	   		        	case 10:
			        		processedInstrumentData.setRsi14(token);
	   		        		break;
	   		        	case 11:
			        		processedInstrumentData.setMacd(token);
	   		        		break;
	   		        	case 12:
			        		processedInstrumentData.setMacdsig(token);
	   		        		break;
	   		        	case 13:
			        		processedInstrumentData.setRoc(token);
	   		        		break;
	   		        	default:
	   		        		break;
					}
					//System.out.println("token: "+token);
				}
				instrumentDataArray.add(processedInstrumentData);
			}
			
		} catch (Exception e) {
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}	
		return instrumentDataArray;
	}
	
	private String getInstrumentName(String instrumentPath){
		return instrumentPath.substring(68, instrumentPath.indexOf("_proc.txt"));
	}
}
