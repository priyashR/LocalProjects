package com.gmail.ramawthar.priyash.logic;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.gmail.ramawthar.priyash.responses.ReturnClass;

public class PrepareMasterFiles {
	
	private String inputPath = "C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\masterFiles\\";
	private String manifest = "instrumentManifest.xls";
	
	public void processNewData(){
		
		File folder = new File(inputPath);
		//read the input directory
	    for (final File fileEntry : folder.listFiles()) {
	    		if ((!(fileEntry.getName().equalsIgnoreCase("zips")))&&(!(fileEntry.getName().equalsIgnoreCase("instrumentManifest.xls")))){
		            System.out.println(fileEntry.getName());  
		            getInstrument(fileEntry.getName());
	    		}
	    }		
	}
	
	private String getInstrument(String filename){
		
		try {
			
			String instrumentDesc = filename.substring(18); 
			System.out.println(instrumentDesc);
			/*
			Path path = Paths.get(inputPath+manifest);
			List<String> lines;
			
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	
			for (int i = 0; i <lines.size(); i++){
				//if (lines.get(i).){
					System.out.println(lines.get(i));
				//}
			}
			*/
			//keep it to around +-4 years of data max
			//if (lines.size()>1052)
			//	lines.remove(1);
			//Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "";
	}
	
	public static void main(String[] args){
		PrepareMasterFiles setup = new PrepareMasterFiles();
		setup.processNewData();
		System.out.println(("hi"));
	}

}
