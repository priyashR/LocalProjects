package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
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
	private String metaInsightFile = "";
	private String metaOwnedInstruments = "";
	private String InsightFilePath = "";
	private String instance = "";
	private ArrayList<InstrumentMetaData> instrumentData = new ArrayList<InstrumentMetaData>();
	private ArrayList<OwnedInstrument> myInstruments = new ArrayList<OwnedInstrument>();
	private ReturnClass rc = new ReturnClass("Init");
	
	private ArrayList<Insight> insights = new ArrayList<Insight>();
	
	private InstrumentInsights getInsight = new InstrumentInsights();
	
	
	public AnalyseProcessedData(String metaFile, String metaInsightFile, String metaOwnedInstruments){
		this.metaInstFile = metaFile;
		this.metaInsightFile = metaInsightFile;
		this.metaOwnedInstruments = metaOwnedInstruments;
	}
	
	/*
	 * Get all the meta data to control the processing to process and push data to the cloud storage
	 */
	
	public ReturnClass readMetaData(String instance){
		//read instrument data
		
		this.instance = instance;
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
        
        //read insight path data
		File insightFile = new File(metaInsightFile);
        try (BufferedReader b = new BufferedReader(new FileReader(insightFile))){
        	
            String readLine = "";
            String token = "";
            while ((readLine = b.readLine()) != null) {
        		StringTokenizer defaultTokenizer = new StringTokenizer(readLine,"<*>");
	        		while (defaultTokenizer.hasMoreTokens()){
	        			token = defaultTokenizer.nextToken();
	        		}
    		}
            b.close(); 
            InsightFilePath = token;
        if (!(instance.equalsIgnoreCase("priyash.ramawthar")))
        	InsightFilePath = token.replace("priyash.ramawthar", "priyash");//+"test\\"+instance;  
        
        } catch (Exception e) {
        	rc.addLog("Error processing the file: "+e.getMessage());
        	rc.setStatus("Error");
        	rc.setDescription(e.getMessage());
            e.printStackTrace();
        }	
        
        
        //read my share data
        File ownedInstruments = new File(metaOwnedInstruments);
        try (BufferedReader b = new BufferedReader(new FileReader(ownedInstruments))){
        	
            String readLine = "";
            String token = "";
            OwnedInstrument owned;
            int pos;
            while ((readLine = b.readLine()) != null) {
            	owned = new OwnedInstrument();
            	pos = 0;
        		StringTokenizer defaultTokenizer = new StringTokenizer(readLine,",");
	        		while (defaultTokenizer.hasMoreTokens()){
	        			token = defaultTokenizer.nextToken();
	        			pos++;
	        			switch (pos){
	        	        	case 1:
	        	        		owned.setInstrument(token);
	        	        		break;
	        	        	case 2:
	        	        		owned.setClosePrice(new Double(token));
	        	        		break;
	        	        	default:
	        	        		break;
	        			}
	        		}
	        		myInstruments.add(owned);
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
		System.out.println(line);
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
	
	public ReturnClass createInsights(){

		for (int i = 0; i < instrumentData.size(); i++) {
			try {
				insights = new ArrayList<Insight>();
				System.out.println(instrumentData.get(i).getOutFile());
				analyzeProcessedData(fetchProcessedData(instrumentData.get(i).getOutFile()));
				
				//write out to file
				writeInsightToFile();
				
				//push to cloud
				pushToCloud();
			}
			catch (Exception e){
				
			}
			
		}
		return rc;
	}
	
	public ReturnClass createSetInsights(ArrayList<String> instruments){

		for (int i = 0; i < instrumentData.size(); i++) {
			try {
				if (instruments.contains(instrumentData.get(i).getInstrumentName())){
					System.out.println(instrumentData.get(i).getInstrumentName());
					insights = new ArrayList<Insight>();
					System.out.println(instrumentData.get(i).getOutFile());
					analyzeProcessedData(fetchProcessedData(instrumentData.get(i).getOutFile()));
					
					//write out to file
					writeInsightToFile();
					
					//push to cloud
					pushToCloud();
				}
			}
			catch (Exception e){
				
			}
			
		}
		return rc;
	}
	
	private void analyzeProcessedData(ArrayList<ProcessedInstrumentData> data){

		int numberOfLines = data.size() - 10;
		for (int i = numberOfLines;i < data.size(); i++ ){
			//System.out.println(data.get(i).getDate()+" : "+data.get(i).getAdx());
			BigDecimal b = data.get(i).getLongSma5().subtract(data.get(i).getBigDecimalSma20());
			//System.out.println(data.get(i).getDate()+" : "+data.get(i).getLongClose()+" : "+data.get(i).getLongSma5()+" : "+data.get(i).getBigDecimalSma20()+" : "+b);
			//Double d1 = getInsight.getPercent(data.get(i).getLongClose(), data.get(i).getLongSma5());
			//Double d2 = getInsight.getPercent(data.get(i).getLongClose(), data.get(i).getBigDecimalSma20());
			//System.out.println(data.get(i).getDate()+" : "+data.get(i).getLongClose()+" : "+d1 +" : "+d2+" : "+(d1-d2));
			Double d1 = getInsight.getPercent(data.get(i).getBigDecimalSma20(), data.get(i).getLongSma5() );
			//System.out.println(data.get(i).getDate()+" : "+data.get(i).getLongClose()+" : "+data.get(i).getLongSma5() +" : "+data.get(i).getBigDecimalSma20()+" : "+b+" : "+d1);
			System.out.println(data.get(i).getDate()+" : "+data.get(i).getLongClose()+" : "+data.get(i).getLongMacd() +" : "+data.get(i).getLongMacdsig());
		}
		 
		Insight i;
		
		// check if you own the instrument
		boolean owned = checkIfOwned(data.get(0).getInstrument());
		//-------------------------------------------------------------------------------------------------------------------
		// apply the the rules and create the general insights
		
		//how far from turning
/*
		i = new Insight();
		i = getInsight.I001a(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/
		// when was the last turning point
/*		
		i = new Insight();
		i = getInsight.I001b(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);			
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/
		
		//Percentage difference between SMA5 and SMA20
/*
		i = new Insight();
		i = getInsight.V002a(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/

		// Actual difference between SMA5 and SMA20
/*
		i = new Insight();
		i = getInsight.V002b(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/

		// ROC since the last turning point
/*
		i = new Insight();
		i = getInsight.V002c(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/

		// % change since the last turning point
/*
		i = new Insight();
		i = getInsight.V002d(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
*/

		// % highest high / lowest low check
		i = new Insight();
		i = getInsight.I004(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// moving average SMA20 turning point
		i = new Insight();
		i = getInsight.I005(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// Three moving averages
		i = new Insight();
		i = getInsight.I006(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// Three moving averages
		i = new Insight();
		i = getInsight.I007(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// MACD crossover
		i = new Insight();
		i = getInsight.I008a(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// MACD
		i = new Insight();
		i = getInsight.I008b(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// MACD state and movement
		i = new Insight();
		i = getInsight.V008c(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// signal state and movement
		i = new Insight();
		i = getInsight.V008d(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check if the MACD and signal are positive
		i = new Insight();
		i = getInsight.I008e(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check the RSI
		i = new Insight();
		i = getInsight.I009(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check the ADX
		i = new Insight();
		i = getInsight.I010(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since two days ago
		i = new Insight();
		i = getInsight.V001_2(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since five days ago
		i = new Insight();
		i = getInsight.V001_5(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since ten days ago
		i = new Insight();
		i = getInsight.V001_10(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());	

		// Include the closing price
		i = new Insight();
		i = getInsight.V003(data);
		if (owned) i.setOwned("Y");
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());	
		
		// check if I have this instrument and apply the trending insights

		if (owned) {
			//create the insights here!!!!
		}
		//-------------------------------------------------------------------------------------------------------------------
		
	}
	
	private boolean checkIfOwned(String instrument){
		// write the logic to check if you own the instrument passed in
		// use the file of owned instruments to validate
		for (int index = 0; index < myInstruments.size(); index ++){
			if (myInstruments.get(index).getInstrument().equalsIgnoreCase(instrument))
				return true;
		}
		return false;
	}
	
	private ArrayList<ProcessedInstrumentData> fetchProcessedData(String instrumentDataPath){
		//fetch data for each instrument based on the path passed in 
		ArrayList<ProcessedInstrumentData> instrumentDataArray = new ArrayList<ProcessedInstrumentData>();

		List<String> lines = null;
		try {
			String instrumentName = getInstrumentName(instrumentDataPath);
			//instrumentDataPath = instrumentDataPath.replace("priyash.ramawthar", "priyash");
			
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
	   		        	case 14:
			        		processedInstrumentData.setDip(token);
	   		        		break;
	   		        	case 15:
			        		processedInstrumentData.setDin(token);
	   		        		break;
	   		        	case 16:
			        		processedInstrumentData.setDx(token);
	   		        		break;
	   		        	case 17:
			        		processedInstrumentData.setAdx(token);
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
		//System.out.println("instrumentPath: "+instrumentPath);
		String instrumentName = instrumentPath;
		if(!(instance.equalsIgnoreCase("priyash.ramawthar"))){
			instrumentName = instrumentPath.replace(instance, "priyash.ramawthar");   
		}
		//System.out.println("instrumentPath: "+instrumentPath);
		//System.out.println("instrumentName: "+instrumentName);
		
		return instrumentName.substring(68, instrumentName.indexOf("_proc.txt"));
	}
	
	private void writeInsightToFile(){
		
		if (insights.size() <= 0){
			return; // nothing to write
		}

        
		ArrayList<String> linesOut = new ArrayList<String>();
		String fileName = insights.get(0).getInstrument()+"_"+insights.get(0).getDate().replaceAll("\"", "");
		System.out.println("fileName: "+fileName);
		String filePath = InsightFilePath+fileName;
		System.out.println("filePath: "+filePath);
		Path path = Paths.get(filePath);
		
		String line = "";
		
		/*
		 * 	x	this.instrument = instrument;
			x	this.insightCode = insightCode;
			x	this.insightDesc = insightDesc;
			x	this.date = date;
				this.insightValue = insightValue;
				this.insightNote = insightNote;
				this.insightRec = insightRec;
		 */
		for (int i = 0; i < insights.size(); i++){
			line = insights.get(i).getInstrument()+","+
				   insights.get(i).getDate()+","+
				   insights.get(i).getInsightCode()+","+
				   insights.get(i).getInsightDesc()+","+
				   insights.get(i).getInsightType()+","+
				   insights.get(i).getInsightValue()+","+
				   insights.get(i).getInsightNote()+","+
				   insights.get(i).getInsightRec()+","+
				   insights.get(i).getOwned();
			linesOut.add(line);
		}
		
		
		try {
			System.out.println(path);
			Files.write(path, linesOut);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		
	}
	private void pushToCloud(){
		String fileName = insights.get(0).getInstrument()+"_"+insights.get(0).getDate().replaceAll("\"", "");
		System.out.println("fileName: "+fileName);
		String filePath = InsightFilePath+fileName;
		System.out.println("filePath: "+filePath);
		Path path = Paths.get(filePath);
		pushFileToCloud(path);
		
	}
	private void pushFileToCloud(Path filePath){
		//read each line and call aws.pushInsightToDB
		System.out.println("Push to cloud");
		File insightFile = new File(filePath.toString());
        try (BufferedReader b = new BufferedReader(new FileReader(insightFile))){
        	
        	rc.addLog("Lets begin reading the insight file metadata");
            //System.out.println("read insight file");
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) { 
                //System.out.println(readLine);

            	//call 
            	AWS aws = new AWS();
            	aws.pushInsightToDB(loadInsightObject(readLine));
                
            }
            b.close();
        } catch (Exception e) {
        	rc.addLog("Error processing the file: "+e.getMessage());
        	rc.setStatus("Error");
        	rc.setDescription(e.getMessage());
            e.printStackTrace();
        }
	}
	
	private Insight loadInsightObject(String line){
		
		String token = "";
        int pos = 0;
		//System.out.println(line);
        Insight insight = new Insight();
		StringTokenizer defaultTokenizer = new StringTokenizer(line,",");
		while (defaultTokenizer.hasMoreTokens()){
			token = defaultTokenizer.nextToken();
			pos++;
			switch (pos){
	        	case 1:
	        		insight.setInstrument(token);
	        		break;
	        	case 2:
	        		insight.setDate(token.replace("\"", ""));
	        		//System.out.println("token: "+insight.getDate());
	        		break;
	        	case 3:
	        		insight.setInsightCode(token);
	        		break;
	        	case 4:
	        		insight.setInsightDesc(token);
	        		break;
	        	case 5:
	        		insight.setInsightType(token);
	        		break;
	        	case 6:
	        		insight.setInsightValue(token);
	        		break;
	        	case 7:
	        		insight.setInsightNote(token);
	        		break;
	        	case 8:
	        		insight.setInsightRec(token);
	        		break;
	        	case 9:
	        		insight.setOwned(token);
	        		break;
	        	default:
	        		break;
			}
			//System.out.println("token: "+token);
		}
		
		return insight;
	}
	
	public void pushBatchOfFilesToCloud(String inputPath, String processedPath, String errorPath){
		
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	            //System.out.println(fileEntry.getName());
	            //System.out.println(getDate(fileEntry, rc));
	    	try{
	    		rc.addLog("Processing file: "+fileEntry.getName());
	    		pushFileToCloud(fileEntry.toPath());
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
	    	catch (Exception e){
	    		
	    	}
	            
	    }
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
	
	/*public static void main(String args[]){
		
		AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\insightMetaData.txt",
				   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\myShares.txt");
		
		ad.readMetaData("priyashteststart");
		ad.pushBatchOfFilesToCloud("C:\\Users\\Priyash\\Dropbox\\trader\\appData\\masterdata\\insights\\batch\\uncopied", 
								   "C:\\Users\\Priyash\\Dropbox\\trader\\appData\\masterdata\\insights\\batch\\done", 
								   "C:\\Users\\Priyash\\Dropbox\\trader\\appData\\masterdata\\insights\\batch\\done");
		
		
	}*/
}
