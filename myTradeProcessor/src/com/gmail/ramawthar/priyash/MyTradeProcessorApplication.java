package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.PrepareData;
import com.gmail.ramawthar.priyash.logic.ProcessData;

/**
 * @author Priyash.Ramawthar
 *
 */
public class MyTradeProcessorApplication {
	public static void main(String[] args){
		/*
		System.out.println("prepare the files for R");
		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
										 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
		System.out.println(pd.processNewData().getStatus());
		*/
		ProcessData processData = new ProcessData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
												  "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\rsciptMetaData.txt");
		
		processData.readMetaData();

		System.out.println("call the rScript per instrument");
		
		processData.processInstrumentData();
		
		System.out.println("write the data back");
		
		processData.writeInstrumentMetaData();
		
	}
}
