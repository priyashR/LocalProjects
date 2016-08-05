package com.gmail.ramawthar.priyash.dataLoader;

import com.gmail.ramawthar.priyash.dataConsumer.InstrumentRow;

import java.awt.Component;

import java.io.File;

import java.sql.Connection;
import java.sql.ResultSet;

import java.util.Date;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

//import oracle.jbo.server.util.SQLExceptionConverter;

public class LoaderCommands {
    
    private List<CSVRow> fileData;
    private RowType r;
    private JSEUtil utility;
    private ExceptionLogger ex;
    private final String fileDatePrefix = "myDailyUpload_";
    
    public LoaderCommands() {
        super();        
        utility = new JSEMySqlUtil();
        ex = utility.getEx();
    }
    public String getCurrentBatch(){
        return utility.getBatchId();
    }
    
    public ExceptionLogger getEx() {
        return ex;
    }

    public List<CSVRow> getFileData() {
        return fileData;
    }
    private void closeConn(){
        utility.closeConn();
    }
    public Connection getConn(){
        return utility.getConn();
    }
    
    public String addInsToSelectionQueue(String instrument){
        return utility.addInsToSelectionQueue(instrument);   
    }
    public String[] getSelectionQueue(){
        return utility.getSelectionQueue();   
    }
    private String getDateString(String inputFileName, String fileDatePrefix){
        
        File newFile = new File(inputFileName);
        //ex.infoLog("Filename: "+newFile.getName());
        String fileName = newFile.getName();
        String fullDate = "INV";
            try{
                if (fileName.toUpperCase().contains(fileDatePrefix.toUpperCase())){//myDailyUpload
                    Integer prefixLength = fileDatePrefix.length();
                    String fileDay = fileName.substring(prefixLength+6, prefixLength+8);
                    String fileYear = fileName.substring(prefixLength, prefixLength+4);
                    
                    int monthNum = Integer.parseInt(fileName.substring(prefixLength+4, prefixLength+6));
                    
                    String monthString;
                    switch (monthNum) {
                        case 1:  monthString = "JAN";
                                 break;
                        case 2:  monthString = "FEB";
                                 break;
                        case 3:  monthString = "MAR";
                                 break;
                        case 4:  monthString = "APR";
                                 break;
                        case 5:  monthString = "MAY";
                                 break;
                        case 6:  monthString = "JUN";
                                 break;
                        case 7:  monthString = "JUL";
                                 break;
                        case 8:  monthString = "AUG";
                                 break;
                        case 9:  monthString = "SEP";
                                 break;
                        case 10: monthString = "OCT";
                                 break;
                        case 11: monthString = "NOV";
                                 break;
                        case 12: monthString = "DEC";
                                 break;
                        default: monthString = "Invalid month";
                                 break;
                    }
                    //ex.infoLog(monthString);
                    fullDate = fileDay+"-"+monthString+"-"+fileYear;
                    ex.infoLog("Full date: "+fullDate);
                }
            } catch (Exception e){
                ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.getDateString: Problem getting the date from the filename - "+e.getMessage());
                return "INV";
            }
            
        return fullDate;
    }    
    
