package com.gmail.ramawthar.priyash.analysis;

import java.util.Date;

import com.gmail.ramawthar.priyash.responses.ReturnClass;
import com.gmail.ramawthar.priyash.testing.DisplayData;
import com.gmail.ramawthar.priyash.testing.TestData;

public class InputInstrumentData {
	
	private Instrument instrumentName;
    private double input[];
    private int inputInt[];
    private double[] close;
    private Date start;
    private Date end;
    private boolean loaded = false;

    
	public InputInstrumentData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InputInstrumentData(Instrument instrumentName, Date start, Date end) {
		super();
		DisplayData.displayLine("InputInstrumentData");
		//check start date < end date
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
		//check start date < end date
		this.start = start;
		//re init the class
		this.input = null;
		this.inputInt = null;
		this.close = null;
		this.loaded = false;		
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		//check start date < end date
		this.end = end;
		//re init the class
		this.input = null;
		this.inputInt = null;
		this.close = null;
		this.loaded = false;	
	}

	public Instrument getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(Instrument instrumentName) {
		this.instrumentName = instrumentName;
		//re init the class
		this.input = null;
		this.inputInt = null;
		this.close = null;
		this.start = null;
		this.end = null;
		this.loaded = false;		
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
    
	public boolean isLoaded() {
		return loaded;
	}

	public ReturnClass downloadData(){
		//method to fetch the data from the database
		//check start date < end date
		//List<InputData> I = td.getAllInputData();
		DisplayData.displayLine("InputInstrumentData.downloadData");

		TestData td = new TestData(Instrument.ADI);
		
		
		this.input = td.getDoubleData();
		DisplayData.displayDuobleData(this.input);
		
		loaded = true;
		return new ReturnClass("success");
	}
    

}
