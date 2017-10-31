package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareMasterFiles {
	
	private String inputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\";
	private String XLSinputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\zips\\";
	private String outputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\";
	private String manifest = "instrumentManifest.csv";
	
	public void processNewData(){
		convertToCSV();
		/*
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	    		if ((!(fileEntry.getName().equalsIgnoreCase("zips")))&&(!(fileEntry.getName().equalsIgnoreCase("instrumentManifest.csv")))){
		            //System.out.println(fileEntry.getName());  
		            System.out.println((getInstrument(fileEntry.getName())));
		            generateFile(fileEntry,getInstrument(fileEntry.getName()));
	    		}
	    }*/
	}
	
	private String getInstrument(String filename){

		String instrumentDesc = (filename.substring(18, filename.indexOf("-2017-10"))).replace("_", " "); 
		//System.out.println(instrumentDesc);
		String result = instrumentDesc+" NOT FOUND";
		
		try {
			
			
			Path path = Paths.get(inputPath+manifest);
			List<String> lines;
			String ins = "";
			String insDesc = "";
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			int i = 0;
			boolean notFound = true;
			while ((i<lines.size())&&(notFound)){
				//System.out.println(lines.get(i));

				StringTokenizer defaultTokenizer = new StringTokenizer(lines.get(i),",");					
				ins = defaultTokenizer.nextToken();
				insDesc = defaultTokenizer.nextToken();
				
				//System.out.println("ins: "+ins);
				//System.out.println("insDesc: "+insDesc);
				//System.out.println("instrumentDesc: "+instrumentDesc);
				
				
				if (insDesc.equalsIgnoreCase(instrumentDesc)){
					notFound = false;
					result = ins;
				}
				
				
				i++;
			}
			//keep it to around +-4 years of data max
			//if (lines.size()>1052)
			//	lines.remove(1);
			//Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	private void generateFile(File file, String fileName){
		
        try (BufferedReader b = new BufferedReader(new FileReader(file))){
        	
            String readLine = "";
            List<String> lines = new ArrayList<String>();
            while ((readLine = b.readLine()) != null) {
            	//System.out.println(readLine);
            	lines.add(readLine);
            }
            b.close();
            Path path = Paths.get(outputPath+fileName+".txt");
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }	
		
	}
	
	private void convertToCSV(){
        try 
        { 
    		File folder = new File(XLSinputPath);
    		//read the input directory
    	    for (final File fileEntry : folder.listFiles()) {
    	    		if ((!(fileEntry.getName().contains(".zip")))&&(!(fileEntry.getName().equalsIgnoreCase("instrumentManifest.csv")))){
    		            //System.out.println(fileEntry.getName());  
    	    			String fileShortName = fileEntry.getName().replace(".xls", "");
    		            //System.out.println(fileShortName);
    		            Process p=Runtime.getRuntime().exec("cmd /c C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\scripts\\XLStoCSV.vbs C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\zips\\"+fileShortName+".xls C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\"+fileShortName+".csv");
    	    		}
    	    }	
             

        }
        catch(IOException e1) {e1.printStackTrace();} 
	}
	public static void main(String[] args){
		PrepareMasterFiles setup = new PrepareMasterFiles();
		setup.processNewData();
		System.out.println(("hi"));
	}

}
