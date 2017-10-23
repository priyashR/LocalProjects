package com.gmail.ramawthar.priyash.upload;

public class InstrumentData {
	private String instrument = "";
	private String close = "";

	public InstrumentData(String instrument) {
		super();
		this.instrument = instrument;
	}
	
	public InstrumentData() {
		super();
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}
	
	

}
