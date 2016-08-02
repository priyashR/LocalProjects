package com.gmail.ramawthar.priyash.dataLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class JSEMySqlUtil implements JSEUtil {//make this a proper cclass and not a static one
        
    // JDBC driver name and database URL
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private final String DB_URL = "jdbc:mysql://localhost:3306/jsesharedata";
    private String BatchId;
    private ExceptionLogger ex;
    
    private Connection conn = null;
      
    // Database credentials
    private final String USER = "ost";
    private final String PASS = "ost";
    
    public JSEMySqlUtil() {
        super();            
        BatchId = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        ex = new MySqlExceptionLogger(this.getConn(), 3, this.getBatchId(), ExceptionFlags.SUPRESS);
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            ex.infoLog("\nConnecting to database...");
            ex.addLog(ExceptionTypes.INFO.toString(), "Connecting to database...");
            this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
            ex.setConn(this.conn);
            ex.infoLog(" SUCCESS! Connected to the db... ");    
            ex.addLog(ExceptionTypes.INFO.toString(), " SUCCESS! Connected to the db... ");        
            
            
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.JSEMySqlUtil: "+se.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.JSEMySqlUtil: "+e.getMessage());
        } 
    }

    public void commitUtil(){
        try {
            if (conn.getAutoCommit())
                conn.setAutoCommit(false);
            conn.commit();
        }catch(SQLException e){
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.commitUtil: "+e.getMessage());
        }
        
    }
    public List<CSVRow> readData(RowType rowType, String filePath) throws Exception {
        List<CSVRow> collection = new Vector<CSVRow>();
        File fileTemplate = new File(filePath);
        FileInputStream fis = new FileInputStream(fileTemplate);
        Reader fr = new InputStreamReader(fis, "UTF-8");
        List<String> values = CSVHelper.parseLine(fr);
        while (values!=null) {
            //ex.infoLog("Extracting rows for the "+rowType.toString()+" file ... ");
            //ex.addLog(ExceptionTypes.INFO.toString(), "Extracting rows for the "+rowType.toString()+" file ... "); 
            values.add(BatchId);
            collection.add(CSVRowFactory.retrieveCSVRow((rowType.toString()),values, ex));
            values = CSVHelper.parseLine(fr);
        }
        return collection;
    } 
    
    public String checkManifest(String instrument) throws SQLException{

        PreparedStatement checkPreparedStatement = null;
        
        checkPreparedStatement = conn.prepareStatement("select * from instrument_manifest where instrument = ?");
        checkPreparedStatement.setString(1, instrument.toUpperCase());
        ResultSet resultSet = checkPreparedStatement.executeQuery();
        
        if (resultSet.next()){    
            return "Y";            
        }      
        return "N";
    }    
    
    public String checkFormulaeManifest(String columnName) throws SQLException{

        PreparedStatement checkPreparedStatement = null;
        
        checkPreparedStatement = conn.prepareStatement("select * from formulae_manifest where formulae_column = ?");
        checkPreparedStatement.setString(1, columnName.toUpperCase());
        ResultSet resultSet = checkPreparedStatement.executeQuery();
        
        if (resultSet.next()){    
            return "Y";            
        }      
        return "N";
    }
    
    public String addNewFormulaeColumn(String columnName, String columnType, String dynamicInd){ // You need to add the formulae type to the formula_manifest!!! and use it when creating new tables
        //Temp fix in to add forumla column to table that dont have them, if they were created after the column was added
        //If the formula exists it is simply not inserted into the formula manifest, but will attempt to add the columns to the formula tables
        Statement stmt = null;
        PreparedStatement insertPreparedStatement = null;        
        if ((columnName == null) || (columnName.equalsIgnoreCase(""))){
            return "Please enter a column name!.";         
        }
        columnName = columnName.toUpperCase();
        try {   
            ex.addLog(ExceptionTypes.INFO.toString(), "Check that the formula column " + columnName + " does not exist in the database..."); 
            
            stmt = conn.createStatement();
            if ((checkFormulaeManifest(columnName)).equalsIgnoreCase("Y")) {
            
                ex.addLog(ExceptionTypes.INFO.toString(), "This formulae column (" + columnName + ") already exists and will not be added to the manifest. All the instrument that do not have this formulae will be updated, the instruments that do will be ignored.");            
                //return ("This formulae column (" + columnName + ") already exists and will not be created.");
                
            } else {
            
                ex.infoLog("Creating the formulae column " + columnName + " to the share data database...", ExceptionFlags.SHOW);      
                ex.addLog(ExceptionTypes.INFO.toString(), "Creating the formulae column " + columnName + " in the share data database for each instrument table...");    

                java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
                
                insertPreparedStatement = conn.prepareStatement("insert into formulae_manifest values (? ,? ,?, ?)");
                insertPreparedStatement.setString(1, columnName);
                insertPreparedStatement.setDate(2, date);
                insertPreparedStatement.setString(3, "Y");  
                if ((dynamicInd != null)&&(dynamicInd.equalsIgnoreCase("N"))){
                    insertPreparedStatement.setString(4, "N");  
                }else{
                    insertPreparedStatement.setString(4, "Y");                      
                }
                insertPreparedStatement.executeUpdate();  
                System.out.println("columnType; |"+columnType+"|");
            }
                
                if ((dynamicInd != null)&&(dynamicInd.equalsIgnoreCase("N"))){
                    if ((columnType == null)||(columnType.equalsIgnoreCase("")))
                        columnType = "DECIMAL(12,2)";
                    columnType = columnType.toUpperCase();
                    String instrument = null;
                    PreparedStatement checkPreparedStatement = null;
                    checkPreparedStatement = conn.prepareStatement("select Instrument from instrument_manifest");
                    ResultSet resultSet = checkPreparedStatement.executeQuery();        
                        while (resultSet.next()){
                            try {
                                instrument = resultSet.getString("Instrument");
                                ex.infoLog("instrument: "+instrument, ExceptionFlags.SHOW);
                                
                                String sql = "alter table " +instrument +"_F add "+ columnName + " " +columnType;  
                                //String sql = "alter table " +instrument +"_F DROP TEST";                          
                                System.out.println(sql);
                                ex.addLog(ExceptionTypes.INFO.toString(), "sql stmt: "+sql);                         
                                stmt.executeUpdate(sql);                        
                            }catch (Exception e) {
                                e.printStackTrace();
                                ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.addNewFormulaeColumn: "+columnName+": "+e.getMessage());
                            }
                        }   
                        resultSet.close();
                    }
            
             
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.addNewFormulaeColumn: "+columnName+": "+se.getMessage());
            return "Error: "+ se.getMessage(); 
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.addNewFormulaeColumn: "+columnName+": "+e.getMessage());
            return "Error: "+ e.getMessage();
        } 
        return columnName + " added successfully!";
    }
    
    public String removeFormulaeColumn(String columnName){
        Statement stmt = null;
        PreparedStatement insertPreparedStatement = null;
        if ((columnName == null) || (columnName.equalsIgnoreCase("")))
         return "Please enter a column name!.";                                    
        columnName = columnName.toUpperCase();
        try {   
            ex.addLog(ExceptionTypes.INFO.toString(), "Check that the formula column " + columnName + " does exist in the database..."); 
            
            if ((checkFormulaeManifest(columnName)).equalsIgnoreCase("N")) {
            
                ex.addLog(ExceptionTypes.INFO.toString(), "This formulae column (" + columnName + ") does not exist and can't be removed.");            
                return ("This formulae column (" + columnName + ") does not exist and can't be removed.");
                
            } else {
            
                ex.infoLog("Removing the formulae column " + columnName + " from the share data database...", ExceptionFlags.SHOW);      
                ex.addLog(ExceptionTypes.INFO.toString(), "Removing the formulae column " + columnName + " in the share data database for each instrument table...");    

                java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
                
                stmt = conn.createStatement();
                insertPreparedStatement = conn.prepareStatement("delete from formulae_manifest where formulae_column = ? ");
                insertPreparedStatement.setString(1, columnName);
                insertPreparedStatement.executeUpdate();  
                
                String instrument = null;
                PreparedStatement checkPreparedStatement = null;
                checkPreparedStatement = conn.prepareStatement("select Instrument from instrument_manifest");
                ResultSet resultSet = checkPreparedStatement.executeQuery();        
                    while (resultSet.next()){
                        instrument = resultSet.getString("Instrument");
                        ex.infoLog("instrument: "+instrument, ExceptionFlags.SHOW);
                        
                        //String sql = "alter table " +instrument +"_F add "+ columnName + " " +columnType;  
                        String sql = "alter table " +instrument +"_F DROP "+ columnName;                          
                        System.out.println(sql);
                        ex.addLog(ExceptionTypes.INFO.toString(), "sql stmt: "+sql);                         
                        stmt.executeUpdate(sql);                        
                    }             
                
                }
             
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeFormulaeColumn: "+columnName+": "+se.getMessage());
            return "Error: "+ se.getMessage(); 
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeFormulaeColumn: "+columnName+": "+e.getMessage());
            return "Error: "+ e.getMessage();
        } 
        return columnName + " removed successfully!";
    }    
    
    public void createNewInstrumentTable(String instrument){
        Statement stmt = null;
        PreparedStatement insertPreparedStatement = null;
        instrument = instrument.toUpperCase();
        try {       
            
            //ex.infoLog("Check that the instrument " + instrument + " does not exist in the database...");   
            ex.addLog(ExceptionTypes.INFO.toString(), "Check that the instrument " + instrument + " does not exist in the database..."); 
            
            if ((checkManifest(instrument)).equalsIgnoreCase("Y")) {
            
                ex.infoLog("This instrument (" + instrument + ") already exists and will not be created.", ExceptionFlags.SHOW);
                ex.addLog(ExceptionTypes.INFO.toString(), "This instrument (" + instrument + ") already exists and will not be created.");
                
            } else {
            
                ex.infoLog("Creating the instrument " + instrument + " table in the share data database...", ExceptionFlags.SHOW);      
                ex.addLog(ExceptionTypes.INFO.toString(), "Creating the instrument " + instrument + " table in the share data database...");                  
                stmt = conn.createStatement();
                     
                String sql = "CREATE TABLE "+ instrument + " " +
                             "(Instrument   VARCHAR(32)    " +
                             ",Name VARCHAR(1024)          " +
                             ",RulingPrice  INTEGER    " +
                             ",Bid  INTEGER            " +
                             ",BidVol       INTEGER            " +
                             ",Offer        INTEGER        " +
                             ",OfferVol     INTEGER            " +
                             ",Deals        INTEGER            " +
                             ",High INTEGER            " +
                             ",Low  INTEGER            " +
                             ",TradedVol    INTEGER        " +
                             ",TradedVal    BIGINT    " +
                             ",VarianceFromPrev     INTEGER" +
                             ",VarPerc      DECIMAL(8,3)            " +
                             ",PreviousRuling       INTEGER" +
                             ",Date DATE                   " +
                             ",DY   DECIMAL(17,8)           " +
                             ",EY   DECIMAL(17,8)           " +
                             ",PE   DECIMAL(17,8)           " +
                             ",BatchId VARCHAR(320)" + 
                             ",UNIQUE KEY Date_UNIQUE (Date))";//Ymdhms date format - retrieve in the CSVRow implementing class
                
                ex.addLog(ExceptionTypes.INFO.toString(), "sql stmt: "+sql); 
        
                stmt.executeUpdate(sql);
                try {
                    if (checkManifest("TEMPLATE").equalsIgnoreCase("Y"))
                         sql = "CREATE TABLE "+ instrument + "_F " +
                                 "as select * from template_f";
                    else sql = "CREATE TABLE "+ instrument + "_F " +
                                 "(Date DATE)";
                    }//Ymdhms date format - retrieve in the CSVRow implementing class}
                catch (SQLException e)
                        {sql = "CREATE TABLE "+ instrument + "_F " +
                                 "(Date DATE)";}//Ymdhms date format - retrieve in the CSVRow implementing class}
                        

                
                ex.addLog(ExceptionTypes.INFO.toString(), "sql stmt: "+sql); 
                
                stmt.executeUpdate(sql);    
                
                ex.infoLog("Table "+instrument+" created in given database...", ExceptionFlags.SHOW);                            
                ex.infoLog("Inserting an entry into the instrument manifest for instrument "+ instrument, ExceptionFlags.SHOW);
                
                insertPreparedStatement = conn.prepareStatement("insert into instrument_manifest values (? ,? ,?, ?)");
                
                java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
                
                insertPreparedStatement.setString(1, instrument);
                insertPreparedStatement.setDate(2, date);
                insertPreparedStatement.setString(3, "Y");
                insertPreparedStatement.setString(4, "N");
                
                insertPreparedStatement.executeUpdate();  
                
            }

            commitUtil();
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.createNewInstrumentTable: "+instrument+": "+se.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.createNewInstrumentTable: "+instrument+": "+e.getMessage());
        } 
        ex.infoLog("Done!", ExceptionFlags.SHOW);       
        ex.addLog(ExceptionTypes.INFO.toString(), instrument + " Created successfully!");
    }
