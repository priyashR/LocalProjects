package com.gmail.ramawthar.priyash.logic;

public class InstrumentMetaData {
	String instrumentName;
	String inFile;
	String outFile;
	String lastProc;
	
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
	
	public String getFileLine(){
		return "<*>"+instrumentName+"<*>"+inFile+"<*>"+outFile+"<*>"+lastProc+"<*>";
	}

}
