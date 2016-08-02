package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class HistoryCSVRow implements CSVRow {
    public HistoryCSVRow() {
        super();
    }
    
    private String  instrument        ;
    private String  name              ;
    private Integer rulingPrice       ;
    private Integer bid               ;
    private Integer bidVol            ;
    private Integer offer             ;
    private Integer offerVol          ;
    private Integer deals             ;
    private Integer high              ;
    private Integer low               ;
    private Long tradedVol         ;
    private Long tradedVal         ;
    private Integer varianceFromPrev  ;
    private Double  varPerc           ;
    private Integer previousRuling    ;
    private Date    date              ;
    private Double  dY                ;
    private Double  eY                ;
    private Double  pE                ;
    private String batchId;
    private ExceptionLogger ex;
    
    @Override
    public void constructFromStrings(List<String> values, ExceptionLogger ex) {     
        this.ex = ex;    
        ex.infoLog("in construct(date): "+values.get(0));
        if ((values.get(0)).equalsIgnoreCase("Date")){
            this.instrument = "HEADER_RECORD";
            return;
        }

        ex.infoLog("Price: "+Integer.parseInt(values.get(1)));
        this.instrument       = ""; //get from user later
        this.name             = ""; //get from manifest later    
        this.rulingPrice      = Integer.parseInt(values.get(1));    
        this.bid              = 0;
        this.bidVol           = 0;    
        this.offer            = 0;      
        this.offerVol         = 0; 
        this.deals            = Integer.parseInt(values.get(5)); 
        this.high             = Integer.parseInt(values.get(2));  
        this.low              = Integer.parseInt(values.get(3));  
        this.tradedVol        = Long.parseLong(values.get(4)); 
        this.tradedVal        = Long.parseLong(((values.get(6)).replaceAll(String.valueOf((char)65533), "") ));  
        this.varianceFromPrev = 0; //to derive
        this.varPerc          = 0.0; //to derive
        this.previousRuling   = 0; //to derive;  
        
        //puts the current date, but date must be passed in when getting the sql for insert
        java.util.Date utilDate = null;
        try {
            utilDate = new SimpleDateFormat("dd-MMM-yy").parse(values.get(0));
        } catch(Exception e) {
            ex.addLog(ExceptionTypes.ERROR.toString(), "HistoryCSVRow.constructFromStrings: Problem converting date: "+e.getMessage());
            e.printStackTrace();
        }
        
        this.date = new java.sql.Date(utilDate.getTime());
        
        this.dY               = Double.parseDouble(values.get(8));
        this.eY               = Double.parseDouble(values.get(9));
        this.pE               = Double.parseDouble(values.get(10));      
        this.batchId          = values.get(values.size()-1);        
    }

    @Override
    public List<String> getValues() {
        Vector<String> values = new Vector<String>();
        
        values.add(instrument);
        values.add(name      );
        values.add(Integer.toString(rulingPrice       ));
        values.add(Integer.toString(bid               ));
        values.add(Integer.toString(bidVol            ));
        values.add(Integer.toString(offer             ));
        values.add(Integer.toString(offerVol          ));
        values.add(Integer.toString(deals             ));
        values.add(Integer.toString(high              ));
        values.add(Integer.toString(low               ));
        values.add(Long.toString(tradedVol         ));
        values.add(Long.toString(tradedVal         ));
        values.add(Integer.toString(varianceFromPrev  ));
        values.add(Double.toString(varPerc           ));
        values.add(Integer.toString(previousRuling    ));
        values.add(date.toString());
        values.add(Double.toString(dY                ));
        values.add(Double.toString(eY                ));
        values.add(Double.toString(pE                ));
        
        return values;
    }
    
    @Override
    public String uploadData(JSEUtil util, String instrument){             
        
        PreparedStatement insertPreparedStatement = null;
        try {       
            
            if ((util.checkManifest(instrument)).equalsIgnoreCase("N")){
                ex.addLog(ExceptionTypes.ERROR.toString(), "HistoryCSVRow.uploadData: This instrument does not exist: "+instrument);
                return "FAILED";
            }
            ex.infoLog("Values to insert: ", ExceptionFlags.SHOW);
            ex.infoLog("instrument: "+instrument, ExceptionFlags.SHOW);
            ex.infoLog("rulingPrice: "+rulingPrice, ExceptionFlags.SHOW);
            ex.infoLog("bid: "+bid);
            ex.infoLog("offer: "+offer);
            ex.infoLog("deals: "+deals);
            ex.infoLog("dY: "+dY);
            ex.infoLog("eY: "+eY);
            ex.infoLog("pE: "+pE);
            ex.infoLog("date: "+date, ExceptionFlags.SHOW);
            
            insertPreparedStatement = util.getConn().prepareStatement("insert into " + instrument +
                                                            " values (? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? )");
            insertPreparedStatement.setString(1, instrument);  
            insertPreparedStatement.setString(2, instrument); 
            insertPreparedStatement.setInt(3, rulingPrice);     
            insertPreparedStatement.setInt(4, bid);
            insertPreparedStatement.setInt(5, bidVol);  
            insertPreparedStatement.setInt(6, offer);  
            insertPreparedStatement.setInt(7, offerVol);  
            insertPreparedStatement.setInt(8, deals);  
            insertPreparedStatement.setInt(9, high);  
            insertPreparedStatement.setInt(10, low);  
            insertPreparedStatement.setLong(11, tradedVol);  
            insertPreparedStatement.setLong(12, tradedVal);  
            insertPreparedStatement.setInt(13, varianceFromPrev);   
            insertPreparedStatement.setDouble(14, varPerc);  
            insertPreparedStatement.setInt(15, previousRuling);  
            insertPreparedStatement.setDate(16, date);      
            insertPreparedStatement.setDouble(17, dY);    
            insertPreparedStatement.setDouble(18, eY);   
            insertPreparedStatement.setDouble(19, pE);   
            insertPreparedStatement.setString(20, batchId); 
            
            insertPreparedStatement.executeUpdate();  
            
        } catch(SQLException se) {
            //se.printStackTrace();
            ex.infoLog(se.getMessage(), ExceptionFlags.SHOW);
            ex.addLog(ExceptionTypes.ERROR.toString(), "HistoryCSVRow.uploadData: "+date+": "+se.getMessage());
            return "FAILED";
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "HistoryCSVRow.uploadData: "+date+": "+e.getMessage());
            return "FAILED";
        }      
        return "SUCCESS";
    }
    
    public static void main(String [] args ){
        /*
        HistoryCSVRow h = new HistoryCSVRow();
        List<String> values = new Vector<String>();
        
        values.add("12-Jan-2013");
        values.add(Integer.toString(100));
        values.add(Integer.toString(10));
        values.add(Integer.toString(19));
        values.add(Integer.toString(1));
        values.add(Integer.toString(1));
        values.add(Integer.toString(15));
        values.add("0.1");
        values.add("0.2");
        values.add("0.3");
        values.add("0.4");
                
        h.constructFromStrings(values);
        
        List<CSVRow> CSVRows = new Vector<CSVRow>();
        CSVRows.add(h);
                
        ex.infoLog("the resultant rate is: " + JSEUtils.uploadCSV(CSVRows));*/
        System.out.println("dt: " +(new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())));
    }
}
