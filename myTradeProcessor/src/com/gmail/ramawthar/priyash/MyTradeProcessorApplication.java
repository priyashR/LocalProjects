package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.logic.PrepareData;
import com.gmail.ramawthar.priyash.logic.RemoveFiles;

/**
 * @author Priyash.Ramawthar
 *
 */
public class MyTradeProcessorApplication {
	public static void main(String[] args){
		
		System.out.println("prepare the files for R");
		PrepareData pd = new PrepareData("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\processed", 
				 						 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\error",
										 "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\");
		System.out.println(pd.processNewData().getStatus());
		
		//RemoveFiles rf = new RemoveFiles("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\watchlist\\new");
		//System.out.println(rf.remove().getStatus());
		
	}
}
