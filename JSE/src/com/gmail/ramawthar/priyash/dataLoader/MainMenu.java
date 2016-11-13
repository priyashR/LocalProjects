package com.gmail.ramawthar.priyash.dataLoader;

import java.io.File;

import java.sql.Connection;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private List<CSVRow> fileData;
    private RowType r;
    private JSEUtil utility;
    private ExceptionLogger ex;
    private final String fileDatePrefix = "myDailyUpload_";

    public MainMenu() {
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
    
    private void loadHistory(String HISTfilePath, String additionalInfo){
        Scanner scn = new Scanner(System.in);
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
    }
    
    
    public void menuInput(){
    	
        class menuOptions{
        	void optionThree(){
        		
        		boolean runThree = true;
                String ThreePrompt = "";
                while (runThree) {
                    ex.infoLog("Please select an option: ", ExceptionFlags.SHOW);
                    ex.infoLog("1 -> List all instruments available.", ExceptionFlags.SHOW);
                    ex.infoLog("2 -> List instrument data(date, price).", ExceptionFlags.SHOW);
                    ex.infoLog("3 -> List all batches.", ExceptionFlags.SHOW);
                    ex.infoLog("4 -> Exit query menu.", ExceptionFlags.SHOW);
                    //add an option to see the current log of errors
                    Scanner scn = new Scanner(System.in);
                    ThreePrompt = scn.nextLine();
                    if (ThreePrompt.equalsIgnoreCase("1")){   
                    	ex.infoLog("List all instruments available.", ExceptionFlags.SHOW);
                    }else if (ThreePrompt.equalsIgnoreCase("2")){
                    	ex.infoLog("List instrument data(date, price).", ExceptionFlags.SHOW);
                    }else if (ThreePrompt.equalsIgnoreCase("3")){
                    	ex.infoLog("List all batches.", ExceptionFlags.SHOW);
                    }else if (ThreePrompt.equalsIgnoreCase("4")){
                    	ex.infoLog("Till we meet again, bye.", ExceptionFlags.SHOW);
                    	runThree = false;
                    }
                    
                }
        	}
        }
        boolean runMain = true;
        String mainPrompt = "";
        String additionalInfo = "";
        while (runMain) {
            ex.infoLog("Please select an option: ", ExceptionFlags.SHOW);
            ex.infoLog("1 -> Load history file.", ExceptionFlags.SHOW);
            ex.infoLog("2 -> Load daily file.", ExceptionFlags.SHOW);
            ex.infoLog("3 -> Query data.", ExceptionFlags.SHOW);
            ex.infoLog("4 -> Remove a batch for an instrument.", ExceptionFlags.SHOW);
            ex.infoLog("5 -> Remove a full batch.", ExceptionFlags.SHOW);
            ex.infoLog("6 -> Create structures.", ExceptionFlags.SHOW);
            ex.infoLog("7 -> Show current log.", ExceptionFlags.SHOW);
            ex.infoLog("8 -> Exit application.", ExceptionFlags.SHOW);
            //add an option to see the current log of errors
            Scanner scn = new Scanner(System.in);
            mainPrompt = scn.nextLine();
            if (mainPrompt.equalsIgnoreCase("1")){
                ex.infoLog("Enter CSV file path: ", ExceptionFlags.SHOW);
                String filePath = scn.nextLine();  
                File csvFileCheck = new File(filePath);
                if (!csvFileCheck.exists()){
                    ex.infoLog("The file: "+filePath+" deos not exist!", ExceptionFlags.SHOW);
                }else if(!csvFileCheck.isFile()){//if it is a folder
                    ex.infoLog(csvFileCheck+" is a directory, I will load any applicable file", ExceptionFlags.SHOW);
                    if (!(filePath.substring(filePath.length()-1).equalsIgnoreCase("\\")))
                        filePath = filePath+"\\";          
                    File[] listOfFiles = csvFileCheck.listFiles();
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            loadHistory(filePath+file.getName(),additionalInfo);
                        }
                    }
                }else{//if it is a file
                    loadHistory(filePath,additionalInfo);
                }
                
                /*
                File csvFileCheck = new File(filePath);
                if (!csvFileCheck.exists()){
                    ex.infoLog("The file: "+filePath+" deos not exist!");
                }else{
                    r = RowType.HISTORY;
                    ex.infoLog("ok, I will load the history file");
                    boolean insLoop = true;
                    while (insLoop) {
                        try {                
                            ex.infoLog("Enter instrument(C to cancel): ");
                            additionalInfo = scn.nextLine(); 
                            if ((!(utility.checkManifest(additionalInfo).equalsIgnoreCase("Y"))) && (!(additionalInfo.equalsIgnoreCase("C")))){
                                String createInstrument = "N";
                                ex.infoLog("Instrument "+additionalInfo+" does not exist, create it now?.");
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
                            fileData = utility.readData(r,filePath);                    
                        } catch (Exception e){
                            ex.infoLog(e.toString());
                            ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(1): "+e.getMessage());
                        }  
                        
                        ex.infoLog("The file has been read successfully, upload to db? ");
                        String promptReply ="";
                        promptReply = scn.nextLine();
                        if (promptReply.equalsIgnoreCase("Y")) {
                            ex.infoLog("Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo));
                        } else {
                            ex.infoLog("Ok, returning to previous menu.");
                        }
                    }else{
                        ex.infoLog("Action cancelled.");
                    }
                }*/
            }else if (mainPrompt.equalsIgnoreCase("2")){//clean this up, create a private method and group code
                boolean validDate = true;
                ex.infoLog("Enter CSV file path: ", ExceptionFlags.SHOW);
                String filePath = scn.nextLine();    
                File csvFileCheck = new File(filePath);
                if (!csvFileCheck.exists()){
                    ex.infoLog("The file: "+filePath+" deos not exist!", ExceptionFlags.SHOW);
                }else if(!csvFileCheck.isFile()){
                    ex.infoLog(csvFileCheck+" is a directory, I will load any applicable file", ExceptionFlags.SHOW);
                    //add the section to load the directory in a loop here
                    if (!(filePath.substring(filePath.length()-1).equalsIgnoreCase("\\")))
                        filePath = filePath+"\\";
                    File[] listOfFiles = csvFileCheck.listFiles();
                    boolean skipInvFiles = false;
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            
                            ex.infoLog("Loading file: "+file.getName(), ExceptionFlags.SHOW);
                            r = RowType.DAILY;
                            additionalInfo = getDateString(file.getName(), fileDatePrefix);
                            boolean insLoop = false;
                            
                            if (additionalInfo.equalsIgnoreCase("INV"))
                                if (skipInvFiles)
                                    additionalInfo = "C";
                                else 
                                    insLoop = true;
                            
                            while (insLoop) {      
                                    ex.infoLog("Enter file date(C to cancel, S to skip invalid files): T for today ", ExceptionFlags.SHOW);
                                    additionalInfo = scn.nextLine(); 
                                    if (additionalInfo.equalsIgnoreCase("S")){
                                        skipInvFiles = true;
                                        insLoop = false;
                                        additionalInfo = "C";
                                    }else if(additionalInfo.equalsIgnoreCase("T")){
                                        additionalInfo = (new SimpleDateFormat("dd-MMM-yy").format(Calendar.getInstance().getTime()));  
                                    }else{
                                        try {
                                                new SimpleDateFormat("dd-MMM-yy").parse(additionalInfo);
                                            } catch(Exception e) {
                                                //e.printStackTrace();
                                                validDate = false;
                                            }
                                        if ((!validDate)&&(!(additionalInfo.equalsIgnoreCase("C")))){
                            
                                            ex.infoLog("Invalid date: "+additionalInfo+" - please enter the date in the format dd-MMM-yy (C to Cancel)", ExceptionFlags.SHOW);
                                            ex.addLog(ExceptionTypes.ERROR.toString(), "Invalid date: "+additionalInfo+" - please enter the date in the format dd-MMM-yy.");
                                        }else{
                                            insLoop = false;
                                        }
                                    }
                            }   
                            if (!(additionalInfo.equalsIgnoreCase("C"))){
                                try {
                                    ex.infoLog("reading data ...");
                                    fileData = utility.readData(r,filePath+file.getName());                    
                                } catch (Exception e){
                                    ex.infoLog(e.toString(), ExceptionFlags.SHOW);
                                    ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(2): "+e.getMessage());
                                }  
                                
                                ex.infoLog("The file has been read successfully, upload to db? ", ExceptionFlags.SHOW);
                                String promptReply ="";
                                promptReply = scn.nextLine();
                                if (promptReply.equalsIgnoreCase("Y")) {
                                    ex.infoLog("Your success percentage is: " + utility.uploadCSV(fileData,additionalInfo), ExceptionFlags.SHOW);
                                } else {
                                    ex.infoLog("Ok, continue to next file if there is one.", ExceptionFlags.SHOW);
                                }
                                
                            }else{
                                ex.infoLog("Load for file "+file.getName()+" cancelled.", ExceptionFlags.SHOW);
                            }                            
                        }
                    }                    
                }else{                
                    r = RowType.DAILY;
                    ex.infoLog("ok, I will load the daily file", ExceptionFlags.SHOW);
                    additionalInfo = getDateString(filePath, fileDatePrefix);
                    boolean insLoop = false;
                    if (additionalInfo.equalsIgnoreCase("INV"))
                        insLoop = true;
                    while (insLoop) {      
                            ex.infoLog("Enter file date(C to cancel): T for today ", ExceptionFlags.SHOW);
                            additionalInfo = scn.nextLine(); 
                            if (additionalInfo.equalsIgnoreCase("T")){
                                additionalInfo = (new SimpleDateFormat("dd-MMM-yy").format(Calendar.getInstance().getTime()));  
                            }else{
                                try {
                                        new SimpleDateFormat("dd-MMM-yy").parse(additionalInfo);
                                    } catch(Exception e) {
                                        e.printStackTrace();
                                        validDate = false;
                                    }
                                if ((!validDate)&&(!(additionalInfo.equalsIgnoreCase("C")))){
    
                                    ex.infoLog("Invalid date: "+additionalInfo+" - please enter the date in the format dd-MMM-yy (C to Cancel)", ExceptionFlags.SHOW);
                                    ex.addLog(ExceptionTypes.ERROR.toString(), "Invalid date: "+additionalInfo+" - please enter the date in the format dd-MMM-yy.");
                                }else{
                                    insLoop = false;
                                }
                            }
                    }   
                    if (!(additionalInfo.equalsIgnoreCase("C"))){
                        try {
                            ex.infoLog("reading data ...");
                            fileData = utility.readData(r,filePath);                    
                        } catch (Exception e){
                            ex.infoLog(e.toString());
                            ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(2): "+e.getMessage());
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
            }else if (mainPrompt.equalsIgnoreCase("3")){
                //ex.infoLog("Data Queries are not available yet.", ExceptionFlags.SHOW);
            	new menuOptions().optionThree();
            }else if (mainPrompt.equalsIgnoreCase("4")){
                String instrument = "";
                ex.infoLog("Please enter batch id.", ExceptionFlags.SHOW);
                String batchId = scn.nextLine();   
                
                boolean insLoop = true;
                while (insLoop) {
                    try {                
                        ex.infoLog("Please enter instrument(C to cancel): ", ExceptionFlags.SHOW);
                        instrument = scn.nextLine(); 
                        if ((!(utility.checkManifest(instrument).equalsIgnoreCase("Y"))) && (!(instrument.equalsIgnoreCase("C")))){
                            ex.infoLog("Instrument "+instrument+"does not exist, please create it.", ExceptionFlags.SHOW);
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+instrument+"does not exist, please create it.");
                        }else{
                            insLoop = false;
                        }
                    }catch (SQLException se){
                        se.printStackTrace();
                        ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(4): Problem checking the manifest - "+se.getMessage());
                    }
                }
                if (!(instrument.equalsIgnoreCase("C"))){
                    utility.removeBatch(batchId, instrument);
                    ex.infoLog("Batch "+batchId+" removed for instrument "+instrument);                    
                }else{
                    ex.infoLog("Action cancelled.", ExceptionFlags.SHOW);
                }
            }else if (mainPrompt.equalsIgnoreCase("5")){                
                ex.infoLog("Please enter batch id.", ExceptionFlags.SHOW);
                String batchId = scn.nextLine();   
                utility.removeAllBatches(batchId);
                ex.infoLog("All data removed for batch "+batchId, ExceptionFlags.SHOW);   
            }else if (mainPrompt.equalsIgnoreCase("6")){                
                boolean insLoop = true;
                while (insLoop) {
                    try {                
                        ex.infoLog("Enter NEW instrument(C to cancel): ", ExceptionFlags.SHOW);
                        additionalInfo = scn.nextLine(); 
                        if ((utility.checkManifest(additionalInfo).equalsIgnoreCase("Y")) && (!(additionalInfo.equalsIgnoreCase("C")))){
                            ex.infoLog("Instrument "+additionalInfo+" ALREADY exists, can not create it.", ExceptionFlags.SHOW);
                            ex.addLog(ExceptionTypes.ERROR.toString(), "Instrument "+additionalInfo+" ALREADY exists, can not create it.");
                        }else{
                            insLoop = false;
                        }
                    }catch (SQLException se){
                        se.printStackTrace();
                        ex.addLog(ExceptionTypes.ERROR.toString(), "MainMenu.menuInput(6): Problem checking the manifest - "+se.getMessage());
                    }
                }
                if (!(additionalInfo.equalsIgnoreCase("C"))){
                    utility.createNewInstrumentTable(additionalInfo);
                }else{
                    ex.infoLog("Action cancelled.", ExceptionFlags.SHOW);
                }
            }else if (mainPrompt.equalsIgnoreCase("7")){             
                ex.infoLog("Please enter batch id(B for current batch)", ExceptionFlags.SHOW);
                String batchId = scn.nextLine();   
                
                if (batchId.equalsIgnoreCase("B"))
                    batchId = getCurrentBatch();
                
                Iterator logIterator = (ex.getFullBatchLog(batchId)).iterator();
                String [][] logDetails;
                int pageSize = 0;
                int logCount = 0;
                ex.infoLog("");
                ex.infoLog("------------- Result set details -------------");
                ex.infoLog("");
                
                while (logIterator.hasNext()){
                    pageSize = 0;
                    ex.infoLog("Curent batch: "+getCurrentBatch(), ExceptionFlags.SHOW);
                    while ((logIterator.hasNext())&&(pageSize < 51)){
                        logDetails = (String [][])logIterator.next();
                        logCount++;
                        ex.infoLog((logDetails[0][0]).toString()+": "+(logDetails[1][0]).toString()+": "+(logDetails[2][0]).toString(), ExceptionFlags.SHOW); 
                        pageSize++;
                    }
                    ex.infoLog("Hit enter for the next page of log files ... ", ExceptionFlags.SHOW);
                    scn.nextLine(); 
                }
                ex.infoLog("Log count of Result set: "+logCount, ExceptionFlags.SHOW);
                /*
                logIterator = (ex.getLog()).iterator();
                logDetails = null;
                pageSize = 0;
                while (logIterator.hasNext()){
                    pageSize = 0;
                    ex.infoLog("Curent batch: "+getCurrentBatch());
                    while ((logIterator.hasNext())&&(pageSize < 51)){
                        logDetails = (String [][])logIterator.next();
                        ex.infoLog((logDetails[0][0]).toString()+": "+(logDetails[1][0]).toString()); 
                        pageSize++;
                    }
                    ex.infoLog("Hit enter for the next page of log files ... ");
                    scn.nextLine(); 
                }                
                */
            }else if (mainPrompt.equalsIgnoreCase("8")){
                ex.infoLog("Ok, bye.", ExceptionFlags.SHOW);
                runMain = false;
            }else{
                ex.infoLog("Invalid input, please select a value between 1 and 8.", ExceptionFlags.SHOW);
            }
        }    


    }

    public static void main(String [] args){
        
        MainMenu myExtraction = null;
        try {     
            myExtraction = new MainMenu();
            myExtraction.menuInput();
            myExtraction.getEx().infoLog("getBatchId: "+  myExtraction.getCurrentBatch(), ExceptionFlags.SHOW_AND_STORE);
            //ex.infoLog("size: "+  ((myExtraction.getFileData()).size()));
            //ex.infoLog("rowSize: "+ ((myExtraction.getFileData().get(1)).getValues()).size());
            
            //ex.infoLog("ins: "+((myExtraction.getFileData().get(1)).getValues()).get(1));
            //ex.infoLog("ins: "+((myExtraction.getFileData().get(2)).getValues()).get(1));          
        }catch (Exception e){
            e.printStackTrace();
            myExtraction.getEx().addLog(ExceptionTypes.ERROR.toString(), "Main: "+e.getMessage());
        }finally{
            //commit rest of log to datastore first then close the connection with the following line
            try{
                myExtraction.getEx().commitToDataStore();
            }catch(Exception e){
                Iterator logIterator = myExtraction.getEx().getLog().iterator();
                String [][] logDetails;
                myExtraction.getEx().infoLog("UNSAVED ERRORS:", ExceptionFlags.SHOW);
                while (logIterator.hasNext()){
                    java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
                    logDetails = (String [][])logIterator.next();
                    myExtraction.getEx().infoLog((logDetails[0][0]).toString(), ExceptionFlags.SHOW);
                    myExtraction.getEx().infoLog((logDetails[1][0]).toString(), ExceptionFlags.SHOW);    
                }
                e.printStackTrace();
            }
            myExtraction.closeConn();
            
        }
        
       // JSEUtils.createNewInstrumentTable("ADI");
                       
    }

}
