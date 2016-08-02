package com.gmail.ramawthar.priyash.dataConsumer;

import com.gmail.ramawthar.priyash.dataLoader.ExceptionFlags;
import com.gmail.ramawthar.priyash.dataLoader.ExceptionTypes;
import com.gmail.ramawthar.priyash.dataLoader.JSEMySqlUtil;
import com.gmail.ramawthar.priyash.dataLoader.JSEUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

public class MySqlInstrumentFormulaDAO implements JSEDAOInt{
    private JSEUtil util;
    public MySqlInstrumentFormulaDAO() {
        super();
        util = new JSEMySqlUtil();
    }

    public JSEUtil getUtil() {
        return util;
    }
    public ResultSet getInstrumentSet(String instrument, String fromDate, String toDate){
        //from date format yyyy-mm-dd (eg. 2014-02-07)
        String instrumnet_f = instrument+"_F";
        try {
            if (util.checkManifest(instrument.toUpperCase()).equalsIgnoreCase("N")){
                System.out.println("This instrument("+instrumnet_f+") does not exist.");
                return null;
            }
            String sql = null;
            
            if (((fromDate == null)||(fromDate.equalsIgnoreCase("")))&&((toDate == null)||(toDate.equalsIgnoreCase("")))){
                sql = "select * from "+instrumnet_f+" order by date asc";
                }
            else if ((!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))&&((toDate == null)||(toDate.equalsIgnoreCase("")))){
                    sql = "select * from "+instrumnet_f+" where date >= '"+fromDate+"' order by date asc";
                }
            else if (((fromDate == null)||(fromDate.equalsIgnoreCase("")))&&(!((toDate == null)||(toDate.equalsIgnoreCase(""))))){
                    sql = "select * from "+instrumnet_f+" where date <= '"+toDate+"' order by date asc";
                }
            else if ((!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))&&(!((toDate == null)||(toDate.equalsIgnoreCase(""))))){
                    sql = "select * from "+instrumnet_f+" where date <= '"+toDate+"' and date >= '"+fromDate+"' order by date asc";
                }
            System.out.println(sql);
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = (util.getConn()).prepareStatement(sql);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            return resultSet;
            
        } catch(SQLException se) {
            se.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getInstrumentSet: "+se.getMessage());
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getInstrumentSet: "+e.getMessage());
            return null;
        }              
    }
    
    public ResultSet getManifestSet(){
        //from date format yyyy-mm-dd (eg. 2014-02-07)
        try {
            String sql = "select * from formulae_manifest where isActive = 'Y'";
            
            System.out.println(sql);
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = (util.getConn()).prepareStatement(sql);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            return resultSet;
            
        } catch(SQLException se) {
            se.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getManifestSet: "+se.getMessage());
            return null;
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getManifestSet: "+e.getMessage());
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
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getMaxFormulaDate: "+se.getMessage());
            return "NOT_IN_MANIFEST";
        } catch(Exception e) {
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.getMaxFormulaDate: "+e.getMessage());
            return "NOT_IN_MANIFEST";
        }     
        
        
        
    }
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, int value){
        //TODO - add the insert here
        //if date exist update it, else insert it
        return "Values posted: instrument: "+instrument+" formula: "+formula+" elementDate: "+elementDate+" value: "+value;
    } 
    public String postDataToFormulaeTable(String instrument, String formula, String elementDate, double value){
        //TODO - add the insert here
        //if date exist update it, else insert it
        return "Values posted: instrument: "+instrument+" formula: "+formula+" elementDate: "+elementDate+" value: "+value;
    }    
    
    public String updateFormulaRatings(String instrument, Map<String, Integer> formulaData, String ratingDate){
        ResultSet formulae = getManifestSet();      
        try{
        Statement stmt = null;
        stmt = util.getConn().createStatement(); 
            //add the instrument(if it doesnt exist already, if it does, the exception will be ignored)
            try {
                
                String sql = "insert into formula_ratings (Instrument, rating_age) values ('"+ instrument.toUpperCase()+"', 'C')";                   
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            }
            try {
                
                String sql = "insert into formula_ratings (Instrument, rating_age) values ('"+ instrument.toUpperCase()+"', 'P')";  
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            }            
            
            try {
                //move previous current to holding state X
                String sql = "update formula_ratings set rating_age = 'X' where instrument = '"+instrument.toUpperCase()+"' and rating_age = 'C'" ;  
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            } 
            try {
                //move previous previous to current, to be updated
                String sql = "update formula_ratings set rating_age = 'C' where instrument = '"+instrument.toUpperCase()+"' and rating_age = 'P'" ;  
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            }   
            try {
                //move previous current to holding previous
                String sql = "update formula_ratings set rating_age = 'P' where instrument = '"+instrument.toUpperCase()+"' and rating_age = 'X'" ;  
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            }            
            while (formulae.next()){
                
                //update formulae
                String currFormula = formulae.getString("formulae_column").toUpperCase();
                Integer currValue = formulaData.get(currFormula);
                if (formulae.getString("dynamic").equalsIgnoreCase("N")) {

                        try {
                            //add new value
                            String sql = "update formula_ratings set "+ currFormula + " = "+currValue+" where instrument = '"+instrument.toUpperCase()+"' and rating_age = 'C'";  
                            System.out.println(sql);                         
                            stmt.executeUpdate(sql);                        
                        }catch (Exception e) {
                            e.printStackTrace();
                        }                        
                    } 
                }
            //update date

            try {
                //add new date
                String sql = "update formula_ratings set rating_date = '"+ratingDate+"' where instrument = '"+instrument.toUpperCase()+"' and rating_age = 'C'";  
                System.out.println(sql);                         
                stmt.executeUpdate(sql);                        
            }catch (Exception e) {
                e.printStackTrace();
            }            
            formulae.close();
        }catch(Exception e){
            try{
            if (!(formulae.isClosed())){
                    formulae.close();
                }              
            }catch(Exception ef){
                    
                }
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.syncRatingColumns: "+e.getMessage());
            return "updateFormulaRatings_PROBLEM";            
        }
        return "Success";
    }
    public String syncRatingColumns(){
        ResultSet formulae = getManifestSet();      
        try{
        Statement stmt = null;
        stmt = util.getConn().createStatement(); 
            while (formulae.next()){
                if (formulae.getString("dynamic").equalsIgnoreCase("N")) {
                        try {
                            
                            String sql = "alter table formula_ratings add "+ formulae.getString("formulae_column") + " Integer";  
                            //String sql = "alter table " +instrument +"_F DROP TEST";                          
                            System.out.println(sql);                         
                            stmt.executeUpdate(sql);                        
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }   
                }
            formulae.close();
        }catch(Exception e){
            try{
            if (!(formulae.isClosed())){
                    formulae.close();
                }              
            }catch(Exception ef){
                    
                }
            e.printStackTrace();
            (util.getEx()).addLog(ExceptionTypes.ERROR.toString(), "MySqlInstrumentFormulaDAO.syncRatingColumns: "+e.getMessage());
            return "SYNC_PROBLEM";            
        }
        return "Success";
    }   
    public static void main(String [] args){
        MySqlInstrumentFormulaDAO m = new MySqlInstrumentFormulaDAO();
        //System.out.println(m.syncRatingColumns());
        
        Map<String, Integer> data = new HashMap<String, Integer>(); 
        data.put("RSI", 5);
        data.put("RSI14", 4);
        
        m.updateFormulaRatings("ADI",data,"22-MAY-2015");
    }
}
