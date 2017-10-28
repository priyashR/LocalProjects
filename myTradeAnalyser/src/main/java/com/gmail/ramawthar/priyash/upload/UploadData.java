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
		
		trade_price_data_stage price_data_stage_rec = new trade_price_data_stage();
				
		price_data_stage_rec.setInstrument(processedInstrumentData.getInstrument());
		price_data_stage_rec.setTrade_date(processedInstrumentData.getDateDate());
		price_data_stage_rec.setHigh(processedInstrumentData.getLongHigh());
		price_data_stage_rec.setLow(processedInstrumentData.getLongLow());
		price_data_stage_rec.setOpen(processedInstrumentData.getLongOpen());
		price_data_stage_rec.setClose(processedInstrumentData.getLongClose());
		price_data_stage_rec.setSma20(processedInstrumentData.getBigDecimalSma20());
		
		price_data_stage_rec.setVol(processedInstrumentData.getLongVol());
		price_data_stage_rec.setObv(processedInstrumentData.getLongObv());
		price_data_stage_rec.setProcessed_status("Uploaded");
		
		//"sma5","rsi14","macd","signal"
		
		price_data_stage_rec.setSma5(processedInstrumentData.getLongSma5());
		price_data_stage_rec.setRsi14(processedInstrumentData.getLongRsi14());
		price_data_stage_rec.setMacd(processedInstrumentData.getLongMacd());
		price_data_stage_rec.setMacdsig(processedInstrumentData.getLongMacdsig());
		
		price_data_stageRepository.save(price_data_stage_rec);
		
		System.out.println("Instrument data to be uploaded for: "+processedInstrumentData.getInstrument());
		System.out.println("Close price to be uploaded : "+processedInstrumentData.getClose());
		System.out.println("Close price to be uploaded : "+processedInstrumentData.getDate());
		
		return new ReturnClass("Success");
	}

}
