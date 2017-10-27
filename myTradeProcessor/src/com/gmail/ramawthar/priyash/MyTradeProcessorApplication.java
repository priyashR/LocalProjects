package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.PrepareData;
import com.gmail.ramawthar.priyash.logic.ProcessData;

/**
 * @author Priyash.Ramawthar
 *
 */
public class MyTradeProcessorApplication {
	public static void main(String[] args){

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
		System.out.println("write instrument data to could: " + processData.writeIntrumentDataToCloud().getStatus());
		System.out.println("write process meatdata to could: " + processData.writeInstrumentMetaData().getStatus());
		
		
		
		// try this to ensure all share data is sent to the cloud:
		// to ensure all processing jobs are finished before calling the cloud service
		// after every instrument processing issue a plus one to some r variable and write it to file
		// at the start of processing set the file value to 0 
		// once the file file value reaches the number of instruments - then start pushing to cloud
		
	}
}
