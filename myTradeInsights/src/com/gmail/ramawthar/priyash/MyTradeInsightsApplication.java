package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.AnalyseProcessedData;
import com.gmail.ramawthar.priyash.logic.SetupTestProcessedData;

public class MyTradeInsightsApplication {
	
	public static void main(String [] args){
		System.out.println("Lets begin");
		
		//*********for testing:	
		/*// Step 1.
		SetupTestProcessedData sd = new SetupTestProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt");
		sd.readMetaData("priyash", "C:\\Users\\Priyash\\Dropbox\\trader\\appData\\masterdata\\test_files\\testInsightsFiles\\");
		sd.createTestData("\"29-Jun-17\"");
		*/
		
		  // Step 2. need to copy test files from:
		
		//C:\Users\Priyash\Dropbox\trader\appData\masterdata\test_files\testInsightsFiles
		//to the test directory:
		//C:\Users\priyashteststart\Dropbox\trader\appData\masterdata\output
		/*
		 * for test start date to create insights and predications:
		 */
		
		  //Step 3
		
		/**/
		AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
				   										   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\insightMetaData.txt",
				   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\myShares.txt");

		ad.readMetaData("priyashteststart");
		ad.createInsights();
		
		//*********for testing.	


		//*********for production:
		//
		//AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt",
		//		   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\insightMetaData.txt",
		//		   "C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\myShares.txt");
		
		//ad.readMetaData("priyash");
		//ad.createInsights();
		//
		//*********for production.

		
	}

}
//C:\Users\Priyash\Dropbox\trader\appData\masterdata\output
//C:\Users\priyashteststart\Dropbox\trader\appData\masterdata\output