package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ComparatorKeys;
import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;
import com.gmail.ramawthar.priyash.dataLoader.InstrumentComparator;
import com.gmail.ramawthar.priyash.dataLoader.JSEUtil;

import java.sql.Date;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class InstrumentFormulaData {
    
    private String name = "";
    private JSEUtil util;
    private JSEDAOInt JSEDB;
    private List<InstrumentFormulaRow> instrumentData = new Vector<InstrumentFormulaRow>();
    private Map<Date, InstrumentFormulaRow> instrumentDataMap = new HashMap<Date, InstrumentFormulaRow>();
    private List<String> columnNames = new Vector<String>();
    private Date fromDate = null;
    private Date toDate = null;
    
    public InstrumentFormulaData(String instrument) {
        this.name = instrument;
        this.JSEDB = new MySqlInstrumentFormulaDAO();
        this.util = JSEDB.getUtil();
        loadColumnNames();
    }
    private void loadColumnNames(){
        ResultSet resultSet = JSEDB.getManifestSet();
        
        try {        
                while (resultSet.next()){
                    if (resultSet.getString("dynamic").equalsIgnoreCase("N"))
                        columnNames.add(resultSet.getString("formulae_column"));      
                }            
            } catch(SQLException se) {
                se.printStackTrace();
                (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "InstrumentFormulaData.loadColumnNames: "+se.getMessage());
            } catch(Exception e) {
                e.printStackTrace();
                (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "InstrumentFormulaData.loadColumnNames: "+e.getMessage());
            }   
    }
    public List<String> getColumnNames() {
        return columnNames;
    }
    
    public String loadInstrumentData(String fromDate, String toDate){
    //  if from date is null, then load all - from date format yyyy-mm-dd (eg. 2014-02-07)
        
        if (!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))
        try {
                new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                this.fromDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(fromDate).getTime());
            } catch(Exception e) {
                return "Invalid from date: "+fromDate+" - please enter the date in the format yyyy-MM-dd.";
            }      

        if (!((toDate == null)||(toDate.equalsIgnoreCase(""))))
        try {
                new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                this.toDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(toDate).getTime());
            } catch(Exception e) {
                return "Invalid to date: "+toDate+" - please enter the date in the format yyyy-MM-dd.";
            }  
    
        try {
            if (util.checkManifest(this.name.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This instrument("+this.name+") does not exist.");
                return "Invalid instrument: "+this.name;
            }

            ResultSet resultSet = JSEDB.getInstrumentSet(this.name.toUpperCase(), fromDate, toDate);
            InstrumentFormulaRow instrumentFormulaRow;
            
            while (resultSet.next()){
                instrumentFormulaRow = new InstrumentFormulaRow();
                instrumentFormulaRow.setDate(resultSet.getString("date"));
                for (String columnName : getColumnNames()){
                    instrumentFormulaRow.setColumnVal(columnName, resultSet.getDouble(columnName));
                }               
                instrumentData.add(instrumentFormulaRow);
                instrumentDataMap.put(instrumentFormulaRow.getDate(),instrumentFormulaRow);
            }
            
            } catch(SQLException se) {
                se.printStackTrace();
                (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "Instrument.Instrument: "+se.getMessage());
                return se.getMessage();
            } catch(Exception e) {
                e.printStackTrace();
                (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "Instrument.Instrument: "+e.getMessage());
                return e.getMessage();
            }         
        return "SUCCESS";
    }    

    public List<InstrumentFormulaRow> getInstrumentData() {
        return instrumentData;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
    
    public InstrumentFormulaRow getDataForDate(Date date){
        if (instrumentDataMap.containsKey(date))
            return instrumentDataMap.get(date);
        return null;
    }

    public String getName() {
        return name;
    }
    
    public InstrumentGraphData getGraphData(String formula){//get graph data for static graphs

        try {
            InstrumentGraphDataLine [] dateLines = new InstrumentGraphDataLine[instrumentData.size()];
            Iterator<InstrumentFormulaRow> instrumentDataIterator = instrumentData.iterator();
            InstrumentGraphData rulingGraphData;
            
            if (!(columnNames.contains(formula))){//(!(util.checkFormulaeManifest(formula).equalsIgnoreCase("Y")))
                (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "InstrumentFormuladata.getGraphData: Formula "+formula+" is not in the column list");
                return null;
            }
                
            int counter = 0;
            while (instrumentDataIterator.hasNext()){
                //System.out.println(instrumentData.next().getRulingPrice()+" indx: "+instrumentHistory.size());
                InstrumentFormulaRow currentRow = instrumentDataIterator.next();
                dateLines[counter++] = new InstrumentGraphDataLine(currentRow.getDate(), currentRow.getColumnVal(formula), 0,0,0,0,0, GraphLineTypes.FORMULA);
                
            }            
            rulingGraphData = new InstrumentGraphData(dateLines, getName(), getName(), "Date", "Ruling Price", true, true, false, DatasetTypes.getDatasetFromString(formula));
            return rulingGraphData;
                
        }catch (Exception e){
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "InstrumentFormuladata.getGraphData: "+e.getMessage());
            return null;
        }            
    }
    
    public static void main(String[] args){
        /*InstrumentFormulaData IFD = new InstrumentFormulaData("ADI");
        for (String columnName : IFD.getColumnNames())
            System.out.println("columnName: "+columnName);
        IFD.loadInstrumentData("2013-12-31", "2014-02-10");

        for (InstrumentFormulaRow instrumentFormulaRow : IFD.getInstrumentData()){
            System.out.println("Date: " + instrumentFormulaRow.getDate());
            Date tomorrow = new Date(0,0,0);
            tomorrow.setTime(instrumentFormulaRow.getDate().getTime()+(24*60*60*1000));
            
            System.out.println(" -> Date + 1: "+tomorrow);
            
            for(String columnName : IFD.getColumnNames())
                System.out.println("columnName: " + columnName+" value: "+instrumentFormulaRow.getColumnVal(columnName));
            
            System.out.println("from date: "+IFD.getFromDate());
            System.out.println("to date: "+IFD.getToDate());
                       
        }
        */
        InstrumentFormulaData ADI = new InstrumentFormulaData("ADI");
        ADI.loadInstrumentData("2013-12-31", "2014-02-10");
        InstrumentFormulaData BMG = new InstrumentFormulaData("BMG");
        BMG.loadInstrumentData("2013-12-31", "2014-02-10");
        InstrumentFormulaData ADT = new InstrumentFormulaData("ADT");
        ADT.loadInstrumentData("2013-12-31", "2014-02-10");
        
        List<InstrumentFormulaData> list = new ArrayList<InstrumentFormulaData>();
        
        list.add(ADI);        
        list.add(BMG);        
        list.add(ADT);
        
       
        
        System.out.println("Before sort: ");
        for( InstrumentFormulaData i : list){
            System.out.println("instrument: "+ i.getName());
        }
        
        InstrumentComparator compPerc = new InstrumentComparator(ComparatorKeys.PERCCHANGE);
        
        Collections.sort(list,compPerc);
        
        System.out.println("After sort: ");
        for( InstrumentFormulaData i : list){
            System.out.println("instrument: "+ i.getName());
        }
        
    }
}
