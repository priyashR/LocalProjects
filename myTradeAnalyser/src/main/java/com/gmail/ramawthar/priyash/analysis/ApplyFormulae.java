package com.gmail.ramawthar.priyash.analysis;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class ApplyFormulae {
	
	private Formula formula;
	
	public ApplyFormulae(Formula formula) {
		super();
		this.formula = formula;
	}

	public Formula getFormula() {
		return formula;
	}
	
	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	public ReturnClass execute(InputInstrumentData iid){
		
		ReturnClass rc = new ReturnClass("success");
		//check if instrument loaded, if not, then load
		
		
		//call a formula from the below implementations
		
		//return the result
		ResultantData rd = new ResultantData(iid.getInstrumentName(),formula);
		rc.setRd(rd);
		return rc;
	}

	
// write the formula logic here
	
}
