/*
 *  will read the information from the inputInforamtion.txt file
 *   and use the setters to set the data info
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
	
	private ArrayList<String> inputFloorData = new ArrayList<>();
	
	
	private ArrayList<String> floorData = new ArrayList<>();
	
	/*
	 * this method reads the input file and slpit it into parts
	 * sets the floor data  
	 */
	public void readInfo (File filePath)throws IOException{
		// read the input.txt file 
		Scanner scanner = new Scanner(filePath);
		// split the string
		while(scanner.hasNext()) {
			String line = scanner.nextLine();
			String [] floorInfo = line.split(","); // split by ","
			// set each part to a variable of the required type
			LocalTime time = LocalTime.parse(floorInfo[0]);
			int currentFloor = Integer.valueOf(floorInfo[1]);
			boolean goingUp = Boolean.valueOf(floorInfo[2]);
			int destinationFloor = Integer.valueOf(floorInfo[3]);
			// use the constructor to create an instance of floorData
			FloorData floorData = new FloorData(time, currentFloor, destinationFloor, goingUp);
			//using the instance of floorData to call the setters in order to set the data 
			floorData.setCurrentFloor(currentFloor);
			floorData.setDestinationFloor(destinationFloor);
			floorData.setGoingUp(goingUp);
			floorData.setLocalTime(time);
		}
		
	}

	
		
}
