import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Floor subsystem sends the data read from the input file to the schduler subsystem
 * receive commands to turn the floor button lamps off 
 * will have the above methods plus run
 */
public class FloorSubsystem implements Runnable  {
	
	private SchedulerSubsystem sc; 
	private ArrayList<FloorData> sentInfo;

	/*
	 * constructor 
	 */
	public FloorSubsystem(SchedulerSubsystem sc) {
		this.sc = sc;
		this.sentInfo = new ArrayList<FloorData>();
		
	}
	
	/*
	 * add the info in input file  
	 */
	/*public ArrayList<FloorData> addInfo() throws IOException{
		sentInfo.addAll(InputReader.getInfo("src/InputInformation.txt"));
		return sentInfo;
	}
	*/
	
	public static ArrayList<FloorData> getInfo(){ //changed from InformationHandler to FloorData
		ArrayList<FloorData> floorInfo = new ArrayList<>();	//changed from InformationHandler to FloorData
		try {
			File fileReader = new File("./src/InputInformation.txt");
			Scanner scanner = new Scanner(fileReader);
			while(scanner.hasNext()) {
				String[] tokens = scanner.nextLine().split(",");
				FloorData fD = new FloorData(tokens);
				floorInfo.add(fD);
			}
			//String lineReader = fileReader.readLine();
			//FloorData floorData = new FloorData(lineReader.split(","));
			//floorInfo.add(floorData);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
		} 
		
		return floorInfo;
		
	}

	
	
	@Override
	public void run() {
		ArrayList<FloorData> lines = getInfo();
		System.out.println("Got the Information from the Input File!");
		sc.putRequestFromFloor(lines);
		System.out.println("Information sent to Scheduler!");
		sc.getElevatorIntoFloor();
		System.out.println("All done!");
		
	}

}