/*     private String addNewInstrumentFormulae(String instrument){
        Statement stmt = null;
        PreparedStatement insertPreparedStatement = null;        
        try{
            
            PreparedStatement checkPreparedStatement = null;
            checkPreparedStatement = conn.prepareStatement("select Instrument from instrument_manifest");
            ResultSet resultSet = checkPreparedStatement.executeQuery();        
                while (resultSet.next()){
                    instrument = resultSet.getString("Instrument");
                    ex.infoLog("instrument: "+instrument, ExceptionFlags.SHOW);
                    
                    //String sql = "alter table " +instrument +"_F add "+ columnName + " " +columnType;  
                    String sql = "alter table " +instrument +"_F DROP "+ columnName;                          
                    System.out.println(sql);
                    ex.addLog(ExceptionTypes.INFO.toString(), "sql stmt: "+sql);                         
                    stmt.executeUpdate(sql);                        
                }            
            
        
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.addNewInstrumentFormulae: "+instrument+": "+se.getMessage());
            return "Error: "+ se.getMessage(); 
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.addNewInstrumentFormulae: "+instrument+": "+e.getMessage());
            return "Error: "+ e.getMessage();
        } 
        
        return "SUCCESS";
    } */
    public String getBatchId(){
        return (BatchId);
    }
    public Double uploadCSV(List<CSVRow> fileData, String additionalInfo){
        Iterator<CSVRow> rowData = fileData.iterator();
        double success = 0;
        double fail = 0;
        Integer failCount = 0;
        Integer failWarn = 100000;//set this to a lower number like 10 if you want the waring messsge
        try {
            
            if (rowData.hasNext())//skip the header
                rowData.next();
            
            while ((rowData.hasNext())&&(!(failCount.equals(failWarn+1)))){
                if ((rowData.next().uploadData(this, additionalInfo)).equalsIgnoreCase("FAILED")){ // if it fails stop - to remove
                    fail++;
                    failCount++;
                    if (failCount.equals(failWarn)){
                        ex.infoLog("There have been "+failCount+" failed lines since the start of the job or last warning. Do you want to cancel the job?", ExceptionFlags.SHOW);
                        Scanner scn = new Scanner(System.in);
                        String response = scn.nextLine();
                        if (response.equalsIgnoreCase("Y")){
                            failCount++;
                        }else{
                            failCount = 0;
                        }
                    }
                }else{
                    success++;
                }
            }
            commitUtil();
            } catch(Exception e) {
                e.printStackTrace();
                ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.uploadCSV: "+e.getMessage());
            } 
        ex.infoLog("success : "+success, ExceptionFlags.SHOW);
        ex.infoLog("fail : "+fail, ExceptionFlags.SHOW);
        ex.infoLog("success/(success+fail) : "+success/(success+fail), ExceptionFlags.SHOW);
        return (Math.round((((success/(success+fail))*100)*100.0))/100.0);//percentage of successful rows
    }

    public Connection getConn() {
        return conn;
    }
    
    public void closeConn(){
        try {
            if(conn != null)
                conn.close();
        } catch(SQLException se) {
            //add to logging 
            se.printStackTrace();
        }
    }

    public ExceptionLogger getEx() {
        return ex;
    }

    public void removeAllBatches(String batchId){
        PreparedStatement checkPreparedStatement = null;

        String setInstrument = "";
        try {   
            checkPreparedStatement = conn.prepareStatement("select instrument from instrument_manifest");
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            while (resultSet.next()){
                setInstrument = resultSet.getString("instrument");
                ex.infoLog("setInstrument: "+setInstrument, ExceptionFlags.SHOW);
                removeBatch(batchId,setInstrument);
            }

        commitUtil();           
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeAllBatches: "+batchId+"/"+setInstrument+" "+se.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeAllBatches: "+batchId+"/"+setInstrument+" "+e.getMessage());
        }     
        
    }
    public void removeBatch(String batchId, String instrument){
        PreparedStatement insertPreparedStatement = null;
        try {   
            insertPreparedStatement = conn.prepareStatement("delete from "+instrument+" where batchid = ? ");
            insertPreparedStatement.setString(1, batchId);
            insertPreparedStatement.executeUpdate();
            commitUtil();
        } catch(SQLException se) {
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeBatch: "+batchId+"/"+instrument+" : "+se.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "JSEMySqlUtil.removeBatch: "+batchId+"/"+instrument+" : "+e.getMessage());
        }         
        }
    public String dynamicFormulaCheck(String formula) throws SQLException{

        if (checkFormulaeManifest(formula.toUpperCase()).equalsIgnoreCase("Y")){
            PreparedStatement checkPreparedStatement = null;
            
            checkPreparedStatement = conn.prepareStatement("select * from formulae_manifest where formulae_column = ? and isActive = 'Y' and dynamic = 'Y'");
            checkPreparedStatement.setString(1, formula.toUpperCase());
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            
            if (resultSet.next()){    
                return "Y";            
            }      
            return "N";
        }else{
            return "INVALID";
        }
            
    }
    public ResultSet getFormulaData(String instrument, String formula) throws SQLException{

        if (checkFormulaeManifest(formula.toUpperCase()).equalsIgnoreCase("Y")){
            PreparedStatement checkPreparedStatement = null;
            
            checkPreparedStatement = conn.prepareStatement("select date, "+formula+" from "+instrument+"_f order date asc");
            ResultSet resultSet = checkPreparedStatement.executeQuery();
                 
            return resultSet;
        }else{
            return null;
        }
            
    }   
    
    public String addInsToSelectionQueue(String instrument){
        try {   
            PreparedStatement PreparedStatementObj = null;
            
            PreparedStatementObj = conn.prepareStatement("select * from instrument_selection_queue where instrument = ? ");
            PreparedStatementObj.setString(1, instrument.toUpperCase());
            ResultSet resultSet = PreparedStatementObj.executeQuery();
            
            if (resultSet.next()){                    
                System.out.println("instrument already in queue");
                return "Success - instrument already in queue";            
            }else{
                System.out.println("update the queue.");
                PreparedStatementObj = null;
                PreparedStatementObj = conn.prepareStatement("update instrument_selection_queue set queue_number = queue_number + 1");
                PreparedStatementObj.executeUpdate();
                
                System.out.println("insert into the queue.");
                PreparedStatementObj = null;
                PreparedStatementObj = conn.prepareStatement("insert into instrument_selection_queue values (? ,?)");
                PreparedStatementObj.setString(1, instrument.toUpperCase());
                PreparedStatementObj.setInt(2, 1);
                PreparedStatementObj.executeUpdate();
                
                System.out.println("delete the oldest in the queue.");
                PreparedStatementObj = null;
                PreparedStatementObj = conn.prepareStatement("delete from instrument_selection_queue where queue_number > 8");
                PreparedStatementObj.executeUpdate();
                
            }
        } catch(SQLException se) {
            se.printStackTrace();
        }      
        
        return "Success - instrument added";
    }
    
    public String[] getSelectionQueue(){
        
        String [] selectionQueue = new String[8];
        int count = 0;
        try {   
            PreparedStatement PreparedStatementObj = null;
            
            PreparedStatementObj = conn.prepareStatement("select * from instrument_selection_queue where queue_number < 9");
            ResultSet resultSet = PreparedStatementObj.executeQuery();
            
            while (resultSet.next()){
                selectionQueue[count] = resultSet.getString(1);
                count++;
            }
            for (int i = count; i < 8; i++){
                selectionQueue[i] = "ADI";
            }
        } catch(SQLException se) {
            se.printStackTrace();
        } 
        
        return selectionQueue;
    }
   
      /* public static void main(String arg[]){
        JSEMySqlUtil j = new JSEMySqlUtil();
        String [] s = j.getSelectionQueue();
        
        System.out.println("1: "+s[0]);        
        System.out.println("2: "+s[1]);        
        System.out.println("3: "+s[2]);        
        System.out.println("4: "+s[3]);        
        System.out.println("5: "+s[4]);        
        System.out.println("6: "+s[5]);        
        System.out.println("7: "+s[6]);        
        System.out.println("8: "+s[7]);
    } */
}
