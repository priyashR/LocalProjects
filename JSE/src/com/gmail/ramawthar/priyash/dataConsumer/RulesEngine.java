package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;

import java.sql.Date;

import java.sql.ResultSet;

import java.util.HashMap;
import java.util.Map;

public class RulesEngine {
    private static final String RSI = "RSI";
    private static final String RSI14 = "RSI14";
    
    private Map<String, Integer> ratingData = new HashMap<String, Integer>();
    private JSEDAOInt JSEDBInstrumentFormula;
    private JSEDAOInt JSEDBInstruments;
    public RulesEngine() {
        JSEDBInstrumentFormula = new MySqlInstrumentFormulaDAO();
        JSEDBInstruments = new MySqlInstrumentDAO();
    }
    private Integer getRSIrating(String instrument){
        return 5;
    }
    private Integer getRSI14rating(String instrument){
        return 5;
    }
    private String getCurrentDate(String instrument){
        //TODO - get max date for the current instrument
        return "21-MAY-2015";
    }
    private String calcRankings() {
        //TOD - calculate the rankings of all current instruments ratings
        return "Success";
    }
    public String generateRatings(){
        ResultSet formulae = JSEDBInstrumentFormula.getManifestSet();
        ResultSet instruments = JSEDBInstruments.getManifestSet();
        try {
            while (instruments.next()){
            String currDate = getCurrentDate(instruments.getString("Instrument"));
                while (formulae.next()){
                    if (formulae.getString("dynamic").equalsIgnoreCase("N")){
                        Integer rating = -1;
                        //You MUST cover all instruments in the formula manifest so you dont mess up the data inserted later, and all data is accurate
                        if (formulae.getString("formulae_column").equalsIgnoreCase(RSI)){
                            rating = getRSIrating(formulae.getString("formulae_column"));
                        }else if(formulae.getString("formulae_column").equalsIgnoreCase(RSI14)){
                            rating = getRSI14rating(formulae.getString("formulae_column"));
                        }
                        
                        ratingData = new HashMap<String, Integer>();
                        ratingData.put(formulae.getString("formulae_column").toUpperCase(), rating);
                    }
                    JSEDBInstrumentFormula.updateFormulaRatings(instruments.getString("Instrument"), ratingData,currDate);
                }
                
            }
            
        instruments.close();   
        formulae.close();
        }catch(Exception e){
            try{
            if (!(formulae.isClosed())){
                    formulae.close();
                }
            if (!(instruments.isClosed())){
                    instruments.close();
                }                
            }catch(Exception ef){
                    
                }
            (JSEDBInstrumentFormula.getUtil().getEx()).addLog(ExceptionTypes.ERROR.toString(), "RulesEngine.generateRatings: "+e.getMessage());
            return e.getMessage();
        }
        calcRankings();
        return "Success";
    }
}
