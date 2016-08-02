package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.JSEUtil;

import java.sql.ResultSet;

import java.util.Map;

public interface JSEDAOInt {
    public JSEUtil getUtil();
    public ResultSet getInstrumentSet(String instrument, String fromDate, String toDate);
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, int value);
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, double value);
    public String getMaxFormulaDate(String instrument, String formula);
    public ResultSet getManifestSet();
    public String updateFormulaRatings(String instrument, Map<String, Integer> formulaData, String ratingDate);
    public String syncRatingColumns();
}
