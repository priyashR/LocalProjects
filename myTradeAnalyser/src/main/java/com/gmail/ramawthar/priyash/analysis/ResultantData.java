package com.gmail.ramawthar.priyash.analysis;

import com.gmail.ramawthar.priyash.responses.ReturnClass;
import com.gmail.ramawthar.priyash.testing.DisplayData;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ResultantData {

    private double output[];
    private int outputInt[];
    private MInteger outBegIdx; 
    private MInteger outNbElement;
    private RetCode retCode;
    private Instrument instrument;
    private Formula formula;
	
	public ResultantData(Instrument instrument,Formula formula) {
		super();
		DisplayData.displayLine("ResultantData");
		this.instrument = instrument;
		this.formula = formula;
	}	

	public ResultantData(double[] output, int[] outputInt, MInteger outBegIdx, MInteger outNbElement, RetCode retCode) {
		super();
		this.output = output;
		this.outputInt = outputInt;
		this.outBegIdx = outBegIdx;
		this.outNbElement = outNbElement;
		this.retCode = retCode;
	}

	public double[] getOutput() {
		return output;
	}

	public void setOutput(double[] output) {
		this.output = output;
	}

	public int[] getOutputInt() {
		return outputInt;
	}

	public void setOutputInt(int[] outputInt) {
		this.outputInt = outputInt;
	}

	public MInteger getOutBegIdx() {
		return outBegIdx;
	}

	public void setOutBegIdx(MInteger outBegIdx) {
		this.outBegIdx = outBegIdx;
	}

	public MInteger getOutNbElement() {
		return outNbElement;
	}

	public void setOutNbElement(MInteger outNbElement) {
		this.outNbElement = outNbElement;
	}

	public RetCode getRetCode() {
		return retCode;
	}

	public void setRetCode(RetCode retCode) {
		this.retCode = retCode;
	}
	
	public Instrument getInstrumentName() {
		return instrument;
	}

	public void setInstrumentName(Instrument instrument) {
		this.instrument = instrument;
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
	}	
    
	public ReturnClass uploadData(){
		//method to push the data back to the database
		DisplayData.displayLine("ResultantData.uploadData");
		ReturnClass rc = new ReturnClass("success");
		return rc;
	}

	
	
	
    
}
