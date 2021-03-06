package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.binary.Base64;


public class ProcessData {
	String metaInstFile = "";
	String metaScriptFile = "";
	ReturnClass rc = new ReturnClass("Init");
	
	ArrayList<InstrumentMetaData> instrumentData = new ArrayList<InstrumentMetaData>();
	
	String rScript = "";
	
	public ProcessData(String metaFile, String metaScriptFile){
		this.metaInstFile = metaFile;
		this.metaScriptFile = metaScriptFile;
	}
	
	/*
	 * Get all the meta data to control the processing to process and push data to the cloud storage
	 */
	
	public ReturnClass readMetaData(){
		
		//read script
		File scriptFile = new File(metaScriptFile);
        try (BufferedReader b = new BufferedReader(new FileReader(scriptFile))){
        	
        	rc.addLog("Lets begin reading the script file metadata");
            System.out.println("read script file");
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine, "SCR");
            }
            b.close();
        } catch (Exception e) {
        	rc.addLog("Error processing the file: "+e.getMessage());
        	rc.setStatus("Error");
        	rc.setDescription(e.getMessage());
            e.printStackTrace();
        }			
		
		//read instrument data
		File instrumentFile = new File(metaInstFile);
        try (BufferedReader b = new BufferedReader(new FileReader(instrumentFile))){
        	
        	rc.addLog("Lets begin reading the instrument file metadata");
            System.out.println("read instrument file");
            
            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                processFileLine(readLine, "INS");
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
	
	private ReturnClass processFileLine(String line, String lineType){

		if (lineType.equalsIgnoreCase("SCR")){
			String token = "";
			//System.out.println(line);
			StringTokenizer defaultTokenizer = new StringTokenizer(line,"<*>");
			while (defaultTokenizer.hasMoreTokens()){
				token = defaultTokenizer.nextToken();
			}
			rScript = token;
			//System.out.println( "script: "+rScript);
			
		}else if (lineType.equalsIgnoreCase("INS")){
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
		}
		
		rc.setStatus("Success");
		return rc;
	}
	
	/*
	 * Fetch the data per instrument and call the R-server to process the share price
	 */
	
	public ReturnClass processInstrumentData(){
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running on default port
             * 6311
             */
            connection = new RConnection();
            
            
            
    		for (int i = 0; i < instrumentData.size(); i++) {
    			System.out.println(instrumentData.get(i).getFileLine());
            
            //below worked!
	            //connection.eval("inFile='C:/Users/priyash.ramawthar/Dropbox/trader/appData/masterdata/ADI.txt'");
	            //connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/appData/masterdata/output/ADI_proc.txt'");
				//connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/appData/scripts/ImportProcessExportParams.r')");
	            
	            //connection.eval("inFile='C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/spy_historical_data.txt'");
	            //connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/spy_with_indicators.csv'");
	            //connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/ImportProcessExportParams.r')");
            
            

    			
                connection.eval("inFile='"+instrumentData.get(i).getInFile()+"'");
                connection.eval("outFile='"+instrumentData.get(i).getOutFile()+"'");
                org.rosuda.REngine.REXP rexp = connection.eval("source('"+rScript+"')");
//                
//                try{
//                	System.out.println("REXP: "+rexp.asString());
//                }catch (Exception e2){
//                	e2.printStackTrace();
//                }
                
    		}
            
        } catch (RserveException e) {
            e.printStackTrace();
        }

		rc.setStatus("Success");
		return rc;
	}
	
	
	/*
	 * Write the NEW processed data to the cloud storage area. Here it will be further analysed
	 */
	
	public ReturnClass writeIntrumentDataToCloud(){
System.out.println("start: writeIntrumentDataToCloud");
		//go through all the instruments in the instrumentData arraylist and call the webservice
		for (int i = 0; i < instrumentData.size(); i++) {
			
			System.out.println(instrumentData.get(i).getOutFile());
			ArrayList<ProcessedInstrumentData> processedData = fetchProcessedData(instrumentData.get(i));
			
			int k = 0;
			int j = 0;
			int max = 500; // max number of records per web service
			while (k < processedData.size()) {
			
				//String input = "[{\"instrument\":\"ADI\",\"close\":\"105\"}]";
				String input = "[";
				boolean last = false;
				int count = 0;
				while ((j < processedData.size())&&(count < max)) { 
					//System.out.println(processedData.get(j).getDate());
					if ((j == (processedData.size()-1))||(count == (max-1)))
						last = true;
					if (!last){
						input = input + processedData.get(j).getJSONFormat()+",";
					}else{
						input = input + processedData.get(j).getJSONFormat()+"]";
					}
					count++;
					j++;
				}
				
				
				//call the web service
				if (last){//this mean that we have records
					System.out.println("input: "+input);
					//put the below line back
					callPushWebWervice(input);
					instrumentData.get(i).setProcessingComplete(true);
					//System.out.println("callPushWebWervice(input)");
					//System.out.println(rc.getStatus()+" - "+rc.getDescription());
				}
				
				k = j;
			}
			writeInstrumentMetaData();
		}	


		
		rc.setStatus("Success");
		return rc;
	}
	
	private ArrayList<ProcessedInstrumentData> fetchProcessedData(InstrumentMetaData instrumentData){
		//fetch data for each instrument based on the path passed in  + update the last processed date
		ArrayList<ProcessedInstrumentData> instrumentDataArray = new ArrayList<ProcessedInstrumentData>();
		try {
			
			Path path = Paths.get(instrumentData.outFile);
			List<String> lines;
			
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			boolean dateFound = false;
			for (int j = 0; j < lines.size(); j++) {
				String token = "";
				int pos = 0;
				StringTokenizer defaultTokenizer = new StringTokenizer(lines.get(j),",");
				if (defaultTokenizer.hasMoreTokens()){
					token = defaultTokenizer.nextToken();
				}
				if (dateFound){
					ProcessedInstrumentData processedInstrumentData = new ProcessedInstrumentData();
					instrumentData.setNewLastProc(token);
					processedInstrumentData.setInstrument(instrumentData.getInstrumentName());
					processedInstrumentData.setDate(token);
					//build the ProcessedInstrumentData object here
					while (defaultTokenizer.hasMoreTokens()){
						token = defaultTokenizer.nextToken();
						pos++;
						switch (pos){
							//need to map sma5,rsi14,macd,macdsig
						//"sma5","rsi14","macd","signal"
				        	case 1:
				        		processedInstrumentData.setOpen(token);
				        		break;
		   		        	case 2:
				        		processedInstrumentData.setClose(token);
		   		        		break;
		   		        	case 3:
				        		processedInstrumentData.setHigh(token);
		   		        		break;
		   		        	case 4:
				        		processedInstrumentData.setLow(token);
		   		        		break;
		   		        	case 5:
				        		processedInstrumentData.setVol(token);
		   		        		break;
		   		        	case 6:
				        		processedInstrumentData.setSma20(token);
		   		        		break;
		   		        	case 7:
				        		processedInstrumentData.setObv(token);
		   		        		break;
		   		        	case 8:
				        		processedInstrumentData.setSma5(token);
		   		        		break;
		   		        	case 9:
				        		processedInstrumentData.setRsi14(token);
		   		        		break;
		   		        	case 10:
				        		processedInstrumentData.setMacd(token);
		   		        		break;
		   		        	case 11:
				        		processedInstrumentData.setMacdsig(token);
		   		        		break;
		   		        	case 12:
				        		processedInstrumentData.setRoc(token);
		   		        		break;
		   		        	default:
		   		        		break;
						}
						//System.out.println("token: "+token);
					}
					instrumentDataArray.add(processedInstrumentData);
					System.out.println("priceDate: "+token +" ---- "+ instrumentData.getLastProc());
				}

				if ((token.equalsIgnoreCase(instrumentData.getLastProc()))||("\"01-Nov-00\"".equalsIgnoreCase(instrumentData.getLastProc())))
					dateFound = true;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}	
		return instrumentDataArray;
	}
	
	private ReturnClass callPushWebWervice(String serviceParams){
		  try {
			  System.out.println("callPush");
				
				rc.setStatus("Success");
				
				//URL url = new URL("http://localhost:5000/uploadData");
				URL url = new URL("http://tradeanaylser.eu-west-1.elasticbeanstalk.com/uploadData");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				
				String username = "user";
				String password = "9af718b5-1ead-4674-8e6d-eeec9580879a";
				//String password = "fc943d50-8746-4efb-a395-ed7631701713";
		        String authString = username + ":" + password;
		        String authStringEnc = new String(Base64.encodeBase64(authString.getBytes()));
		        conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
				conn.setRequestProperty("Content-Type", "application/json");

				//String input = "{\"qty\":100,\"name\":\"iPad 4\"}";
				String input = serviceParams;

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
					rc.setStatus("Error");
					rc.setDescription("Failed : HTTP error code : "+conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				rc.setDescription("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					//System.out.println(output);
					rc.setDescription(rc.getDescription()+output);
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				rc.setStatus("Error");
				rc.setDescription("Failed : HTTP error code : "+e.getMessage());

			  } catch (IOException e) {

				rc.setStatus("Error");
				rc.setDescription("Failed : HTTP error code : "+e.getMessage());

			 }
		return rc;		
	}
	
	/*
	 * Write the updated meta data back to the metadata controlling file
	 */
	
	public ReturnClass writeInstrumentMetaData(){
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < instrumentData.size(); i++) {
			lines.add(instrumentData.get(i).getFileLine());
			//System.out.println(instrumentData.get(i).getFileLine());
		}
		
		try {
			
			Path path = Paths.get(metaInstFile);

			Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			rc.setStatus("Error");
			rc.setDescription(e.getMessage());
			e.printStackTrace();
		}
		
		rc.setStatus("Success");
		return rc;
	}
	/*
	public static void main(String[] args){
		ProcessData pd = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				  						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		pd.readMetaData();
		pd.processInstrumentData();
		pd.writeIntrumentDataToCloud();
		pd.writeInstrumentMetaData();
	}*/
}
