
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class InputReader {
	
	
	 
	
	/*
	 * this method reads the input file and slpit it into parts
	 * sets the floor data  
	 */
	public static ArrayList<InformationHandler> getInfo (String filePath)throws IOException{
		ArrayList<InformationHandler> floorInfo = new ArrayList<>();
		BufferedReader fileReader = new BufferedReader(new FileReader(filePath));
		
		String lineReader;
		lineReader = fileReader.readLine();
		FloorData floorData = new FloorData(lineReader.split(","));
		floorInfo.add(floorData);
		return floorInfo;
		
		
	}
}
	