package com.gmail.ramawthar.priyash.analysis;

public class InputInstrumentData {
	
	private String instrumentName;
	private String formula;
    private double input[];
    private int inputInt[];
    private double[] close;
    
	public InputInstrumentData(String instrumentName, String formula, double[] input, int[] inputInt, double[] close) {
		super();
		this.instrumentName = instrumentName;
		this.formula = formula;
		this.input = input;
		this.inputInt = inputInt;
		this.close = close;
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
    
    

}
