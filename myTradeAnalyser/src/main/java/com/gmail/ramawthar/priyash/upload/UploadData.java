package com.gmail.ramawthar.priyash.upload;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class UploadData {
	
	InstrumentData instrumentdata;

	public UploadData(InstrumentData instrumentdata) {
		super();
		this.instrumentdata = instrumentdata;
	}
	
	public ReturnClass uploadToDatabase(){
		
		System.out.println("Instrument data to be uploaded for: "+instrumentdata.getInstrument());
		System.out.println("Close price to be uploaded : "+instrumentdata.getClose());
		
		return new ReturnClass("Success");
	}

}