    public String loadHistory(String HISTfilePath, String additionalInfo){
        File csvFileCheck = new File(HISTfilePath);
        List<CSVRow> fileData;
        if (!csvFileCheck.exists()){
            return "The file: "+HISTfilePath+" does not exist!, Cannot continue.";       
        }else{
            r = RowType.HISTORY;
            try {    
                    if (utility.checkManifest(additionalInfo).equalsIgnoreCase("Y")){
                        fileData = utility.readData(r,HISTfilePath); 
                        return "Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo);
                    }else{
                        return "The instrument "+additionalInfo+" does not exist, please create it.";
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                    ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.loadHistory: Problem checking the manifest - "+se.getMessage());
                    return "Problem checking the manifest - check the log file for more info.";
                }catch (Exception e){
                    e.printStackTrace();
                    ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.loadHistory: Problem checking the manifest - "+e.getMessage());
                    return "Problem checking the manifest - check the log file for more info.";
                }
        }        
    }  
    
    public String loadDaily(String DAYfilePath, String additionalInfo){
        File csvFileCheck = new File(DAYfilePath);
        List<CSVRow> fileData;
        if (!csvFileCheck.exists()){
            return "The file: "+DAYfilePath+" does not exist!, Cannot continue.";       
        }else{
            r = RowType.DAILY;
            try {    
                    if ((additionalInfo == null) || (additionalInfo == ""))
                        additionalInfo = getDateString(DAYfilePath, fileDatePrefix);
                    if (additionalInfo.equalsIgnoreCase("INV"))
                        return "Error deriving the date of the file from the file name, please enter the date or enter T for today";                
                if (additionalInfo.equalsIgnoreCase("T")){
                        additionalInfo = (new SimpleDateFormat("dd-MMM-yy").format(Calendar.getInstance().getTime()));  
                    }else{
                        try {
                                new SimpleDateFormat("dd-MMM-yy").parse(additionalInfo);
                            } catch(Exception e) {
                                return "Invalid date: "+additionalInfo+" - please enter the date in the format dd-MMM-yy.";
                            }
                    }
                    
                    fileData = utility.readData(r,DAYfilePath); 
                    return "Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo);
                }catch (Exception e){
                    e.printStackTrace();
                    ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.loadDaily: Problem reading the file data - "+e.getMessage());
                    return "Problem reading the file data - check the log file for more info.";
                }
        }        
    }   
    
    public String createStructures(String instrument){
        if ((instrument == null)||(instrument.equalsIgnoreCase("")))
            return "Please enter the new instrument name!";
        try {        
            if (utility.checkManifest(instrument).equalsIgnoreCase("Y")){
                return "The instrument "+instrument+" already exists, cannot continue.";
            }else{
                utility.createNewInstrumentTable(instrument);
                return "The instrument "+instrument+" created.";
                
            }
        }catch (SQLException se){
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.createStructures(6): Problem checking the manifest - "+se.getMessage());
            return "Problem creating instrument - check the log file for more info.";
        }
    }
    
    public String removeInsBatch(String batchId, String instrument){
        try {        
            if (!(utility.checkManifest(instrument).equalsIgnoreCase("Y"))){
                return "The instrument "+instrument+" does not exist, cannot continue.";
            }else{
                utility.removeBatch(batchId, instrument);
                return "The batch "+batchId+" for instrument "+instrument+" removed.";                
            }
        }catch (SQLException se){
            se.printStackTrace();
            ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.removeInsBatch(6): Problem checking the manifest - "+se.getMessage());
            return "Problem finding instrument - check the log file for more info.";
        }
    }
    
    public String removeFullBatch(String batchId){
        utility.removeAllBatches(batchId);
        return "All data removed for batch "+batchId;            
    }
    public String createInstrument(String instrument){
        try {
            if (utility.checkManifest(instrument).equalsIgnoreCase("Y"))
                return "The instrument "+instrument+" already exists!.";
            utility.createNewInstrumentTable(instrument);      
            return "Instrument "+instrument+" created";
            }catch (SQLException se){
                se.printStackTrace();
                ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.createInstrument: Problem checking the manifest - "+se.getMessage());
                return "LoaderCommands.createInstrument: Problem checking the manifest - "+se.getMessage();
            }
    }
    
    public String addNewFormulaeColumn(String columnName, String columnType, String dynamicInd){
        return utility.addNewFormulaeColumn(columnName, columnType, dynamicInd);      
    }      
    public String removeFormulaeColumn(String columnName){
        return utility.removeFormulaeColumn(columnName);      
    }   
    public String checkInsManifest(String instrument){
        try {
            return utility.checkManifest(instrument);      
        }catch (SQLException se){
                se.printStackTrace();
                ex.addLog(ExceptionTypes.ERROR.toString(), "LoaderCommands.createInstrument: Problem checking the manifest - "+se.getMessage());
                return "N";
        }
    }  
    
    public String launchGraph(String insOrForm,String formOrIns, String fromDate, String toDate, String [] paramArr){
        // check date values
        
        Date From = null;        
        Date To = null;
        
        String actualFrom = "";        
        String actualTo = "";
        
        if (!((fromDate == null)||(fromDate.equalsIgnoreCase(""))))
        try {
                From = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            } catch(Exception e) {
                return "Invalid from date: "+fromDate+" - please enter the date in the format yyyy-MM-dd.";
            }      

        if (!((toDate == null)||(toDate.equalsIgnoreCase(""))))
        try {
                To = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            } catch(Exception e) {
                return "Invalid to date: "+toDate+" - please enter the date in the format yyyy-MM-dd.";
            } 
        
        if ((From!=null)&&(To!=null)){
            if (From.compareTo(To)>0){
                actualFrom = fromDate;
                actualTo = toDate;
            }else{
                actualFrom = toDate;
                actualTo = fromDate;
            }
        }else{
            actualFrom = fromDate;
            actualTo = toDate;
        }

        // check if insOrForm is and instrument or formula
        String instrument= "";
        String formula= "";
        
        if (checkInsManifest(insOrForm).equalsIgnoreCase("Y")){
            instrument = insOrForm;
            formula = formOrIns;
        }else{
            formula = insOrForm;
        }
        
        if (checkInsManifest(formOrIns).equalsIgnoreCase("Y")){
            if (instrument == null){
                instrument = formOrIns;
            }else{
                return "Please enter only one instrument and one formula.";
            }
        }else{
            if (instrument == null)
                return "Please enter at least one instrument and one formula.";  
        }
        
        // check if the formula needs to be derived and derive if so
        try {
            String isDynamic = utility.dynamicFormulaCheck(formula);
            if (isDynamic.equalsIgnoreCase("Y")){ // calulate dynamically
                
            }else if(isDynamic.equalsIgnoreCase("N")){ // use static values
                ResultSet formulaData = utility.getFormulaData(instrument, formula);
                
                if (formulaData == null)
                    return "There was a problem getting the formula data for instrument "+instrument+" formula "+formula;
                                
                while ( formulaData.next()){
                    formulaData.getDate("date");
                    formulaData.getDouble(formula);
                }
               
                
            }else 
                return "Invalid formula supplied.";
        }catch (SQLException se) {
            return "Error checking if the formula "+formula+" is dynamic: "+se.getMessage();
        }
        // call the correct formula with the parameters and get the resultant data set
        
        // -> move to the JSEDataAnalyser package : launch the graph for the dataset
        //loadCMD.        
        return "Lift off";
    }
}
