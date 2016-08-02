package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;
import com.gmail.ramawthar.priyash.dataLoader.JSEMySqlUtil;

import com.gmail.ramawthar.priyash.dataLoader.JSEUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Instrument {
    private String name = "";
    private JSEUtil util;
    private JSEDAOInt JSEDB;
    private List<InstrumentRow> instrumentHistory = new Vector<InstrumentRow>();
    
    public Instrument(String instrument) {
        this.name = instrument;
        this.JSEDB = new MySqlInstrumentDAO();
        this.util = JSEDB.getUtil();
    }
    public String loadInstrumentHist(String fromDate, String toDate){
    //  if from date is null, then load all - from date format yyyy-mm-dd (eg. 2014-02-07)
        
        if (!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))
        try {
                new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            } catch(Exception e) {
                return "Invalid from date: "+fromDate+" - please enter the date in the format yyyy-MM-dd.";
            }      

        if (!((toDate == null)||(toDate.equalsIgnoreCase(""))))
        try {
                new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch(Exception e) {
                return "Invalid to date: "+toDate+" - please enter the date in the format yyyy-MM-dd.";
            }  
    
        try {
            if (util.checkManifest(this.name.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This instrument("+this.name+") does not exist.");
                return "Invalid instrument: "+this.name;
            }

            ResultSet resultSet = JSEDB.getInstrumentSet(this.name.toUpperCase(), fromDate, toDate);
            InstrumentRow instrumentRow;
            
            while (resultSet.next()){
                instrumentRow = new InstrumentRow();
                instrumentRow.setInstrument(resultSet.getString("instrument"));
                instrumentRow.setName(resultSet.getString("name"));
                instrumentRow.setRulingPrice(resultSet.getString("rulingPrice"));
                instrumentRow.setBid(resultSet.getString("bid"));
                instrumentRow.setBidVol(resultSet.getString("bidVol"));
                instrumentRow.setOffer(resultSet.getString("offer"));
                instrumentRow.setOfferVol(resultSet.getString("offerVol"));
                instrumentRow.setDeals(resultSet.getString("deals"));
                instrumentRow.setHigh(resultSet.getString("high"));
                instrumentRow.setLow(resultSet.getString("low"));
                instrumentRow.setTradedVol(resultSet.getString("tradedVol"));
                instrumentRow.setTradedVal(resultSet.getString("tradedVal"));
                instrumentRow.setVarianceFromPrev(resultSet.getString("varianceFromPrev"));
                instrumentRow.setVarPerc(resultSet.getString("varPerc"));
                instrumentRow.setPreviousRuling(resultSet.getString("previousRuling"));
                instrumentRow.setDate(resultSet.getString("date"));
                instrumentRow.setDY(resultSet.getString("dY"));
                instrumentRow.setEY(resultSet.getString("eY"));
                instrumentRow.setPE(resultSet.getString("pE"));      
                
                instrumentHistory.add(instrumentRow);
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
    public List<InstrumentRow> getInstrumentHistory() {
        return instrumentHistory;
    }

    public String getName() {
        return name;
    }
    
    public int[] getIntRulingPrice(){
        int[] intRulingPrice = new int[instrumentHistory.size()];
        Iterator<InstrumentRow> instrumentData = instrumentHistory.iterator();
        int counter = 0;
        while (instrumentData.hasNext()){
            //System.out.println(instrumentData.next().getRulingPrice()+" indx: "+instrumentHistory.size());
            intRulingPrice[counter++] = instrumentData.next().getRulingPrice();
            
        }        
        return intRulingPrice;
    }
    
    public double[] getDblRulingPrice()    {
        double[] dblRulingPrice = new double[instrumentHistory.size()];
        Iterator<InstrumentRow> instrumentData = instrumentHistory.iterator();
        int counter = 0;
        while (instrumentData.hasNext()){
            //System.out.println(instrumentData.next().getRulingPrice()+" indx: "+instrumentHistory.size());
            dblRulingPrice[counter++] = instrumentData.next().getRulingPrice().doubleValue();            
        }    
        return dblRulingPrice;        
    }
    
    public Integer[][] getOHLCPrice(){
        /*
         * 0-Open
         * 1-High
         * 2-Low
         * 3-Close
         */
        Integer[][] OHLCPrice = new Integer[instrumentHistory.size()][4];
        Iterator<InstrumentRow> instrumentData = instrumentHistory.iterator();
        int counter = 0;
        while (instrumentData.hasNext()){
            //System.out.println(instrumentData.next().getRulingPrice()+" indx: "+instrumentHistory.size());
            OHLCPrice[counter++][0] = instrumentData.next().getPreviousRuling();
            OHLCPrice[counter++][1] = instrumentData.next().getHigh();
            OHLCPrice[counter++][2] = instrumentData.next().getLow();
            OHLCPrice[counter++][3] = instrumentData.next().getRulingPrice();
            OHLCPrice[counter++][4] = instrumentData.next().getTradedVol().intValue();
            
        }         
        return OHLCPrice;
    }
    
    public Date[] getDates(){
        Date[] dates = new Date[instrumentHistory.size()];
        Iterator<InstrumentRow> instrumentData = instrumentHistory.iterator();
        int counter = 0;
        while (instrumentData.hasNext()){
            dates[counter++] = instrumentData.next().getDate();
        }        
        return dates;
    }    
    
    public String [] getStringDates(){
        String [] dates = new String[instrumentHistory.size()];
        Iterator<InstrumentRow> instrumentData = instrumentHistory.iterator();
        int counter = 0;
        while (instrumentData.hasNext()){
            dates[counter++] = instrumentData.next().getDate().toString();
        }        
        return dates;
    }    
    
    public String postToFormulaeTable(String formulae, double[] valuesToPost, int offset, boolean checkMax){
        boolean maxflag = checkMax;
        //check if the counts are the same bewteen the instrument History collection and the valuesToPost array
        //if ((valuesToPost.length) != instrumentHistory.size()){
        //    return "There is a mismatch in sizes between the array input and the instrument array. Input array ="+valuesToPost.length+" and current array ="+instrumentHistory.size();
        //}
        //check if the formula column exist on the manifest of formulae
        try {
            if (util.checkFormulaeManifest(formulae.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This formulae("+formulae+") does not exist.");
                return "Invalid formulae: "+formulae;
            }  
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "Instrument.postToFormulaeTable: "+e.getMessage());
            return e.getMessage();
        }
        

        Date maxDate = null;
        //get the max date for this function
        if (checkMax){
            String date = JSEDB.getMaxFormulaDate(this.name, formulae);
            if ((date == null)||(date.equalsIgnoreCase(""))){
                maxflag = false; 
            }else{        
                try {        
                    maxDate = new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(date)).getTime());
                    } catch(Exception e) {
                        return "Invalid date: "+date;
                    }  
            }
        }
        //get the date array
        Date [] currDates = getDates();
        for (int i= 0; i<instrumentHistory.size()-offset;i++){
            System.out.println("this.name: "+this.name+" formulae: "+formulae+" For date: "+currDates[i+offset].toString());
            if (maxflag){
                if (maxDate.compareTo(currDates[i+offset])<0)
                    System.out.println(JSEDB.postDataToFormulaeTable(this.name, formulae, currDates[i+offset].toString(), valuesToPost[i]));
            } else{
                System.out.println(JSEDB.postDataToFormulaeTable(this.name, formulae, currDates[i+offset].toString(), valuesToPost[i]));
            }
            
        }        
        //use the date array to post the data in the valuesToPost array to the current instruments formula table for the input formula
        return "SUCCESS";
    }
    
    public String postToFormulaeTable(String formulae, double[] valuesToPost, int offset){
        return postToFormulaeTable(formulae, valuesToPost, offset,true);
    }

    public String postToFormulaeTable(String formulae, int[] valuesToPost, int offset, boolean checkMax){
        boolean maxflag = checkMax;
        //check if the counts are the same bewteen the instrument History collection and the valuesToPost array
        //if ((valuesToPost.length-offset) > instrumentHistory.size()){
        //    return "There is a mismatch in sizes between the array input and the instrument array. Input array ="+valuesToPost.length+" and current array ="+instrumentHistory.size();
        //}
        //check if the formula column exist on the manifest of formulae
        try {
            if (util.checkFormulaeManifest(formulae.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This formulae("+formulae+") does not exist.");
                return "Invalid formulae: "+formulae;
            }  
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "Instrument.postToFormulaeTable: "+e.getMessage());
            return e.getMessage();
        }
        

        Date maxDate = null;
        //get the max date for this function
        if (checkMax){
            String date = JSEDB.getMaxFormulaDate(this.name, formulae);
            if ((date == null)||(date.equalsIgnoreCase(""))){
                maxflag = false; 
            }else{        
                try {        
                    maxDate = new java.sql.Date((new SimpleDateFormat("yyyy-MM-dd").parse(date)).getTime());
                    } catch(Exception e) {
                        return "Invalid date: "+date;
                    }  
            }
        }
        //get the date array
        Date [] currDates = getDates();
        for (int i= 0; i<valuesToPost.length-offset;i++){
            System.out.println("this.name: "+this.name+" formulae: "+formulae+" For date: "+currDates[i].toString());
            if (maxflag){
                if (maxDate.compareTo(currDates[i+offset])<0)
                    System.out.println(JSEDB.postDataToFormulaeTable(this.name, formulae, currDates[i+offset].toString(), valuesToPost[i]));
            } else{
                System.out.println(JSEDB.postDataToFormulaeTable(this.name, formulae, currDates[i+offset].toString(), valuesToPost[i]));
            }
            
        }        
        //use the date array to post the data in the valuesToPost array to the current instruments formula table for the input formula
        return "SUCCESS";
    }
    
    public String postToFormulaeTable(String formulae, int[] valuesToPost, int offset){
        return postToFormulaeTable(formulae, valuesToPost, offset,true);
    }  
    
    public InstrumentGraphData getRulingPricegraphData(){
        double [] rP = getDblRulingPrice();
        Date[] dates = getDates();
        InstrumentGraphDataLine [] dateLines = new InstrumentGraphDataLine[rP.length];
        for (int i= 0; i<rP.length;i++){
            //System.out.println("Index: "+i+" value: "+rP[i]+" For date: "+dates[i]);
            dateLines[i] = new InstrumentGraphDataLine(dates[i], rP[i], 0,0,0,0,0, GraphLineTypes.RULINGPRICE);
        }      
        InstrumentGraphData rulingGraphData = new InstrumentGraphData(dateLines, getName(), getName(), "Date", "Ruling Price", true, true, false, DatasetTypes.RULINGPRICE);
        return rulingGraphData;
    }

    
    public static void main(String [] args){
        Instrument ADI = new Instrument("ADI");
        System.out.println(ADI.loadInstrumentHist("2014-02-07","2014-05-26"));
        //ADI.getIntRulingPrice();
        Iterator<InstrumentRow> instrumentData = ADI.getInstrumentHistory().iterator();
        //while (instrumentData.hasNext()){
            //System.out.println(instrumentData.next());
        //}
        int[] rP = ADI.getIntRulingPrice();
        Date[] dates = ADI.getDates();
        
        for (int i= 0; i<rP.length;i++){
            System.out.println("Index: "+i+" value: "+rP[i]+" For date: "+dates[i]);
        }
        
    }
    
    public double[] getInstrumentData(DatasetTypes instrumentDataType)    {
        //DblRulingPrice 
        
        if (instrumentDataType.equals(DatasetTypes.RULINGPRICE))
            return getDblRulingPrice();
        
        return null;
    }
}
