package com.gmail.ramawthar.priyash.dataLoader;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class MySqlExceptionLogger implements ExceptionLogger {
    
    Connection conn;
    private int cacheSize = 500;
    private List<String[][]> logMessages = new Vector<String[][]>();
    private String currentBatchId;
    private ExceptionFlags defaultFlag = ExceptionFlags.SHOW;
    public MySqlExceptionLogger(Connection conn, int cacheSize, String currentBatchId) {
        super();
        logMessages = new Vector<String[][]>();
        this.conn = conn;
        this.cacheSize = cacheSize;
        this.currentBatchId = currentBatchId;        
    }
    public MySqlExceptionLogger(Connection conn, int cacheSize, String currentBatchId, ExceptionFlags overrideFlag) {
        super();
        logMessages = new Vector<String[][]>();
        this.conn = conn;
        this.cacheSize = cacheSize;
        this.currentBatchId = currentBatchId;  
        this.defaultFlag = overrideFlag;      
    }
    public void setDefaults(Connection conn, int cacheSize, String currentBatchId, ExceptionFlags overrideFlag){
        logMessages = new Vector<String[][]>();
        this.conn = conn;
        this.cacheSize = cacheSize;
        this.currentBatchId = currentBatchId;
        this.defaultFlag = overrideFlag;
    }
    public void addLog(String messageType, String messageValue){
            String [][] log = new String[2][1];
            log[0][0] = messageType;
            log[1][0] = messageValue;
            
        if (!cacheLimitReached()) {
            logMessages.add(log);      
        }else{            
            commitToDataStore();
            logMessages = new Vector<String[][]>(); 
            logMessages.add(log);               
        }
    }
    public void infoLog(String messageValue, ExceptionFlags overrideFlag){
        switch(overrideFlag){
        case SHOW: System.out.println(messageValue);
            break;
        case STORE: addLog(ExceptionTypes.INFO.toString(),messageValue);
            break;
        case SHOW_AND_STORE: addLog(ExceptionTypes.INFO.toString(),messageValue);
            break;
        case SUPRESS:
            break;
        default:
            break;
        }
    }
    public void infoLog(String messageValue){
        switch(defaultFlag){
        case SHOW: System.out.println(messageValue);
            break;
        case STORE: addLog(ExceptionTypes.INFO.toString(),messageValue);
            break;
        case SHOW_AND_STORE: addLog(ExceptionTypes.INFO.toString(),messageValue);
            break;
        case SUPRESS:
            break;
        default:
            break;
        }        
    }
    public void commitToDataStore(){
        if (logMessages.size()>0) {
            PreparedStatement insertPreparedStatement = null;
            try {  
                insertPreparedStatement = conn.prepareStatement("insert into log_messages values (? ,? ,?, ?)");
                Iterator logIterator = logMessages.iterator();
                String [][] logDetails;
                while (logIterator.hasNext()){
                    java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
                    logDetails = (String [][])logIterator.next();
                    insertPreparedStatement.setString(1, currentBatchId.toString());
                    insertPreparedStatement.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
                    insertPreparedStatement.setString(3, (logDetails[0][0]).toString());
                    insertPreparedStatement.setString(4, (logDetails[1][0]).toString());                
                    insertPreparedStatement.executeUpdate();    
                }
            } catch(SQLException se) {
                se.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            } 
        }
    }
    
    public List<String[][]> getLog(){
        return logMessages;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    public int getCacheSize() {
        return cacheSize;
    }
    
    private boolean cacheLimitReached(){
        
        return (cacheSize==logMessages.size());
    }

    public String getCurrentBatchId() {
        return currentBatchId;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    public List<String[][]> getFullBatchLog(String batchId){
        String [][] log;
        List<String[][]> logMessages = new Vector<String[][]>();
        
        try {
            if (conn.getAutoCommit())
                conn.setAutoCommit(false);
            conn.commit();
        }catch(SQLException e){
            e.printStackTrace();
        }
        PreparedStatement checkPreparedStatement = null;
        try{
            System.out.println("batchId: "+batchId);
            checkPreparedStatement = conn.prepareStatement("select * from log_messages where BatchId = ? ORDER BY date_created desc");
            checkPreparedStatement.setString(1, batchId);
            ResultSet resultSet = checkPreparedStatement.executeQuery();
            while(resultSet.next()){
                //System.out.println("message_value: "+resultSet.getString("message_value"));
                log = new String[3][1];
                log[0][0] = resultSet.getString("date_created");
                log[1][0] = resultSet.getString("message_type");
                log[2][0] = resultSet.getString("message_value");                
                logMessages.add(log);            
            }
            
        }catch(SQLException e){
            e.getMessage();
        }
            
        return logMessages;
    }
    public static void main(String[] args){
        JSEUtil u = new JSEMySqlUtil();
        ExceptionLogger ex = new MySqlExceptionLogger(u.getConn(), 3, u.getBatchId());

        ex.addLog(ExceptionTypes.INFO.toString(), "Hello");   
        ex.addLog(ExceptionTypes.INFO.toString(), "Hi");      
        ex.addLog(ExceptionTypes.INFO.toString(), "how are you?");     
        ex.addLog(ExceptionTypes.INFO.toString(), "ok, you?");   
        
        Iterator logIterator = (ex.getLog()).iterator();
        String [][] logDetails;
        while (logIterator.hasNext()){
            System.out.println("getCurrentBatchId: "+ex.getCurrentBatchId());
            logDetails = (String [][])logIterator.next();
            System.out.println("Log type: "+(logDetails[0][0]).toString());
            System.out.println("Log Message: "+(logDetails[1][0]).toString());         
        }
    }

}
