package com.gmail.ramawthar.priyash.dataLoader;

import java.awt.Component;

import java.io.File;

import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class AppCommands {
    
    private List<CSVRow> fileData;
    private RowType r;
    private JSEUtil utility;
    private ExceptionLogger ex;
    private final String fileDatePrefix = "myDailyUpload_";
    private Component parentComponent; 
    
    public AppCommands(Component parentComponent) {
        super();        
        utility = new JSEMySqlUtil();
        ex = utility.getEx();
        this.parentComponent = parentComponent;
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
        utility.closeConn();;
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
    
    // General commands 
    // The below is wrong - it should be on the view/controller, not part of the Model, u are breakign the MVC
    // Command to load a history file
    public void loadHistory(String HISTfilePath, String additionalInfo){
        File csvFileCheck = new File(HISTfilePath);
        if (!csvFileCheck.exists()){
            JOptionPane.showMessageDialog(parentComponent
                                         ,"The file: "+HISTfilePath+" does not exist!"
                                         ,"Cannot continue."
                                         ,JOptionPane.ERROR_MESSAGE);       
        }else{
            r = RowType.HISTORY;
            boolean insLoop = true;
            while (insLoop) {
                try {                
                    String s = (String)JOptionPane.showInputDialog(parentComponent
                                                                 ,"Enter instrument: "
                                                                 ,"Historical data"
                                                                 ,JOptionPane.INFORMATION_MESSAGE);
                    if (s == null)
                        s="C";
                    additionalInfo = s; 
                    if ((!(utility.checkManifest(additionalInfo).equalsIgnoreCase("Y"))) && (!(additionalInfo.equalsIgnoreCase("C")))){
                    /*PRJSE
                        String createInstrument = "N";
                        ex.infoLog("Instrument "+additionalInfo+" does not exist, create it now?.", ExceptionFlags.SHOW);
                        createInstrument = scn.nextLine();
                        if (createInstrument.equalsIgnoreCase("Y")){
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+additionalInfo+" does not exist, requested to create it.");
                            utility.createNewInstrumentTable(additionalInfo);
                            insLoop = false;
                        }else{
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+additionalInfo+" does not exist, please create it.");
                        }*///PRJSE
                    }else{
                        insLoop = false;
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                    ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(1): Problem checking the manifest - "+se.getMessage());
                }
            }/*PRJSE
            if (!(additionalInfo.equalsIgnoreCase("C"))){
                try {
                    ex.infoLog("reading data ...");
                    fileData = utility.readData(r,HISTfilePath);                    
                } catch (Exception e){
                    ex.infoLog(e.toString(), ExceptionFlags.SHOW);
                    ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(1): "+e.getMessage());
                }  
                
                ex.infoLog("The file has been read successfully, upload to db? ", ExceptionFlags.SHOW);
                String promptReply ="";
                promptReply = scn.nextLine();
                if (promptReply.equalsIgnoreCase("Y")) {
                    ex.infoLog("Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo), ExceptionFlags.SHOW);
                } else {
                    ex.infoLog("Ok, returning to previous menu.", ExceptionFlags.SHOW);
                }
            }else{
                ex.infoLog("Action cancelled.", ExceptionFlags.SHOW);
            }*///PRJSE
        }        
    }  
    
   /* private void loadHistory(String HISTfilePath, String additionalInfo){
        Scanner scn = new Scanner(System.in); //*
        File csvFileCheck = new File(HISTfilePath);
        if (!csvFileCheck.exists()){
            ex.infoLog("The file: "+HISTfilePath+" deos not exist!", ExceptionFlags.SHOW);
        }else{
            r = RowType.HISTORY;
            ex.infoLog("ok, I will load the history file at this path: "+HISTfilePath, ExceptionFlags.SHOW);
            boolean insLoop = true;
            while (insLoop) {
                try {                
                    ex.infoLog("Enter instrument(C to cancel): ", ExceptionFlags.SHOW);
                    additionalInfo = scn.nextLine(); 
                    if ((!(utility.checkManifest(additionalInfo).equalsIgnoreCase("Y"))) && (!(additionalInfo.equalsIgnoreCase("C")))){
                        String createInstrument = "N";
                        ex.infoLog("Instrument "+additionalInfo+" does not exist, create it now?.", ExceptionFlags.SHOW);
                        createInstrument = scn.nextLine();
                        if (createInstrument.equalsIgnoreCase("Y")){
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+additionalInfo+" does not exist, requested to create it.");
                            utility.createNewInstrumentTable(additionalInfo);
                            insLoop = false;
                        }else{
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+additionalInfo+" does not exist, please create it.");
                        }
                    }else{
                        insLoop = false;
                    }
                }catch (SQLException se){
                    se.printStackTrace();
                    ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(1): Problem checking the manifest - "+se.getMessage());
                }
            }
            if (!(additionalInfo.equalsIgnoreCase("C"))){
                try {
                    ex.infoLog("reading data ...");
                    fileData = utility.readData(r,HISTfilePath);                    
                } catch (Exception e){
                    ex.infoLog(e.toString(), ExceptionFlags.SHOW);
                    ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(1): "+e.getMessage());
                }  
                
                ex.infoLog("The file has been read successfully, upload to db? ", ExceptionFlags.SHOW);
                String promptReply ="";
                promptReply = scn.nextLine();
                if (promptReply.equalsIgnoreCase("Y")) {
                    ex.infoLog("Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo), ExceptionFlags.SHOW);
                } else {
                    ex.infoLog("Ok, returning to previous menu.", ExceptionFlags.SHOW);
                }
            }else{
                ex.infoLog("Action cancelled.", ExceptionFlags.SHOW);
            }
        }        
    }*/
    
}
