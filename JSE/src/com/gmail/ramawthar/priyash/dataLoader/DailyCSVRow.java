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

public class DailyCSVRow implements CSVRow {
    public DailyCSVRow() {
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
        ex.infoLog("Extracting data for: "+values.get(0));
        if ((values.get(0)).equalsIgnoreCase("Instr.")){
            this.instrument = "HEADER_RECORD";
            return;
        }
        
        this.instrument       = values.get(0);        
        this.name             = values.get(1);    
        this.rulingPrice      = Integer.parseInt(((values.get(2)).replace("c","")));    
        this.bid              = Integer.parseInt(((values.get(3)).replace("c","")));     
        this.bidVol           = Integer.parseInt(values.get(4));    
        this.offer            = Integer.parseInt(((values.get(5)).replace("c","")));      
        this.offerVol         = Integer.parseInt(values.get(6)); 
        this.deals            = Integer.parseInt(values.get(7)); 
        this.high             = Integer.parseInt(((values.get(8)).replace("c","")));  
        this.low              = Integer.parseInt(((values.get(9)).replace("c","")));  
        this.tradedVol        = Long.parseLong(values.get(10)); 
        this.tradedVal        = Long.parseLong(((values.get(11)).replace("c","")));  
        this.varianceFromPrev = Integer.parseInt(((values.get(12)).replace("c","")));  
        this.varPerc          = Double.parseDouble(((values.get(13)).replace("%",""))); 
        this.previousRuling   = Integer.parseInt(((values.get(14)).replace("c","")));  
        
        //puts the current date, but date must be passed in when getting the sql for insert
        this.date             = new java.sql.Date(new java.util.Date().getTime());
        
        //derive later
        this.dY               = 0.0;
        //derive later
        this.eY               = 0.0;
        //derive later
        this.pE               = 0.0;    
        this.batchId               = values.get(values.size()-1);       
    }

    @Override
    public List<String> getValues() {
        
        Vector<String> values = new Vector<String>();
        
        values.add(instrument);
        values.add(name);
        values.add(Integer.toString(rulingPrice));
        values.add(Integer.toString(bid));
        values.add(Integer.toString(bidVol));
        values.add(Integer.toString(offer));
        values.add(Integer.toString(offerVol));
        values.add(Integer.toString(deals));
        values.add(Integer.toString(high));
        values.add(Integer.toString(low));
        values.add(Long.toString(tradedVol));
        values.add(Long.toString(tradedVal));
        values.add(Integer.toString(varianceFromPrev));
        values.add(Double.toString(varPerc));
        values.add(Integer.toString(previousRuling));
        values.add((date).toString());
        values.add(Double.toString(dY));
        values.add(Double.toString(eY));
        values.add(Double.toString(pE));
        values.add(batchId);
        
        return values;  
    }
    
    @Override
    public String uploadData(JSEUtil util, String currentDate){
        
        PreparedStatement insertPreparedStatement = null;
        try {       
            
            if ((util.checkManifest(instrument)).equalsIgnoreCase("N")){
                ex.addLog(ExceptionTypes.ERROR.toString(), "DailyCSVRow.uploadData: The instrument "+instrument+" does not exist.");
                return "FAILED";
            }
            
            date = new java.sql.Date((new SimpleDateFormat("dd-MMM-yy").parse(currentDate)).getTime());
                
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
            
            insertPreparedStatement = util.getConn().prepareStatement("delete from " + instrument +
                                                            " where date = ? ");   
            insertPreparedStatement.setDate(1, date); 
            
            System.out.println("Result: "+insertPreparedStatement.executeUpdate());
            System.out.println("tradedVal: "+tradedVal);
            insertPreparedStatement = null;
            
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
            ex.infoLog(se.getMessage());
            ex.addLog(ExceptionTypes.ERROR.toString(), "DailyCSVRow.uploadData: "+instrument+": "+se.getMessage());
            return "FAILED";
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "DailyCSVRow.uploadData: "+instrument+": "+e.getMessage());
            return "FAILED";
        }      
        
        return "SUCCESS";
        
    }
}
