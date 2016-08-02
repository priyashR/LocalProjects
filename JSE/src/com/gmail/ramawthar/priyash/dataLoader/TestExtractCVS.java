package com.gmail.ramawthar.priyash.dataLoader;

import java.io.File;

import java.io.FileInputStream;

import java.io.InputStreamReader;
import java.io.Reader;

import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class TestExtractCVS {
    public TestExtractCVS() {
        super();
    }
    public static List<CSVRow> readData() throws Exception {
        List<CSVRow> collection = new Vector<CSVRow>();
        //get the file path
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter CSV file path: ");
        String csvFile = scn.nextLine();
        //csvFile = "C:\\test.csv";
        File fileTemplate = new File(csvFile);
        FileInputStream fis = new FileInputStream(fileTemplate);
        Reader fr = new InputStreamReader(fis, "UTF-8");
        List<String> values = CSVHelper.parseLine(fr);
        while (values!=null) {
            collection.add(CSVRowFactory.retrieveCSVRow((RowType.TEST.toString()),values, null));
            values = CSVHelper.parseLine(fr);
        }
      //  lnr.close();
        return collection;
    }    
    
    public static void main(String [] args){
        try {
            List<CSVRow> testCollection = TestExtractCVS.readData();
            
            //system.out.println(((TestCSVRow)testCollection.get(0)).getName());
            //system.out.println(((TestCSVRow)testCollection.get(0)).getName());
            
        }
        catch (Exception e){
            System.out.println(e.getMessage());  
            System.out.println(e.toString());   
        }
                       
    }
}
