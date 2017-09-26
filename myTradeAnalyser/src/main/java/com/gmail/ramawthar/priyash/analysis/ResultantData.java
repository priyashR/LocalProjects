package com.gmail.ramawthar.priyash.analysis;

import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ResultantData {

    private double output[];
    private int outputInt[];
    private MInteger outBegIdx; 
    private MInteger outNbElement;
    private RetCode retCode;
    
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
	
	
    
}
