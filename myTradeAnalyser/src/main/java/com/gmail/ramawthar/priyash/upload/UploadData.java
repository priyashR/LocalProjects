package com.gmail.ramawthar.priyash.upload;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class UploadData {
	
	ProcessedInstrumentData processedInstrumentData;

	public UploadData(ProcessedInstrumentData processedInstrumentData) {
		super();
		this.processedInstrumentData = processedInstrumentData;
	}
	
	public ReturnClass uploadToDatabase(){
		
		System.out.println("Instrument data to be uploaded for: "+processedInstrumentData.getInstrument());
		System.out.println("Close price to be uploaded : "+processedInstrumentData.getClose());
		
		return new ReturnClass("Success");
	}

}
