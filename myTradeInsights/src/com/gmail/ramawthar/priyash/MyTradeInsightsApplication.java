package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.AnalyseProcessedData;
import com.gmail.ramawthar.priyash.logic.SetupTestProcessedData;

public class MyTradeInsightsApplication {
	
	public static void main(String [] args){
		System.out.println("Lets begin");
		//setup test files
		SetupTestProcessedData sd = new SetupTestProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt");
		sd.readMetaData("priyash", "priyashteststart");
		sd.createTestData("\"20-Dec-17\"");
		
		
//		AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt");
		/*
		 * for production:
		 */
//		ad.readMetaData("priyash");
		/*
		 * for test start date to create insights and predications:
		 */
//		ad.readMetaData("priyashteststart");
			
//		ad.createInsights();
	}

}
//C:\Users\Priyash\Dropbox\trader\appData\masterdata\output
//C:\Users\priyashteststart\Dropbox\trader\appData\masterdata\output