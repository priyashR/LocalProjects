package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.gmail.ramawthar.priyash.responses.ReturnClass;


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
			System.out.println(line);
			StringTokenizer defaultTokenizer = new StringTokenizer(line,"<*>");
			while (defaultTokenizer.hasMoreTokens()){
				token = defaultTokenizer.nextToken();
			}
			rScript = token;
			System.out.println( "script: "+rScript);
			
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
				System.out.println("token: "+token);
			}
			if (!(instrumentMetaData.getInstrumentName().equalsIgnoreCase(""))){
				instrumentData.add(instrumentMetaData);		
			}
		}
		
		rc.setStatus("Success");
		return rc;
	}
	
	public ReturnClass processInstrumentData(){
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running on default port
             * 6311
             */
            connection = new RConnection();
            
            
            
    		//for (int i = 0; i < instrumentData.size(); i++) {
    		//	System.out.println(instrumentData.get(i).getFileLine());
            
	            connection.eval("inFile='C:/Users/priyash.ramawthar/Dropbox/trader/appData/masterdata/ADI.txt'");
	            connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/appData/masterdata/output/ADI_proc.txt'");
				connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/appData/scripts/ImportProcessExportParams.r')");
	            
	            //connection.eval("inFile='C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/spy_historical_data.txt'");
	            //connection.eval("outFile='C:/Users/priyash.ramawthar/Dropbox/trader/spy_with_indicators.csv'");
	            //connection.eval("source('C:/Users/priyash.ramawthar/Dropbox/trader/R_scripts/ImportProcessExportParams.r')");
            
            

    			
                //connection.eval("inFile='"+instrumentData.get(i).getInFile()+"'");
                //connection.eval("outFile='"+instrumentData.get(i).getOutFile()+"'");
                //connection.eval("source('"+rScript+"')");
    		//}
            
        } catch (RserveException e) {
            e.printStackTrace();
        }

		rc.setStatus("Success");
		return rc;
	}
	
	public ReturnClass writeInstrumentMetaData(){
		
		for (int i = 0; i < instrumentData.size(); i++) {
			System.out.println(instrumentData.get(i).getFileLine());
		}

		rc.setStatus("Success");
		return rc;
	}
}
