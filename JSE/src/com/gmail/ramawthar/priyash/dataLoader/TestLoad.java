package com.gmail.ramawthar.priyash.dataLoader;

import java.io.File;

import java.sql.*;
import java.util.*;

public class TestLoad {
    public TestLoad() {
        super();
    }
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://localhost:3306/jsesharedata";
    
    // Database credentials
    static final String USER = "ost";
    static final String PASS = "ost";
    
    private static String getDateString(String inputFileName, String fileDatePrefix){
        
        File newFile = new File(inputFileName);
        System.out.println("Filename: "+newFile.getName());
        String fileName = newFile.getName();
        String fullDate = "U";
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
            System.out.println(monthString);
            fullDate = fileDay+"-"+monthString+"-"+fileYear;
            System.out.println("Full date: "+fullDate);
        }
        return fullDate;
    }

    public static void main(String[] args){
        System.out.println("Enter file path");
        Connection conn = null;
        Statement stmt = null;
        Scanner scn = new Scanner(System.in);
        String filePath = scn.nextLine();
        try {                
                    //"C:\\mysql\\test_uploads"
                    File folder = new File(filePath);
                    File[] listOfFiles = folder.listFiles();
                    if (!folder.isFile())
                        System.out.println(filePath+" is a folder");
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            System.out.println("Full date: "+getDateString(file.getName(), "lookOutList_"));
                            }
                        }
                    
            
            
            
            /*
                //"C:\\mysql\\test_uploads"
                File folder = new File(filePath);
                File[] listOfFiles = folder.listFiles();
if (!folder.isFile())
    system.out.println(filePath+" is a folder");
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        system.out.println(fileName);
                        if (fileName.contains("myDailyUpload")){
                            String fileDay = fileName.substring(20, 22);
                            String fileYear = fileName.substring(14, 18);
                            
                            int monthNum = Integer.parseInt(fileName.substring(18, 20));
                            
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
                            system.out.println(monthString);
                            String fullDate = fileDay+"-"+monthString+"-"+fileYear;
                            system.out.println("Full date: "+fullDate);
                        }
                    }
                }*/
            
            } catch(Exception e) {
                e.printStackTrace();
            }            
/*        String firstName = null, surname = null, notes = null;

        try {
            // STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: Open a connection
            import com.redsam.validator.framework.Session;int("\nConnecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            system.out.println(" SUCCESS!\n");

            // STEP 4: Ask for user input
            import com.redsam.validator.framework.Session;int("Enter first name: ");
            firstName = scn.nextLine();

            import com.redsam.validator.framework.Session;int("Enter surname: ");
            surname = scn.nextLine();

            import com.redsam.validator.framework.Session;int("Enter notes: ");
            notes = scn.nextLine();
            
            // STEP 5: Excute query
            import com.redsam.validator.framework.Session;int("\nInserting records into table...");
            stmt = conn.createStatement();
          
            String sql = "INSERT INTO test_loading (Firstname, Surname, notes)" +
                    "VALUES (?, ?, ?)";
            
            //stmt.executeUpdate(sql);
            
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, notes);
            preparedStatement.executeUpdate(); 
            
            system.out.println(" SUCCESS!\n");
            
        } catch(SQLException se) {
            se.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(stmt != null)
                    conn.close();
            } catch(SQLException se) {
                system.out.println(se.getMessage());
            }
            try {
                if(conn != null)
                    conn.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
        system.out.println("Thank you for your patronage!");
    */
        }        
}
