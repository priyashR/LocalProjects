package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.AnalyseProcessedData;
import com.gmail.ramawthar.priyash.logic.SetupTestProcessedData;

public class MyTradeInsightsApplication {
	
	public static void main(String [] args){
		System.out.println("Lets begin");
		//*********for production:
		//
		
		
		 //windows
		  String uncopied = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\insights";
		  String done = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\insights\\done";
		  String err = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\insights\\err";
		  
		  String instrumentMetaData = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt"; 
		  String insightMetaData = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\insightMetaData.txt"; 
		  String myShares = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\metaData\\myShares.txt"; 
		  
		  String instance = "priyash.ramawthar";
		
		
		/* 
		 //Ubuntu
		  String uncopied = "/home/priyash/Dropbox/trader/appData/masterdata/insights";
		  String done = "/home/priyash/Dropbox/trader/appData/masterdata/insights/done";
		  String err = "/home/priyash/Dropbox/trader/appData/masterdata/insights/err";
		  
		  String instrumentMetaData = "/home/priyash/Dropbox/trader/appData/metaData/instrumentsMetaData.txt"; 
		  String insightMetaData = "/home/priyash/Dropbox/trader/appData/metaData/insightMetaData.txt"; 
		  String myShares = "/home/priyash/Dropbox/trader/appData/metaData/myShares.txt"; 
		  
		  String instance = "priyash";
		  */
		//*********for production.
		AnalyseProcessedData ad = new AnalyseProcessedData(instrumentMetaData,
				   											 insightMetaData,
				   											 myShares);
		
		ad.readMetaData(instance);
		ad.createInsights(uncopied, 
							done, 
							err);
		
		//*********for production.

		
	}

}
//C:\Users\Priyash\Dropbox\trader\appData\masterdata\output
//C:\Users\priyashteststart\Dropbox\trader\appData\masterdata\output