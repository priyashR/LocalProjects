package com.gmail.ramawthar.priyash.analysis;

import java.util.Date;

public class InputInstrumentData {
	
	private String instrumentName;
	private String formula;
    private double input[];
    private int inputInt[];
    private double[] close;
    private Date start;
    private Date end;

    
	public InputInstrumentData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InputInstrumentData(String instrumentName, String formula, double[] input, int[] inputInt, double[] close,
			Date start, Date end) {
		super();
		this.instrumentName = instrumentName;
		this.formula = formula;
		this.input = input;
		this.inputInt = inputInt;
		this.close = close;
		this.start = start;
		this.end = end;
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

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
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
    
	public String loadData(){
		return "success";
	}
    

}
