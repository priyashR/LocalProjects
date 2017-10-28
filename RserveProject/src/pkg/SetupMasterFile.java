package pkg;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SetupMasterFile {

	public void addVolumeData(){	
		
		try {
		
			Path path = Paths.get("C:\\Users\\priyash.ramawthar\\Dropbox\\trader\\appData\\masterdata\\ADI.txt");
			List<String> lines;
			
			lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			
			
			for (int i = 0; i < lines.size(); i++){
				lines.set(i, lines.get(i) +",100");
			}
			

			Files.write(path, lines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
		e.printStackTrace();
		}	
	}
	
	
	public static void main(String [] args){
		SetupMasterFile smf = new SetupMasterFile();
		smf.addVolumeData();
	}
}
