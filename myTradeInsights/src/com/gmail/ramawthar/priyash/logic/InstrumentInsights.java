package com.gmail.ramawthar.priyash.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class InstrumentInsights {
	
	//Insights:
	// To check how far from turning point we are
	public Insight I001a(ArrayList<ProcessedInstrumentData> data){

		Insight i001 = new Insight();
		
		int numberOfLines = data.size();
		
		if (numberOfLines <= 0)
			return i001;
		
		int lastLine = numberOfLines - 1;
		
		BigDecimal SMA5 = data.get(lastLine).getLongSma5();
		BigDecimal SMA20 = data.get(lastLine).getBigDecimalSma20();
		/*
		 * 1. Get what percentage SMA5 is of SMA20
		 * 2. If its:
		 * 		>110 then insightValue =5 
		 * 		>105 then insightValue =4 
		 * 		>103 then insightValue =3 
		 * 		>102 then insightValue =2 
		 * 		>101 then insightValue =1 
		 * 		<= 100 or >100 then insightValue =0 
		 * 		< 90 then insightValue =-5 
		 * 		< 95 then insightValue =-4 
		 * 		< 97 then insightValue =-3 
		 * 		< 98 then insightValue =-2 
		 * 		< 99 then insightValue =-1 
		 */
		Double percentage = getPercent(SMA20, SMA5);
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		if ((percentage > 110.00)) {
			insightValue = "-5";
			insightNote = "SMA5 is much higher than SMA20";
			insightRec = "Monitor";
		}else if ((percentage > 105.00)){
			insightValue = "-4";
			insightNote = "SMA5 is higher than SMA20";
			insightRec = "Monitor";
		}else if ((percentage > 103.00)){
			insightValue = "-3";
			insightNote = "SMA5 is approaching SMA20";
			insightRec = "Monitor";
		}else if ((percentage > 102.00)){
			insightValue = "-2";
			insightNote = "SMA5 is moving close to SMA20";
			insightRec = "Monitor";
		}else if ((percentage > 101.00)){
			insightValue = "-1";
			insightNote = "SMA5 is almost touching SMA20";
			insightRec = "Analyse";
		}else if ((percentage >= 100.00)){
			insightValue = "0";
			insightNote = "SMA5 is crossing SMA20";
			insightRec = "Consider BUY or SELL - check the price direction";
		}else if ((percentage < 90.00)) {
			insightValue = "5";
			insightNote = "SMA5 is much lower than SMA20";
			insightRec = "Monitor";
		}else if ((percentage < 95.00)){
			insightValue = "4";
			insightNote = "SMA5 is lower than SMA20";
			insightRec = "Monitor";
		}else if ((percentage < 97.00)){
			insightValue = "3";
			insightNote = "SMA5 is approaching SMA20";
			insightRec = "Monitor";
		}else if ((percentage < 98.00)){
			insightValue = "2";
			insightNote = "SMA5 is moving close to SMA20";
			insightRec = "Monitor";
		}else if ((percentage < 99.00)){
			insightValue = "1";
			insightNote = "SMA5 is almost touching SMA20";
			insightRec = "Analyse";
		}else if ((percentage <= 100.00)){
			insightValue = "0";
			insightNote = "SMA5 is crossing SMA20";
			insightRec = "Consider BUY or SELL - check the price direction";
		}
		
		i001.setInstrument(data.get(lastLine).getInstrument());
		i001.setInsightCode("I001a");
		i001.setInsightDesc("SMA5 crossover SMA20");
		i001.setDate(data.get(lastLine).getDate());
		i001.setInsightValue(insightValue);
		i001.setInsightNote(insightNote);
		i001.setInsightRec(insightRec);
		
		return i001;
	}

	// To get the distance away from the last turning point
	public Insight I001b(ArrayList<ProcessedInstrumentData> data){

		Insight i001b = new Insight();
		
		int numberOfLines = data.size();
		
		//number of lines in the file
		if (numberOfLines < 20)//we want to use 20 lines
			return i001b;
		//number of lines used from file
		numberOfLines = numberOfLines - 1;//we're gona use this as indexes - 0-19
		Double percentage = 0.0;
		BigDecimal SMA5 = null;
		BigDecimal SMA20 = null;
		int index = numberOfLines;
		for (int i = 0;i < 20; i++){
			index = numberOfLines - i;
			SMA5 = data.get(index).getLongSma5();
			SMA20 = data.get(index).getBigDecimalSma20();
			percentage = getPercent(SMA20, SMA5);
			if ((percentage >= 99.00)&&(percentage < 101.00)){
				index = i;
				i = 22;
			}
		}

		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		if (index == 19) {
			insightValue = "5";
			insightNote = "Last crossover occured very 19 or more trading days ago";
			insightRec = "";
		}else if (index > 15){
			insightValue = "4";
			insightNote = "Last crossover occured between 16 and 19 trading days ago";
			insightRec = "";
		}else if (index > 10){
			insightValue = "3";
			insightNote = "Last crossover occured between 11 and 15 trading days ago";
			insightRec = "";
		}else if (index > 6){
			insightValue = "2";
			insightNote = "Last crossover occured between 7 and 10 trading days ago";
			insightRec = "";
		}else if (index > 3){
			insightValue = "1";
			insightNote = "Last crossover occured between 4 and 6 trading days ago";
			insightRec = "";
		}else if (index >= 0){
			insightValue = "0";
			insightNote = "Last crossover occured between 0 and 3 trading days ago";
			insightRec = "";
		}
		i001b.setInstrument(data.get(0).getInstrument());
		i001b.setInsightCode("I001b");
		i001b.setInsightDesc("Last crossover of SMA5 on SMA20");
		i001b.setDate(data.get(0).getDate());
		i001b.setInsightValue(insightValue);
		i001b.setInsightNote(insightNote);
		i001b.setInsightRec(insightRec);
		//System.out.println(insightNote); 
		return i001b;
	}	
	
	// % difference between SMA5 and SMA20
	public Insight I002a(ArrayList<ProcessedInstrumentData> data){

		Insight i002a = new Insight();
		
		int numberOfLines = data.size();
		
		if (numberOfLines <= 0)
			return i002a;
		
		int lastLine = numberOfLines - 1;
		
		BigDecimal SMA5 = data.get(lastLine).getLongSma5();
		BigDecimal SMA20 = data.get(lastLine).getBigDecimalSma20();

		Double percentage = getPercent(SMA20, SMA5);
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		insightValue = (Math.round((percentage - 100) * 100.0) / 100.0) + "";
		insightNote = "Percentage difference between SMA5 and SMA20";
		insightRec = "N/A";
		
		i002a.setInstrument(data.get(lastLine).getInstrument());
		i002a.setInsightCode("I002");
		i002a.setInsightDesc("SMA5 crossover SMA20");
		i002a.setDate(data.get(lastLine).getDate());
		i002a.setInsightValue(insightValue);
		i002a.setInsightNote(insightNote);
		i002a.setInsightRec(insightRec);
		
		return i002a;
	}	
	
	public Double getPercent(BigDecimal val1, BigDecimal val2){
		
		Double percent = (val2.divide(val1,4,RoundingMode.HALF_UP)).doubleValue()*100;
		return Math.round(percent * 100D) / 100D;
	}

	public Double getPercent(Long val1, BigDecimal val2){
		BigDecimal bigVal1 = new BigDecimal(val1);
		Double percent = (val2.divide(bigVal1,4,RoundingMode.HALF_UP)).doubleValue()*100;
		return Math.round(percent * 100D) / 100D;
	}

}
