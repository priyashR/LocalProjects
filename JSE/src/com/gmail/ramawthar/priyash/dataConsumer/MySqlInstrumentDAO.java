package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;
import com.gmail.ramawthar.priyash.dataLoader.JSEMySqlUtil;
import com.gmail.ramawthar.priyash.dataLoader.JSEUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;

public class MySqlInstrumentDAO implements JSEDAOInt{
    private JSEUtil util;
    public MySqlInstrumentDAO() {
        super();
        util = new JSEMySqlUtil();
    }

    public JSEUtil getUtil() {
        return util;
    }
    
    public ResultSet getInstrumentSet(String instrument, String fromDate, String toDate){
        //from date format yyyy-mm-dd (eg. 2014-02-07)
        try {
            if (util.checkManifest(instrument.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This instrument("+instrument+") does not exist.");
                return null;
            }
            String sql = null;
            
            if (((fromDate == null)||(fromDate.equalsIgnoreCase("")))&&((toDate == null)||(toDate.equalsIgnoreCase("")))){
                sql = "select * from "+instrument+" order by date asc";
                }
            else if ((!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))&&((toDate == null)||(toDate.equalsIgnoreCase("")))){
                    sql = "select * from "+instrument+" where date >= '"+fromDate+"' order by date asc";
                }
            else if (((fromDate == null)||(fromDate.equalsIgnoreCase("")))&&(!((toDate == null)||(toDate.equalsIgnoreCase(""))))){
                    sql = "select * from "+instrument+" where date <= '"+toDate+"' order by date asc";
                }
            else if ((!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))&&(!((toDate == null)||(toDate.equalsIgnoreCase(""))))){
                    sql = "select * from "+instrument+" where date <= '"+toDate+"' and date >= '"+fromDate+"' order by date asc";
                }
            System.out.println(sql);
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = (util.getConn()).prepareStatement(sql);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            return resultSet;
            
        } catch(SQLException se) {
            se.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getInstrumentSet: "+se.getMessage());
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getInstrumentSet: "+e.getMessage());
            return null;
        }              
    }
    
    public ResultSet getManifestSet(){
        //from date format yyyy-mm-dd (eg. 2014-02-07)
        try {
            String sql = "select * from instrument_manifest where isActive = 'Y'";
            
            System.out.println(sql);
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = (util.getConn()).prepareStatement(sql);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            return resultSet;
            
        } catch(SQLException se) {
            se.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getManifestSet: "+se.getMessage());
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getManifestSet: "+e.getMessage());
            return null;
        }              
    }
    
    public String getMaxFormulaDate(String instrument, String formula){
        try {
            if (util.checkManifest(instrument.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This instrument("+instrument+") does not exist.");
                return "NOT_IN_INSTRUMENT_MANIFEST";
            }
                
            if (util.checkFormulaeManifest(formula.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This formulae("+formula+") does not exist.");
                return "NOT_IN_FORMULA_MANIFEST";
            }
            String sql = "select max(date) date from "+instrument.toUpperCase()+"_F where "+formula.toUpperCase()+" <> null";
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = (util.getConn()).prepareStatement(sql);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            String maxDate = null;
            
            while (resultSet.next()){
                maxDate = resultSet.getString("date");
            }
            
            return maxDate;
            
        } catch(SQLException se) {
            se.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getMaxFormulaDate: "+se.getMessage());
            return "NOT_IN_MANIFEST";
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentDAO.getMaxFormulaDate: "+e.getMessage());
            return "NOT_IN_MANIFEST";
        }     
        
        
        
    }
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, int value){
        ///Here create an instance of the MySqlInstrumentFormulaDAO and use that clases implementationof this method 
        //- not the best design but as far as possbile, write once and reuse instead duplicate
        
        //TODO - add the insert here
        //if date exist update it, else insert it
        return "Values posted: instrument: "+instrument+" formula: "+formula+" elementDate: "+elementDate+" value: "+value;
    } 
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, double value){
        ///Here create an instance of the MySqlInstrumentFormulaDAO and use that clases implementationof this method 
        //- not the best design but as far as possbile, write once and reuse instead duplicate
        
        
        //TODO - add the insert here
        //if date exist update it, else insert it
        return "Values posted: instrument: "+instrument+" formula: "+formula+" elementDate: "+elementDate+" value: "+value;
    }
    
    public String updateFormulaRatings(String instrument, Map<String, Integer> formulaData, String ratingDate){
        ///Here create an instance of the MySqlInstrumentFormulaDAO and use that clases implementationof this method 
        //- not the best design but as far as possbile, write once and reuse instead duplicate
                
        return "Success";
    }
    public String syncRatingColumns(){
        ///Here create an instance of the MySqlInstrumentFormulaDAO and use that clases implementationof this method 
        //- not the best design but as far as possbile, write once and reuse instead duplicate
        
        return "Success";
    }
}
