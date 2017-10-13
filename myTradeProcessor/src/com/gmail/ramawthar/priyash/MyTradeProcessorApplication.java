package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.PrepareData;

/**
 * @author Priyash.Ramawthar
 *
 */
public class MyTradeProcessorApplication {
	public static void main(String[] args){
		System.out.println("prepare the files for R");
		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata");
		System.out.println(pd.processNewData().getStatus());
		
	}
}
