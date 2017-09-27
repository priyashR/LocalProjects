package com.gmail.ramawthar.priyash.analysis;

import java.util.Date;

public class InputInstrumentData {
	
	private Instrument instrumentName;
    private double input[];
    private int inputInt[];
    private double[] close;
    private Date start;
    private Date end;

    
	public InputInstrumentData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InputInstrumentData(Instrument instrumentName, Date start, Date end) {
		super();
		this.instrumentName = instrumentName;
		this.start = start;
		this.end = end;
	}
	
	public InputInstrumentData(Instrument instrumentName) {
		super();
		this.instrumentName = instrumentName;
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Instrument getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(Instrument instrumentName) {
		this.instrumentName = instrumentName;
	}

	public double[] getInput() {
		return input;
	}

	public void setInput(double[] input) {
		this.input = input;
	}

	public int[] getInputInt() {
		return inputInt;
	}

	public void setInputInt(int[] inputInt) {
		this.inputInt = inputInt;
	}

	public double[] getClose() {
		return close;
	}

	public void setClose(double[] close) {
		this.close = close;
	}
    
	public String downloadData(){
		//method to fetch the data from the database
		return "success";
	}
    

}
