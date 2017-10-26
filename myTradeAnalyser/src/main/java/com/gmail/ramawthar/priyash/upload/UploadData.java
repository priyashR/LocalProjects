package com.gmail.ramawthar.priyash.upload;

import com.gmail.ramawthar.priyash.domain.trade_price_data_stage;
import com.gmail.ramawthar.priyash.repository.Price_data_stageRepository;
import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class UploadData {
	
	ProcessedInstrumentData processedInstrumentData;

	public UploadData(ProcessedInstrumentData processedInstrumentData) {
		super();
		this.processedInstrumentData = processedInstrumentData;
	}
	
	public ReturnClass uploadToDatabase(Price_data_stageRepository price_data_stageRepository){
		
		//Price_data_stage price_data_stage_rec = new Price_data_stage();
				
		//price_data_stageRepository.save(price_data_stage_rec);
		
		System.out.println("Instrument data to be uploaded for: "+processedInstrumentData.getInstrument());
		System.out.println("Close price to be uploaded : "+processedInstrumentData.getClose());
		System.out.println("Close price to be uploaded : "+processedInstrumentData.getDate());
		
		return new ReturnClass("Success");
	}

}
