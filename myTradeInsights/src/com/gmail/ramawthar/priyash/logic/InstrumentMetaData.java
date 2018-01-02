package com.gmail.ramawthar.priyash.logic;

public class InstrumentMetaData {
	String instrumentName;
	String inFile;
	String outFile;
	String lastProc;
	String newLastProc;
	boolean processingComplete = false;
	
	public InstrumentMetaData(String instrumentName, String inFile, String outFile, String lastProc){
		this.instrumentName = instrumentName;
		this.inFile = inFile;
		this.outFile = outFile;
		this.lastProc = lastProc;
	}
	public InstrumentMetaData(){
		super();
		this.instrumentName = "";
		this.inFile = "";
		this.outFile = "";
		this.lastProc = "";
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getInFile() {
		return inFile;
	}

	public void setInFile(String inFile) {
		this.inFile = inFile;
	}

	public String getOutFile() {
		return outFile;
	}

	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	public String getLastProc() {
		return lastProc;
	}

	public void setLastProc(String lastProc) {
		this.lastProc = lastProc;
	}
	
	
	
	public String getNewLastProc() {
		return newLastProc;
	}
	public void setNewLastProc(String newLastProc) {
		this.newLastProc = newLastProc;
	}
	public boolean isProcessingComplete() {
		return processingComplete;
	}
	public void setProcessingComplete(boolean processingComplete) {
		this.processingComplete = processingComplete;
	}
	public String getFileLine(){
		if (processingComplete){
			lastProc = newLastProc;
		}
		return "<*>"+instrumentName+"<*>"+inFile+"<*>"+outFile+"<*>"+lastProc+"<*>";
	}

}
