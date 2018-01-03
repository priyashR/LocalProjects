package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.AnalyseProcessedData;

public class MyTradeInsightsApplication {
	
	public static void main(String [] args){
		
		AnalyseProcessedData ad = new AnalyseProcessedData("C:\\Users\\priyash\\Dropbox\\trader\\appData\\metaData\\instrumentsMetaData.txt");
		ad.readMetaData("priyash");
		ad.createInsights();
	}

}
