package com.gmail.ramawthar.priyash.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
/*
 * Need to make messages more  consistent
 * Need to remove the insights of no value - ie. if it's not applicable or no data, etc.
 * Need to make the insight value more meaningful
 * 
 * 
 * USE Qlik sense to visualize when testing the insights
 */
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
			insightRec = "HOLD";
		}else if ((percentage > 105.00)){
			insightValue = "-4";
			insightNote = "SMA5 is higher than SMA20";
			insightRec = "HOLD";
		}else if ((percentage > 103.00)){
			insightValue = "-3";
			insightNote = "SMA5 is approaching SMA20";
			insightRec = "HOLD";
		}else if ((percentage > 102.00)){
			insightValue = "-2";
			insightNote = "SMA5 is moving close to SMA20";
			insightRec = "consider SELL";
		}else if ((percentage > 101.00)){
			insightValue = "-1";
			insightNote = "SMA5 is almost touching SMA20";
			insightRec = "SELL";
		}else if ((percentage >= 100.00)){
			insightValue = "0";
			insightNote = "SMA5 is crossing SMA20";
			insightRec = "Consider SELL - check the price direction";
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
			insightRec = "Consider BUY";
		}else if ((percentage < 99.00)){
			insightValue = "1";
			insightNote = "SMA5 is almost touching SMA20";
			insightRec = "BUY";
		}else if ((percentage <= 100.00)){
			insightValue = "0";
			insightNote = "SMA5 is crossing SMA20";
			insightRec = "Consider BUY - check the price direction";
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
		i002a.setInsightCode("I002a");
		i002a.setInsightDesc("Percentage difference between SMA5 and SMA20");
		i002a.setDate(data.get(lastLine).getDate());
		i002a.setInsightValue(insightValue);
		i002a.setInsightNote(insightNote);
		i002a.setInsightRec(insightRec);
		
		return i002a;
	}	

	// actual difference between SMA5 and SMA20
	public Insight I002b(ArrayList<ProcessedInstrumentData> data){

		Insight i002b = new Insight();
		
		int numberOfLines = data.size();
		
		if (numberOfLines <= 0)
			return i002b;
		
		int lastLine = numberOfLines - 1;
		
		BigDecimal b = data.get(lastLine).getLongSma5().subtract(data.get(lastLine).getBigDecimalSma20());
		
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		insightValue = b.toString();
		insightNote = "Actual difference between SMA5 and SMA20";
		insightRec = "N/A";
		
		i002b.setInstrument(data.get(lastLine).getInstrument());
		i002b.setInsightCode("I002b");
		i002b.setInsightDesc("Actual difference between SMA5 and SMA20");
		i002b.setDate(data.get(lastLine).getDate());
		i002b.setInsightValue(insightValue);
		i002b.setInsightNote(insightNote);
		i002b.setInsightRec(insightRec);
		
		return i002b;
	}		


	// ROC since the last turning point
	public Insight I002c(ArrayList<ProcessedInstrumentData> data){

		Insight i002c = new Insight();
		
		int numberOfLines = data.size();
		
		//number of lines in the file
		if (numberOfLines < 30)//we want to use 30 lines
			return i002c;
		//number of lines used from file
		numberOfLines = numberOfLines - 1;//we're gona use this as indexes - 0-29
		Double percentage = 0.0;
		BigDecimal SMA5 = null;
		BigDecimal SMA20 = null;
		int index = numberOfLines;
		for (int i = 0;i < 30; i++){
			index = numberOfLines - i;
			SMA5 = data.get(index).getLongSma5();
			SMA20 = data.get(index).getBigDecimalSma20();
			percentage = getPercent(SMA20, SMA5);
			if ((percentage >= 99.00)&&(percentage < 101.00)){
				i = 32;
			}
		}

		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		insightNote = "The rate of change since the last crossover (using SMA5 and SMA20)";
		if (index == (numberOfLines - 29)) 
			insightNote = "The rate of change since 29 days ago";
			
		Double percentageValue = getPercent(data.get(index).getLongClose(), data.get(numberOfLines).getLongClose());
		Double percentageChange = (Math.round((percentageValue - 100) * 100.0) / 100.0);
		
		Integer days = numberOfLines - index;
		
		if (days == 0){
			insightValue = "0";
		}else{
			Double ROC = (Math.round((percentageChange/days) * 100.0) / 100.0);
			insightValue = ROC+"";
		}
		
		insightRec = "N/A";
		
		i002c.setInstrument(data.get(0).getInstrument());
		i002c.setInsightCode("I002c");
		i002c.setInsightDesc("ROC since the last change");
		i002c.setDate(data.get(0).getDate());
		i002c.setInsightValue(insightValue);
		i002c.setInsightNote(insightNote);
		i002c.setInsightRec(insightRec);
		//System.out.println(insightNote); 
		return i002c;
	}	
	
	// % change since the last turning point
	public Insight I002d(ArrayList<ProcessedInstrumentData> data){

		Insight i002d = new Insight();
		
		int numberOfLines = data.size();
		
		//number of lines in the file
		if (numberOfLines < 30)//we want to use 30 lines
			return i002d;
		//number of lines used from file
		numberOfLines = numberOfLines - 1;//we're gona use this as indexes - 0-29
		Double percentage = 0.0;
		BigDecimal SMA5 = null;
		BigDecimal SMA20 = null;
		int index = numberOfLines;
		for (int i = 0;i < 30; i++){
			index = numberOfLines - i;
			SMA5 = data.get(index).getLongSma5();
			SMA20 = data.get(index).getBigDecimalSma20();
			percentage = getPercent(SMA20, SMA5);
			if ((percentage >= 99.00)&&(percentage < 101.00)){
				i = 32;
			}
		}

		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		insightNote = "The percentage change since the last crossover (using SMA5 and SMA20)";
		if (index == (numberOfLines - 29)) 
			insightNote = "The percentage of change since 29 days ago";
			
		Double percentageValue = getPercent(data.get(index).getLongClose(), data.get(numberOfLines).getLongClose());
		Double percentageChange = (Math.round((percentageValue - 100) * 100.0) / 100.0);
		
		insightValue = percentageChange+"";
		
		insightRec = "N/A";
		
		i002d.setInstrument(data.get(0).getInstrument());
		i002d.setInsightCode("I002d");
		i002d.setInsightDesc("% change since the last turing point");
		i002d.setDate(data.get(0).getDate());
		i002d.setInsightValue(insightValue);
		i002d.setInsightNote(insightNote);
		i002d.setInsightRec(insightRec);
		//System.out.println(insightNote); 
		return i002d;
	}	

	// highest high or lowest low - 14 days
	public Insight I004(ArrayList<ProcessedInstrumentData> data){

		Insight i004 = new Insight();
		
		int numberOfLines = data.size();
		
		if (numberOfLines <= 15)
			return i004;
		
		int index = numberOfLines;
		Long highest = data.get(numberOfLines - 1).getLongHigh();
		Long lowest = data.get(numberOfLines - 1).getLongLow();
		
		for (int i = 1;i < 16; i++){
			index = numberOfLines - i;
			if (highest < data.get(index).getLongHigh()){
				highest = data.get(index).getLongHigh();
			}
			if (lowest > data.get(index).getLongLow()){
				lowest = data.get(index).getLongLow();
			}
		}
		
		int lastLine = numberOfLines - 1;
		
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";

		if (data.get(lastLine).getLongClose() > highest){
			insightValue = "0";
			insightNote = "Closing price higher than the higest high in the last 14 days.";
			insightRec = "BUY";
		}else if (data.get(lastLine).getLongClose() < lowest){
			insightValue = "5";
			insightNote = "Closing price lower than the lower low in the last 14 days.";
			insightRec = "SELL";
		}else{
			insightValue = "3";
			insightNote = "Nothing indicated";
			insightRec = "N/A";
		}
		
		i004.setInstrument(data.get(lastLine).getInstrument());
		i004.setInsightCode("I004");
		i004.setInsightDesc("Highest high or lowest low - 14 days");
		i004.setDate(data.get(lastLine).getDate());
		i004.setInsightValue(insightValue);
		i004.setInsightNote(insightNote);
		i004.setInsightRec(insightRec);
		
		return i004;
	}		
	
	// moving average SMA20 turning point
	public Insight I005(ArrayList<ProcessedInstrumentData> data){

		Insight i005 = new Insight();
		
		int thirdIndex = (data.size()-1);
		
		
		if (thirdIndex <= 3)
			return i005;
		
		int secondIndex = thirdIndex - 2;
		int firstIndex = secondIndex - 2;
		
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";

		if (((data.get(thirdIndex).getBigDecimalSma20().compareTo(data.get(secondIndex).getBigDecimalSma20()) == 1)||//greater
			 (data.get(thirdIndex).getBigDecimalSma20().compareTo(data.get(secondIndex).getBigDecimalSma20()) == 0))//or equal
	       &&(data.get(secondIndex).getBigDecimalSma20().compareTo(data.get(firstIndex).getBigDecimalSma20()) == -1)){
			insightValue = "0";
			insightNote = "SMA20 turned to positive from negative";
			insightRec = "BUY";
		}else if (((data.get(thirdIndex).getBigDecimalSma20().compareTo(data.get(secondIndex).getBigDecimalSma20()) == -1)||//less
				   (data.get(thirdIndex).getBigDecimalSma20().compareTo(data.get(secondIndex).getBigDecimalSma20()) == 0))//or equal
			     &&(data.get(secondIndex).getBigDecimalSma20().compareTo(data.get(firstIndex).getBigDecimalSma20()) == 1)){
			insightValue = "5";
			insightNote = "SMA20 turned to negative from positive";
			insightRec = "SELL";
		}else{
			insightValue = "3";
			insightNote = "Nothing indicated";
			insightRec = "N/A";
		}
		
		i005.setInstrument(data.get(thirdIndex).getInstrument());
		i005.setInsightCode("I005");
		i005.setInsightDesc("Moving average SMA20 turning point");
		i005.setDate(data.get(thirdIndex).getDate());
		i005.setInsightValue(insightValue);
		i005.setInsightNote(insightNote);
		i005.setInsightRec(insightRec);
		
		return i005;
	}		
	
	// Three moving averages
	public Insight I006(ArrayList<ProcessedInstrumentData> data){

		Insight i006 = new Insight();
		
		int lastIndex = (data.size()-1);
		
		
		if (lastIndex < 0)
			return i006;
				
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";

		if ((data.get(lastIndex).getBigDecimalSma10().compareTo(data.get(lastIndex).getBigDecimalSma20()) == 1)&&//greater
			(data.get(lastIndex).getLongSma5().compareTo(data.get(lastIndex).getBigDecimalSma20()) == 1)){//and greater
			insightValue = "0";
			insightNote = "SMA5 and SMA10 are bigger than SMA20";
			insightRec = "BUY";
		}else if (data.get(lastIndex).getLongSma5().compareTo(data.get(lastIndex).getBigDecimalSma20()) == -1){//less{
			insightValue = "5";
			insightNote = "SMA5 is less than SMA20";
			insightRec = "SELL";
		}else{
			insightValue = "3";
			insightNote = "Nothing indicated";
			insightRec = "N/A";
		}
		
		i006.setInstrument(data.get(lastIndex).getInstrument());
		i006.setInsightCode("I006");
		i006.setInsightDesc("Three moving averages and turn overs");
		i006.setDate(data.get(lastIndex).getDate());
		i006.setInsightValue(insightValue);
		i006.setInsightNote(insightNote);
		i006.setInsightRec(insightRec);
		
		return i006;
	}		
	
	// Trends and market sentiment against the trend - also check th ADX to confirm the trend
	public Insight I007(ArrayList<ProcessedInstrumentData> data){

		Insight i007 = new Insight();
		
		int lastIndex = (data.size()-1);
		
		if (lastIndex < 5)
			return i007;

		int indexLessFive = (lastIndex-5);
				
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		BigDecimal diffFiveDaysAgo = (data.get(indexLessFive).getLongSma5().subtract(data.get(indexLessFive).getBigDecimalSma20())).abs();
		BigDecimal diffToday = (data.get(lastIndex).getLongSma5().subtract(data.get(lastIndex).getBigDecimalSma20())).abs();
		
		boolean withTrend = true;//equal is with trend
		if (diffFiveDaysAgo.compareTo(diffToday) == 1){
			withTrend = false;			
		}else if (diffFiveDaysAgo.compareTo(diffToday) == -1){
			withTrend = true;
		}

		if ((data.get(lastIndex).getLongClose() > data.get(indexLessFive).getLongClose())&&
			(withTrend)){
			insightValue = "0";
			insightNote = "Market sentiment is with the trend and its going up";
			insightRec = "BUY";
		}else if ((data.get(lastIndex).getLongClose() > data.get(indexLessFive).getLongClose())&&
				  (!withTrend)){
			insightValue = "4";
			insightNote = "Market sentiment is against the trend and its going up";
			insightRec = "Consider SELL";
		}else if ((data.get(lastIndex).getLongClose() < data.get(indexLessFive).getLongClose())&&
				  (withTrend)){
			insightValue = "5";
			insightNote = "Market sentiment is with the trend and its going down";
			insightRec = "SELL";
		}else if ((data.get(lastIndex).getLongClose() < data.get(indexLessFive).getLongClose())&&
				  (!withTrend)){
			insightValue = "2";
			insightNote = "Market sentiment is against the trend and its going down";
			insightRec = "Consider BUY";
		}else{
			insightValue = "3";
			insightNote = "Nothing indicated";
			insightRec = "N/A";
		}
		
		i007.setInstrument(data.get(lastIndex).getInstrument());
		i007.setInsightCode("I007");
		i007.setInsightDesc("Trends and market sentiment against the trend - also check th ADX to confirm the trend");
		i007.setDate(data.get(lastIndex).getDate());
		i007.setInsightValue(insightValue);
		i007.setInsightNote(insightNote);
		i007.setInsightRec(insightRec);
		
		return i007;
	}	
	
	// MACD crossover signal - works best of both positive or negative
	public Insight I008a(ArrayList<ProcessedInstrumentData> data){

		Insight i008a = new Insight();
		
		int lastIndex = data.size();
		
		if (lastIndex < 5)
			return i008a;
		
		lastIndex = lastIndex - 1;
		int secondLastIndex = lastIndex - 1;
				
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		Double percentage = getPercent(data.get(lastIndex).getLongMacdsig(),data.get(lastIndex).getLongMacd());
		Double prevPercentage = getPercent(data.get(secondLastIndex).getLongMacdsig(),data.get(secondLastIndex).getLongMacd());
		
		if ((percentage > 96)&&(percentage < 104)){
			if (prevPercentage < 100){
				insightValue = "0";
				insightNote = "MACD touching the siganl line with the previous day being less than the signal, consider buying if both MACD and signal are positive";
				insightRec = "Consider BUY";
			}else if (prevPercentage > 100){
				insightValue = "5";
				insightNote = "MACD touching the siganl line with the previous day being more than the signal, consider selling";
				insightRec = "SELL";
			}else {
				insightValue = "3";
				insightNote = "MACD touching the siganl line with the previous day being equal or very close to the signal";
				insightRec = "Unknown";
			}
		}
		else{
			insightValue = "-3";
			insightNote = "Nothing indicated";
			insightRec = "N/A";
		}
		
		i008a.setInstrument(data.get(lastIndex).getInstrument());
		i008a.setInsightCode("I008a");
		i008a.setInsightDesc("MACD crossover signal");
		i008a.setDate(data.get(lastIndex).getDate());
		i008a.setInsightValue(insightValue);
		i008a.setInsightNote(insightNote);
		i008a.setInsightRec(insightRec);
		
		return i008a;
	}	
	
	// MACD crossover signal - works best of both positive or negative
	public Insight I008b(ArrayList<ProcessedInstrumentData> data){

		Insight i008b = new Insight();
		
		int lastIndex = data.size();
		
		if (lastIndex < 5)
			return i008b;
		
		lastIndex = lastIndex - 1;
		int secondLastIndex = lastIndex - 2;
				
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		
		if ((data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == 0)
		  &&(data.get(secondLastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacdsig()) == -1)){
			insightValue = "0";
			insightNote = "MACD touching the siganl line with previously being less than the signal, consider buying";
			insightRec = "Consider BUY";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == 0)
				  &&(data.get(secondLastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacdsig()) == 1)){
			insightValue = "5";
			insightNote = "MACD touching the siganl line with previously being more than the signal, consider selling";
			insightRec = "SELL";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == 1)
				  &&(data.get(secondLastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacdsig()) == -1)){
			insightValue = "0";
			insightNote = "MACD crossed the siganl line with previously being less than the signal, consider buying";
			insightRec = "Consider BUY";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == -1)
				  &&(data.get(secondLastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacdsig()) == 1)){
			insightValue = "0";
			insightNote = "MACD crossed the siganl line with previously being more than the signal, consider selling";
			insightRec = "Consider BUY";
		}
		
		i008b.setInstrument(data.get(lastIndex).getInstrument());
		i008b.setInsightCode("I008b");
		i008b.setInsightDesc("MACD crossover signal");
		i008b.setDate(data.get(lastIndex).getDate());
		i008b.setInsightValue(insightValue);
		i008b.setInsightNote(insightNote);
		i008b.setInsightRec(insightRec);
		
		return i008b;
	}	
	// MACD movement and state
	public Insight I008c(ArrayList<ProcessedInstrumentData> data){

		Insight i008c = new Insight();
		
		int lastIndex = data.size();
		
		if (lastIndex < 5)
			return i008c;
		
		lastIndex = lastIndex - 1;
		int secondLastIndex = lastIndex - 2;
				
		String insightValue = "";
		String insightNote = "";
		String insightRec = "";
		
		
		if ((data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == 1)
		  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacd()) == 1)){
			insightValue = "999";
			insightNote = "MACD is positive and went up from before";
			insightRec = "N/A";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == 1)
				  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacd()) == -1)){
			insightValue = "999";
			insightNote = "MACD is positive but went down from before";
			insightRec = "N/A";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == -1)
				  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacd()) == 1)){
			insightValue = "999";
			insightNote = "MACD is negative but went up from before";
			insightRec = "N/A";
		} else if ((data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == -1)
				  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(secondLastIndex).getLongMacd()) == -1)){
			insightValue = "999";
			insightNote = "MACD is negative and went down from before";
			insightRec = "N/A";
		}
		
		i008c.setInstrument(data.get(lastIndex).getInstrument());
		i008c.setInsightCode("I008c");
		i008c.setInsightDesc("MACD movement and state");
		i008c.setDate(data.get(lastIndex).getDate());
		i008c.setInsightValue(insightValue);
		i008c.setInsightNote(insightNote);
		i008c.setInsightRec(insightRec);
		
		return i008c;
	}	
	// Signal movement and state
		public Insight I008d(ArrayList<ProcessedInstrumentData> data){

			Insight i008d = new Insight();
			
			int lastIndex = data.size();
			
			if (lastIndex < 5)
				return i008d;
			
			lastIndex = lastIndex - 1;
			int secondLastIndex = lastIndex - 2;
					
			String insightValue = "";
			String insightNote = "";
			String insightRec = "";
			
			
			if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == 1)
			  &&(data.get(lastIndex).getLongMacdsig().compareTo(data.get(secondLastIndex).getLongMacdsig()) == 1)){
				insightValue = "999";
				insightNote = "Signal is positive and went up from before";
				insightRec = "N/A";
			} else if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == 1)
					  &&(data.get(lastIndex).getLongMacdsig().compareTo(data.get(secondLastIndex).getLongMacdsig()) == -1)){
				insightValue = "999";
				insightNote = "Signal is positive but went down from before";
				insightRec = "N/A";
			} else if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == -1)
					  &&(data.get(lastIndex).getLongMacdsig().compareTo(data.get(secondLastIndex).getLongMacdsig()) == 1)){
				insightValue = "999";
				insightNote = "Signal is negative but went up from before";
				insightRec = "N/A";
			} else if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == -1)
					  &&(data.get(lastIndex).getLongMacdsig().compareTo(data.get(secondLastIndex).getLongMacdsig()) == -1)){
				insightValue = "999";
				insightNote = "Signal is negative and went down from before";
				insightRec = "N/A";
			}
			
			i008d.setInstrument(data.get(lastIndex).getInstrument());
			i008d.setInsightCode("I008d");
			i008d.setInsightDesc("Signal movement and state");
			i008d.setDate(data.get(lastIndex).getDate());
			i008d.setInsightValue(insightValue);
			i008d.setInsightNote(insightNote);
			i008d.setInsightRec(insightRec);
			
			return i008d;
		}
		
		// Check if the MACD and signal are positive
		public Insight I008e(ArrayList<ProcessedInstrumentData> data){

			Insight i008e = new Insight();
			
			int lastIndex = data.size();
			
			if (lastIndex < 5)
				return i008e;
			
			lastIndex = lastIndex - 1;
					
			String insightValue = "";
			String insightNote = "";
			String insightRec = "";
			
			
			if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == 1)
			  &&(data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == 1)
			  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == 1)){
				insightValue = "0";
				insightNote = "MACD and Signal are positive and MACD is higher";
				insightRec = "N/A";
			} else if ((data.get(lastIndex).getLongMacdsig().compareTo(new BigDecimal(0)) == 1)
					  &&(data.get(lastIndex).getLongMacd().compareTo(new BigDecimal(0)) == 1)
					  &&(data.get(lastIndex).getLongMacd().compareTo(data.get(lastIndex).getLongMacdsig()) == -1)){
				insightValue = "0";
				insightNote = "MACD and Signal are positive but MACD is lower";
				insightRec = "N/A";
			} 
			
			i008e.setInstrument(data.get(lastIndex).getInstrument());
			i008e.setInsightCode("I008e");
			i008e.setInsightDesc("Signal and MACD positive check");
			i008e.setDate(data.get(lastIndex).getDate());
			i008e.setInsightValue(insightValue);
			i008e.setInsightNote(insightNote);
			i008e.setInsightRec(insightRec);
			
			return i008e;
		}	

		// Check the RSI
		public Insight I009(ArrayList<ProcessedInstrumentData> data){

			Insight i009 = new Insight();
			
			int lastIndex = data.size();
			
			if (lastIndex < 5)
				return i009;
			
			lastIndex = lastIndex - 1;
					
			String insightValue = "";
			String insightNote = "";
			String insightRec = "";
			
			
			if (data.get(lastIndex).getLongRsi14().compareTo(new BigDecimal(70)) == 1){
				insightValue = "5";
				insightNote = "RSI indicates overbaught";
				insightRec = "Consider SELL";
			} else if (data.get(lastIndex).getLongRsi14().compareTo(new BigDecimal(30)) == -1){
				insightValue = "0";
				insightNote = "RSI indicates oversold";
				insightRec = "Consider BUY";
			} 
			
			i009.setInstrument(data.get(lastIndex).getInstrument());
			i009.setInsightCode("I009");
			i009.setInsightDesc("RSI evaluation");
			i009.setDate(data.get(lastIndex).getDate());
			i009.setInsightValue(insightValue);
			i009.setInsightNote(insightNote);
			i009.setInsightRec(insightRec);
			
			return i009;
		}	
		
		// Check the ADX
		public Insight I010(ArrayList<ProcessedInstrumentData> data){

			Insight i010 = new Insight();
			
			int lastIndex = data.size();
			
			if (lastIndex < 5)
				return i010;
			
			lastIndex = lastIndex - 1;
					
			String insightValue = "";
			String insightNote = "";
			String insightRec = "";
			
			
			if (data.get(lastIndex).getBigAdx().compareTo(new BigDecimal(25)) == 1){
				insightValue = "0";
				insightNote = "ADX indicates currently in trend";
				insightRec = "N/A";
			} else if (data.get(lastIndex).getLongRsi14().compareTo(new BigDecimal(30)) == -1){
				insightValue = "5";
				insightNote = "ADX indicates currently no trend";
				insightRec = "N/A";
			} 
			
			i010.setInstrument(data.get(lastIndex).getInstrument());
			i010.setInsightCode("I010");
			i010.setInsightDesc("ADX trend evaluation");
			i010.setDate(data.get(lastIndex).getDate());
			i010.setInsightValue(insightValue);
			i010.setInsightNote(insightNote);
			i010.setInsightRec(insightRec);
			
			return i010;
		}	
		
		// Check the percentage change from 2 days ago
		public Insight V001_2(ArrayList<ProcessedInstrumentData> data){
			
			return percDiff(data,2);
		}	

		// Check the percentage change from 5 days ago
		public Insight V001_5(ArrayList<ProcessedInstrumentData> data){
			
			return percDiff(data,5);
		}	

		// Check the percentage change from 10 days ago
		public Insight V001_10(ArrayList<ProcessedInstrumentData> data){
			
			return percDiff(data,10);
		}	
		
		// Check the percentage difference
		private Insight percDiff(ArrayList<ProcessedInstrumentData> data, int days){

			Insight v001 = new Insight();
			
			int lastIndex = data.size();
			
			if (lastIndex < 5)
				return v001;
			
			lastIndex = lastIndex - 1;
					
			Double percChange = getPercent(data.get(lastIndex-days).getLongClose(),data.get(lastIndex).getLongClose());
			
			v001.setInstrument(data.get(lastIndex).getInstrument());
			v001.setInsightCode("V001_"+days);
			v001.setInsightDesc("Percentage change since "+days+" days ago");
			v001.setDate(data.get(lastIndex).getDate());
			v001.setInsightValue(percChange.toString());
			v001.setInsightNote("Percentage change since "+days+" days ago");
			v001.setInsightRec("N/A");
			
			return v001;
		}
		
	public Double getPercent(BigDecimal val1, BigDecimal val2){
		if (val1.compareTo(new BigDecimal(0)) == 0){
			return Math.round(0 * 100D) / 100D;
		}
		
		Double percent = (val2.divide(val1,4,RoundingMode.HALF_UP)).doubleValue()*100;
		return Math.round(percent * 100D) / 100D;
	}

	public Double getPercent(Long val1, BigDecimal val2){
		if (val1 == 0){
			return Math.round(0 * 100D) / 100D;
		}
		BigDecimal bigVal1 = new BigDecimal(val1);
		Double percent = (val2.divide(bigVal1,4,RoundingMode.HALF_UP)).doubleValue()*100;
		return Math.round(percent * 100D) / 100D;
	}

	public Double getPercent(Long val1, Long val2){
		if (val1 == 0){
			return Math.round(0 * 100D) / 100D;
		}
		BigDecimal bigVal1 = new BigDecimal(val1);
		BigDecimal bigVal2 = new BigDecimal(val2);
		Double percent = (bigVal2.divide(bigVal1,4,RoundingMode.HALF_UP)).doubleValue()*100;
		return Math.round(percent * 100D) / 100D;
	}

}
