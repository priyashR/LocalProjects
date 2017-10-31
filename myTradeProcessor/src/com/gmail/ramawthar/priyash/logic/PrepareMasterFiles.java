package com.gmail.ramawthar.priyash.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareMasterFiles {
	
	private String inputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\";
	private String manifest = "instrumentManifest.csv";
	
	public void processNewData(){
		
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	    		if ((!(fileEntry.getName().equalsIgnoreCase("zips")))&&(!(fileEntry.getName().equalsIgnoreCase("instrumentManifest.csv")))){
		            //System.out.println(fileEntry.getName());  
		            System.out.println((getInstrument(fileEntry.getName())));
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
	
	public static void main(String[] args){
		PrepareMasterFiles setup = new PrepareMasterFiles();
		setup.processNewData();
		System.out.println(("hi"));
	}

}
