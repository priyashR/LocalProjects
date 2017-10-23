package com.gmail.ramawthar.priyash.analysis;

import com.gmail.ramawthar.priyash.responses.ReturnClass;
import com.gmail.ramawthar.priyash.testing.DisplayData;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class ApplyFormulae {
	
	private Formula formula;
    private Core lib;

	
	public ApplyFormulae(Formula formula) {
		super();
		DisplayData.displayLine("ApplyFormulae");
		this.formula = formula;
	    lib = new Core();
	}

	public Formula getFormula() {
		return formula;
	}
	
	public void setFormula(Formula formula) {
		this.formula = formula;
	}

	public ReturnClass execute(InputInstrumentData iid){
		DisplayData.displayLine("ApplyFormulae.execute");
		ReturnClass rc = new ReturnClass("success");
		ReturnClass rsi = new ReturnClass("success");
		//check if instrument loaded, if not, then load
		if (!iid.isLoaded()){
			rc.setStatus("ERROR");
			rc.setDescription("The input instrument object has not been loaded");
			return rc;
		}
		
		
		//call a formula from the below implementations
		switch (formula){
		   case RSI:
			   rsi = calcRSI(iid, 14, null, null);
			   break;
		   case MA:
			   break;
		   default: break;
		}
		
		
		//return the result
		//ResultantData rd = new ResultantData(iid.getInstrumentName(),formula);
		rc.setRd(rsi.getRd());
		return rc;
		
	}
	
	public ReturnClass calcRSI(InputInstrumentData iid, int lookBack, String rangeFrom, String rangeTo){
		
		ResultantData rd;
        MInteger outBegin = new MInteger();           
        MInteger outNBElem = new MInteger();
        double [] rsiRes = new double[iid.getInput().length];
        
		lib.rsi(0,iid.getInput().length-1,iid.getInput(),lookBack,outBegin,outNBElem,rsiRes);
		
		rd = new ResultantData(rsiRes, outBegin, outNBElem);
		
		ReturnClass rc = new ReturnClass("success");
		rc.setRd(rd);
		
		return rc;
		
	}

	
// write the formula logic here
	
}
