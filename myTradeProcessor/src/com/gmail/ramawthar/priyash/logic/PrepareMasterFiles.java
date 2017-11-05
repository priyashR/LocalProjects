package com.gmail.ramawthar.priyash.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareMasterFiles {
	
	private String inputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\";
	private String XLSinputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\xls\\";
	private String outputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\";
	private String manifest = "instrumentManifest.csv";
	private String scipt = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\scripts\\XLStoCSV.vbs";
	private static int maxLineCount = 1052;
	
	public void processNewData(){
		//convert files first
		// do this first, then the rest-> 
		//convertToCSV();
		
		//run this after conversion to csv
		
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	    		if ((!(fileEntry.getName().equalsIgnoreCase("xls")))&&(!(fileEntry.getName().equalsIgnoreCase("instrumentManifest.csv")))){
		            //System.out.println(fileEntry.getName());  
		            System.out.println((getInstrument(fileEntry.getName())));
		            generateFile(fileEntry,getInstrument(fileEntry.getName()));
	    		}
	    }
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
            int lineCount = 0;
            while (((readLine = b.readLine()) != null)&&(lineCount<maxLineCount)) {
            	if ((lineCount>0)&&(!(readLine.contains("...")))){//ignore the header and the last line
            	//System.out.println(readLine);
            		lines.add(processLine(readLine));
            	}
            	lineCount++;
            }
            b.close();
            
            
            // add the header
            lines.add("DATE,OPEN,CLOSE,HIGH,LOW,VOLUME");
            
            //then invert the list below:
            Collections.reverse(lines);
            
            Path path = Paths.get(outputPath+fileName+".txt");
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }	
		
	}
	
	private String processLine(String line){
		StringTokenizer defaultTokenizer = new StringTokenizer(line,",");	
		
		//1 date 
		String date = defaultTokenizer.nextToken();
		//2 closing
		String close = defaultTokenizer.nextToken();
		//3 high
		String high = defaultTokenizer.nextToken();
		//4 low
		String low = defaultTokenizer.nextToken();
		//5 volume
		String volume = defaultTokenizer.nextToken();
		//6 number of deals
		defaultTokenizer.nextToken();
		//7 rand value
		defaultTokenizer.nextToken();
		//8 perc move
		String percMove = defaultTokenizer.nextToken();
		
		String open = getOpen(close,percMove); // may change to have the current opening = previous close
		
		return date+","+open+","+close+","+high+","+low+","+volume;//+","+percMove;
	}
	
	private String getOpen(String close, String percMove){
		
		String moveNumber = percMove.replaceAll("%", "");
		boolean neg = false;
		
		if (moveNumber.contains("-"))
			neg = true;
		
		moveNumber = moveNumber.replaceAll("-", "");
		
		double intClose = Double.parseDouble(close);
		
		//BigDecimal decClose = new BigDecimal(close);
		
		Double perc = Double.parseDouble(moveNumber);
		
		System.out.println("perc: "+perc);
		
		
		
		Integer open;
		//need to fix the rounding issues
		if (neg){
			Double diff = 1.000000-(1.000000/100.000000*perc);
			open = (int) (intClose/diff);
			System.out.println("neg open: "+open);
			System.out.println("neg diff: "+diff);
		}else{
			Double diff = 1.000000+(1.000000/100.000000*perc);
			open = (int) (intClose/diff);
			System.out.println(" pos open: "+open);
			System.out.println("pos diff: "+diff);
		}
		
		
		return open.toString();
		
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
    		            //Process p=Runtime.getRuntime().exec("cmd /c C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\scripts\\XLStoCSV.vbs C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\zips\\"+fileShortName+".xls C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\"+fileShortName+".csv");
    	    			Process p=Runtime.getRuntime().exec("cmd /c "+scipt+" "+XLSinputPath+fileShortName+".xls "+inputPath+fileShortName+".csv");
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
