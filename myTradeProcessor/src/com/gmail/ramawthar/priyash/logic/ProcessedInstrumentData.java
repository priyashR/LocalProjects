package com.gmail.ramawthar.priyash.logic;

public class ProcessedInstrumentData {
	private String instrument = "";
	private String open = "";
	private String close = "";
	private String high = "";
	private String low = "";
	
	public ProcessedInstrumentData() {
		super();
	}

	public ProcessedInstrumentData(String instrument, String open, String close, String high, String low) {
		super();
		this.instrument = instrument;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}
	
	public String getJSONFormat(){
		return "test";
	}
	
	

}
