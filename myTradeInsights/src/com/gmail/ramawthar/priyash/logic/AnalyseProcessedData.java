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

import com.gmail.ramawthar.priyash.WS.AWS;
import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class AnalyseProcessedData {
	

	private String metaInstFile = "";
	private String metaInsightFile = "";
	private String InsightFilePath = "";
	private String instance = "";
	private ArrayList<InstrumentMetaData> instrumentData = new ArrayList<InstrumentMetaData>();
	private ReturnClass rc = new ReturnClass("Init");
	
	private ArrayList<Insight> insights = new ArrayList<Insight>();
	
	private InstrumentInsights getInsight = new InstrumentInsights();
	
	
	public AnalyseProcessedData(String metaFile, String metaInsightFile){
		this.metaInstFile = metaFile;
		this.metaInsightFile = metaInsightFile;
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

			insights = new ArrayList<Insight>();
			System.out.println(instrumentData.get(i).getOutFile());
			analyzeProcessedData(fetchProcessedData(instrumentData.get(i).getOutFile()));
			
			//write out to file
			writeInsightToFile();
			
			//push to cloud
			pushToCloud();
			
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
		 
		Insight i = new Insight();
		// apply the the rules and create the general insights
		
		//how far from turning
		i = getInsight.I001a(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());
		
		// when was the last turning point
		i = getInsight.I001b(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);			
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		//Percentage difference between SMA5 and SMA20
		i = getInsight.V002a(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// Actual difference between SMA5 and SMA20
		i = getInsight.V002b(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// ROC since the last turning point
		i = getInsight.V002c(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// % change since the last turning point
		i = getInsight.V002d(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// % highest high / lowest low check
		i = getInsight.I004(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// moving average SMA20 turning point
		i = getInsight.I005(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// Three moving averages
		i = getInsight.I006(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());

		// Three moving averages
		i = getInsight.I007(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// MACD crossover
		i = getInsight.I008a(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// MACD
		i = getInsight.I008b(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// MACD state and movement
		i = getInsight.V008c(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
		// signal state and movement
		i = getInsight.V008d(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check if the MACD and signal are positive
		i = getInsight.I008e(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check the RSI
		i = getInsight.I009(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Check the ADX
		i = getInsight.I010(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since two days ago
		i = getInsight.V001_2(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since two days ago
		i = getInsight.V001_5(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights

		// Percentage change since two days ago
		i = getInsight.V001_10(data);
		if ((!(i.getInsightCode().equalsIgnoreCase("NONE")))&&(!(i.getInsightValue().equalsIgnoreCase(""))))
			insights.add(i);
		System.out.println(i.getInstrument()+" : "+i.getInsightCode()+" : "+i.getInsightValue());		
		// check if I have this instrument and apply the trending insights
		
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
				   insights.get(i).getInsightRec();
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
            System.out.println("read insight file");
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                //System.out.println(readLine);

            	//call 
            	AWS aws = new AWS();
            	aws.pushInsightToDB(loadInsightObject(readLine));
                loadInsightObject(readLine);
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
	        	default:
	        		break;
			}
			//System.out.println("token: "+token);
		}
		
		return insight;
	}
	
	public void pushBatchOfFilesToCloud(){
		//read the batch file unprocessed directory
		//for each file call pushFileToCloud
		//move processed files to the processed directory
	}
}
