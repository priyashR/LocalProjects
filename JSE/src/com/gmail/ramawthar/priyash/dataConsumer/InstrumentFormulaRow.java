package com.gmail.ramawthar.priyash.dataConsumer;

import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;

public class InstrumentFormulaRow {
    private Map<String, Double> columns = new HashMap<String, Double>();
    private Date date = null;
    public InstrumentFormulaRow() {
        super();
    }
    
    public void setColumnVal(String columnName, Double columnValue){
        columns.put(columnName, columnValue);
    }
    public Double getColumnVal(String columnName){
        if (columns.containsKey(columnName))
            return columns.get(columnName);
        return null;
    }

    public void setDate(String date) {
        try {
            this.date = new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(date)).getTime());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }
}
