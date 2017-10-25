package com.gmail.ramawthar.priyash.upload;

public class ProcessedInstrumentData {
	private String date = "";
	private String instrument = "";
	private String open = "";
	private String close = "";
	private String high = "";
	private String low = "";
	private String sma20 = "";
	
	public ProcessedInstrumentData() {
		super();
	}

	public ProcessedInstrumentData(String date, String instrument, String open, String close, String high, String low, String sma20) {
		super();
		this.date = date;
		this.instrument = instrument;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
		this.sma20 = sma20;
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
	
	public String getSma20() {
		return sma20;
	}

	public void setSma20(String sma20) {
		this.sma20 = sma20;
	}
	
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJSONFormat(){
		
		//String output = "[{\"instrument\":\"ADI\",\"open\":\"105\",\"close\":\"105\",\"high\":\"105\",\"low\":\"105\",\"sma20\":\"105\"}]";
		String output = "{\"instrument\":\""+instrument+"\",\"date\":"+date+",\"open\":\""+open+"\",\"close\":\""+close+"\",\"high\":\""+high+"\",\"low\":\""+low+"\",\"sma20\":\""+sma20+"\"}";
		return output;
	}

}
