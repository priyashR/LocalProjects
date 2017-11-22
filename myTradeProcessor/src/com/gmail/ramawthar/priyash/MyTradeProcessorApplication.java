package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.PrepareData;
import com.gmail.ramawthar.priyash.logic.ProcessData;

/**
 * @author Priyash.Ramawthar
 *
 */
//
//in R:
//load the lib -> 	library(Rserve)
//start the server ->	Rserve()
public class MyTradeProcessorApplication {
	
	public void prepDataOnly(){
		System.out.println("prepare the files for R");
		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
										 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
		
		System.out.println("Process new data: "+pd.processNewData().getStatus());
	}
	
	public void uploadToCloudOnly(){
		ProcessData processData = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				  "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		
		processData.readMetaData();
		//System.out.println("Call rScript - result: "+processData.processInstrumentData().getStatus());
		processData.writeIntrumentDataToCloud();
		//System.out.println("write process meatdata to cloud: " + processData.writeInstrumentMetaData().getStatus());
	}
	
	public void processOnly(){
		ProcessData processData = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				  "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		
		System.out.println("Read meatdata: "+processData.readMetaData().getStatus());
		System.out.println("Call rScript - result: "+processData.processInstrumentData().getStatus());
		//System.out.println("write instrument data to cloud: " + processData.writeIntrumentDataToCloud().getStatus());
		//System.out.println("write process meatdata to cloud: " + processData.writeInstrumentMetaData().getStatus());
	}
	
	public void processAndUploadToCloudOnly(){
		ProcessData processData = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				  "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		
		System.out.println("Read meatdata: "+processData.readMetaData().getStatus());
		System.out.println("Call rScript - result: "+processData.processInstrumentData().getStatus());
		System.out.println("write instrument data to cloud: " + processData.writeIntrumentDataToCloud().getStatus());
		System.out.println("write process meatdata to cloud: " + processData.writeInstrumentMetaData().getStatus());
	}
	
	public void runFullProcess(){
		System.out.println("prepare the files for R");
		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
										 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
		
		System.out.println("Process new data: "+pd.processNewData().getStatus());
		
		ProcessData processData = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
												  "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		
		System.out.println("Read meatdata: "+processData.readMetaData().getStatus());
		System.out.println("Call rScript - result: "+processData.processInstrumentData().getStatus());
		System.out.println("write instrument data to cloud: " + processData.writeIntrumentDataToCloud().getStatus());
		System.out.println("write process meatdata to cloud: " + processData.writeInstrumentMetaData().getStatus());
		
		
		
		// try this to ensure all share data is sent to the cloud:
		// to ensure all processing jobs are finished before calling the cloud service
		// after every instrument processing issue a plus one to some r variable and write it to file
		// at the start of processing set the file value to 0 
		// once the file file value reaches the number of instruments - then start pushing to cloud
	}
	public static void main(String[] args){

		MyTradeProcessorApplication app = new MyTradeProcessorApplication();
		
		app.runFullProcess();
		//app.processOnly();
		//app.uploadToCloudOnly();
		
	}
}
