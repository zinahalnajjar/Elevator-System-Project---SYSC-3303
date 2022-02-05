import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Floor subsystem sends the data read from the input file to the schduler subsystem
 * receive commands to turn the floor button lamps off 
 * will have the above methods plus run
 */
public class FloorSubsystem implements Runnable  {
	
	private SchedulerSubsystem sc; 
	private ArrayList<FloorRequest> sentInfo;
	private FloorRequestData floorRequestData;

	/*
	 * constructor 
	 */
	public FloorSubsystem(SchedulerSubsystem sc, FloorRequestData floorRequestData) {
		this.sc = sc;
		this.floorRequestData = floorRequestData;
		this.sentInfo = new ArrayList<FloorRequest>();
		
	}
	
	/*
	 * add the info in input file  
	 */
	/*public ArrayList<FloorData> addInfo() throws IOException{
		sentInfo.addAll(InputReader.getInfo("src/InputInformation.txt"));
		return sentInfo;
	}
	*/
	
	public static ArrayList<FloorRequest> getInfo(){ //changed from InformationHandler to FloorData
		ArrayList<FloorRequest> floorInfo = new ArrayList<>();	//changed from InformationHandler to FloorData
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
					FloorRequest fD = new FloorRequest(time, originFloor, destinationFloor, goingUp);
					floorInfo.add(fD);
				} catch(Exception e) {
					System.out.println("Error: INVALID File format. Ignoring line: " + line);
				}
				
			}
			//String lineReader = fileReader.readLine();
			//FloorData floorRequest = new FloorData(lineReader.split(","));
			//floorInfo.add(floorRequest);
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
		} 
		
		return floorInfo;
		
	}

	
	
	@Override
	public void run() {
		ArrayList<FloorRequest> lines = getInfo();
		System.out.println("Got the Information from the Input File!");
		//Send ONE request AT A TIME.
		for (FloorRequest floorRequest : lines) {
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

	private void sendFloorRequest(FloorRequest floorRequest) {
		synchronized (floorRequestData) {
    		while(floorRequestData.getFloorRequest() != null) {
    			//wait until request data is cleared
    			try {
//    				System.out.println("Awaiting FloorRequest to be Completed....");
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

	public void setElevatorResponse(ResponseData elevatorResponse) {
		System.out.println("Floor Received ElevatorResponse: " + elevatorResponse.getResponse());
		System.out.println("Floor up/down LAMP OFF");
		
	}

}
