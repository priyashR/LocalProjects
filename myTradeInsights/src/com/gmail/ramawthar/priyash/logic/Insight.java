package com.gmail.ramawthar.priyash.logic;

public class Insight {
	
	private String instrument = ""; 	// ADI
	private String insightCode = ""; 	// I001a
	private String insightDesc = ""; 	// SMA5 crossover SMA20
	private String date = ""; 			// "29-Dec-17"
	private String insightValue = ""; 	// 0 -> can be between 5 and 0, 5 is far from the event 0 is the event 
	private String insightNote = "";	// Crossover reached
	private String insightRec = ""; 	// Possibly BUY/SELL - check the price direction
	
	public Insight(String instrument, String insightCode, String insightDesc, String date, String insightValue,
			String insightNote, String insightRec) {
		super();
		this.instrument = instrument;
		this.insightCode = insightCode;
		this.insightDesc = insightDesc;
		this.date = date;
		this.insightValue = insightValue;
		this.insightNote = insightNote;
		this.insightRec = insightRec;
	}

	public Insight() {
		this.insightCode = "NONE";
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getInsightCode() {
		return insightCode;
	}

	public void setInsightCode(String insightCode) {
		this.insightCode = insightCode;
	}

	public String getInsightDesc() {
		return insightDesc;
	}

	public void setInsightDesc(String insightDesc) {
		this.insightDesc = insightDesc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInsightValue() {
		return insightValue;
	}

	public void setInsightValue(String insightValue) {
		this.insightValue = insightValue;
	}

	public String getInsightNote() {
		return insightNote;
	}

	public void setInsightNote(String insightNote) {
		this.insightNote = insightNote;
	}

	public String getInsightRec() {
		return insightRec;
	}

	public void setInsightRec(String insightRec) {
		this.insightRec = insightRec;
	}
	
	
	

}
