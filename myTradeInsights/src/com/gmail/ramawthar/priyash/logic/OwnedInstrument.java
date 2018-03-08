package com.gmail.ramawthar.priyash.logic;

public class OwnedInstrument {

	private String instrument;
	private Double closePrice;
	
	
	public OwnedInstrument() {
		super();
	}
	
	public OwnedInstrument(String instrument, Double closePrice) {
		super();
		this.instrument = instrument;
		this.closePrice = closePrice;
	}

	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public Double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}
	
	 
}
