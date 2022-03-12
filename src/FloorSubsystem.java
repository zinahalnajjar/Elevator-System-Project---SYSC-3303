

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * FloorSubsyste
 * 
 * floor subsystem thread that will send
 * ------old code-------- 
 * @author Zinah, Mack, Vilmos
 *
 */

/*
 * Floor subsystem sends the data read from the input file to the scheduler subsystem
 * receive commands to turn the floor button lamps off 
 */
public class FloorSubsystem implements Runnable  {
	
	private SchedulerSubsystem sc; 
	private ArrayList<FloorMovementData> sentInfo;
	private FloorRequestData floorRequestData;

	/**
	 * base constructor
	 * @param sc, scheduler thread
	 * @param floorRequestData, information to be passed to floor
	 */
	public FloorSubsystem(SchedulerSubsystem sc, FloorRequestData floorRequestData) {
		this.sc = sc;
		this.floorRequestData = floorRequestData;
		this.sentInfo = new ArrayList<FloorMovementData>();
		
	}
	
	/**
	 * takes a text file and converts it into requests for the floor 
	 * @return floorInfo, contains the requests as an ArrayList
	 */
	public static ArrayList<FloorMovementData> getInfo(){ 
		ArrayList<FloorMovementData> floorInfo = new ArrayList<>();	
		try {
			File fileReader = new File("./src/InputInformation.txt");
			Scanner scanner = new Scanner(fileReader);
			while(scanner.hasNext()) {
				String line = null;
				try {
					line = scanner.nextLine();
					String[] tokens = line.split(",");
					Integer originFloor = Integer.valueOf(tokens[1]);
					LocalTime time = LocalTime.parse(tokens[0]);
					Integer destinationFloor = Integer.valueOf(tokens[3]);
					Boolean goingUp = Boolean.valueOf(tokens[2]);
					FloorMovementData fD = new FloorMovementData(time, originFloor, destinationFloor, goingUp);
					floorInfo.add(fD);
				} catch(Exception e) {
					System.out.println("Error: INVALID File format. Ignoring line: " + line);
				}
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
		} 
		
		return floorInfo;
		
	}

	/**
	 * sends the request to scheduler
	 * @param floorRequest, floorRequest to be sent to scheduler
	 */
	private void sendFloorRequest(FloorMovementData floorRequest) {
		synchronized (floorRequestData) {
    		while(floorRequestData.getFloorRequest() != null) {
    			//wait until request data is cleared
    			try {
    				//System.out.println("Awaiting FloorRequest to be Completed....");
    				floorRequestData.wait();
    			} catch (InterruptedException e) {
    				System.err.println(e);
    			}
    		}
    		floorRequestData.setFloorRequest(floorRequest);
    		System.out.println("Floor up/down LAMP ON.");
    		floorRequestData.notifyAll();
		}//synchronized
		
	}

	/**
	 * sets the elevators response
	 * @param elevatorResponse, elevators response
	 */
	public void setElevatorResponse(ResponseData elevatorResponse) {
		System.out.println("Floor Received ElevatorResponse: " + elevatorResponse.getResponse());
		System.out.println("Floor up/down LAMP OFF");
		
	}
	
	@Override
	public void run() {
		ArrayList<FloorMovementData> lines = getInfo();
		System.out.println("Got the Information from the Input File!");
		//Send ONE request AT A TIME.
		for (FloorMovementData floorRequest : lines) {
			System.out.println("--------------------");
			System.out.println("Sending Request to Scheduler...");
//			sc.putRequestFromFloor(floorRequest);
			sendFloorRequest(floorRequest);
			//SIMULATE DELAY between elevator requests.
			try {
//				System.out.println("Waiting for Passenger at Floor...");
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				
			}
			System.out.println("--------------------X");
		}

	}

}