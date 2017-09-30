package com.gmail.ramawthar.priyash.testing;

import java.util.Date;

import com.gmail.ramawthar.priyash.analysis.Formula;
import com.gmail.ramawthar.priyash.analysis.Instrument;
import com.gmail.ramawthar.priyash.analysis.ProcessPriceData;
import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class TestPriceAnalysis {
	public ReturnClass testProcesPrice(){
		//type sysout then ctrl+space
		System.out.println("testProcesPrice");
		ProcessPriceData ppd = new ProcessPriceData(Instrument.ADI, Formula.RSI,new Date(),new Date());
		
		ReturnClass r = ppd.process();
		
		return r;
	}
	
	public static void main(String args[]){
		TestPriceAnalysis t = new TestPriceAnalysis();
		ReturnClass r = t.testProcesPrice();
		System.out.println("the call status is: "+r.getStatus());
	}
	
}
