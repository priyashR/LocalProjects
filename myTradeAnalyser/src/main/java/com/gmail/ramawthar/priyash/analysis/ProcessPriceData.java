package com.gmail.ramawthar.priyash.analysis;

import java.util.Date;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class ProcessPriceData {
	
	private Instrument instrument;
	private Formula formula;
	private Date start;
	private Date end;
	
	
	
	public ProcessPriceData(Instrument instrument, Formula formula, Date start, Date end) {
		super();
		this.instrument = instrument;
		this.formula = formula;
		this.start = start;
		this.end = end;
	}

	public ProcessPriceData(Instrument instrument, Formula formula) {
		super();
		this.instrument = instrument;
		this.formula = formula;
	}

	public ProcessPriceData() {
		super();
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
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
	
	public ReturnClass process(){

		InputInstrumentData iid = new InputInstrumentData(instrument,start,end);
		iid.downloadData();
		
		ApplyFormulae af = new ApplyFormulae(formula);
		ReturnClass rc = af.execute(iid);
		
		//check state maybe
		return rc;
	}
	

}
