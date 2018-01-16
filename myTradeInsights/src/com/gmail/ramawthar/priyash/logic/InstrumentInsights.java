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
		
		String date = data.get(lastLine).getDate();
		String instrument = data.get(lastLine).getInstrument();
		
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
		
		if ((percentage > 110.00)) {
			insightValue = "5";
		}else if ((percentage > 105.00)){
			insightValue = "4";
		}else if ((percentage > 103.00)){
			insightValue = "3";
		}else if ((percentage > 102.00)){
			insightValue = "2";
		}else if ((percentage > 101.00)){
			insightValue = "1";
		}else if ((percentage >= 100.00)){
			insightValue = "0";
		}else if ((percentage < 90.00)) {
			insightValue = "-5";
		}else if ((percentage < 95.00)){
			insightValue = "-4";
		}else if ((percentage < 97.00)){
			insightValue = "-3";
		}else if ((percentage < 98.00)){
			insightValue = "-2";
		}else if ((percentage < 99.00)){
			insightValue = "-1";
		}else if ((percentage <= 100.00)){
			insightValue = "0";
		}
		
		i001.setInstrument(data.get(lastLine).getInstrument());
		i001.setInsightCode("I001a");
		i001.setInsightDesc("SMA5 crossover SMA20");
		i001.setDate(data.get(lastLine).getDate());
		i001.setInsightValue(insightValue);
		i001.setInsightNote("Crossover reached");
		i001.setInsightRec("Possibly BUY/SELL - check the price direction");
		
		return i001;
	}

	// To get an indication of the direction of the price
	public Insight I001b(ArrayList<ProcessedInstrumentData> data){

		Insight i001 = new Insight();
		
		int numberOfLines = data.size();
		
		if (numberOfLines <= 0)
			return i001;
		
		int lastLine = numberOfLines - 1;
		
		String date = data.get(lastLine).getDate();
		String instrument = data.get(lastLine).getInstrument();
		
		BigDecimal SMA5 = data.get(lastLine).getLongSma5();
		BigDecimal SMA20 = data.get(lastLine).getBigDecimalSma20();
		/*
		 * 1. Get what percentage SMA5 is of SMA20
		 * 2. If its < 90 or >110 then
		 */
		
		
		return i001;
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
