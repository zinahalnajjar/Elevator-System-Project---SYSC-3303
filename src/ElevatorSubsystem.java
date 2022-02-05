import java.util.ArrayList;

/**
 * elevator subsystem receives data from the scheduler 
 * performs instructions from data
 * @author vilmos
 *
 */
public class ElevatorSubsystem implements Runnable {
	
	private SchedulerSubsystem scheduler;

	private int elevatorID;
	private int currentFloor;
	private boolean motorOperating;
	private boolean doorsOpen;
	private ArrayList<FloorRequest> elevatorData;
	private ElevatorRequest elevatorRequest;
	
	/**
	 * Constructor
	 * @param elevatorRequest2 
	 */
	public ElevatorSubsystem(SchedulerSubsystem scheduler, int elevatorId, ElevatorRequest elevatorRequest) {
    	//Shared with SchedulerSubsystem
		this.elevatorRequest = elevatorRequest;
		
		this.scheduler = scheduler;
		this.elevatorID = elevatorId;
		this.motorOperating = false;
		this.doorsOpen = false;
		this.currentFloor = 0;//Ground  
	}

	/**
	 * Opens the doors
	 */
	public void openDoors() {doorsOpen = true;}
	
	/**
	 * Closes the doors
	 */
	public void closeDoors() {doorsOpen = false;}
	
	/**
	 * Turns on the motor
	 */
	public synchronized void motorsOn() {
		motorOperating = true;
	}

	public synchronized void motorOff() { 
		motorOperating = false;
		notifyAll();
	}
	
	@Override
	public void run() {
        while(true) {
        	synchronized (elevatorRequest) {
        		while(elevatorRequest.getFloor() == null) {
        			//wait until request data arrrives
        			try {
        				System.out.println("Elevator Awaiting....");
        				elevatorRequest.wait();
        			} catch (InterruptedException e) {
        				System.err.println(e);
        			}
        		}//while
        		moveTo(elevatorRequest.getFloor());
        		elevatorRequest.clear();
        		elevatorRequest.notifyAll();
			}//synchronized
        	
        }//while (true)

	}
	//old 
//	public void run() {
//		// check for requests from scheduler, then handle requests
//		while(true) {
//			
//				elevatorData = scheduler.getInfoForElevator();
//				System.out.println("Data available");
//				System.out.println("----------------"); 
//				System.out.println("Current Floor: " + elevatorData.get(0).getCurrentFloor() + "/n");
//				System.out.println("Destination Floor: " + elevatorData.get(0).getDestinationFloor() + "/n");
//				System.out.println("Current time: " + elevatorData.get(0).getLocalTime() + "/n");
//				scheduler.putElevatorResponse(elevatorData);
//			
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//		}
//		
//	}

	//added
	public void moveTo(int destinationFloor) {
		//Check if motor is running
		while (motorOperating) {
			//wait until motor stops
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}
		
		//	Checks where the Elevator is.
//		If Elevator at Dest
		if(currentFloor == destinationFloor) {
			System.out.println("Elevator already at: " + destinationFloor);
		}
		else{
			System.out.println("Elevator moving from: " + currentFloor + " to: " + destinationFloor);
			motorsOn();
			//Move number of floors
			moveElevator(Math.abs(currentFloor - destinationFloor));
			motorOff();
			//For each move currentFloor gets changed here.
			currentFloor = destinationFloor;
			System.out.println("Elevator reached: " + destinationFloor);
		}
		//		Opens door.
		openDoor(destinationFloor);
		scheduler.setElevatorResponse("Elevator reached: " + destinationFloor);
	}

	private void openDoor(int floor) {
		System.out.println("Door opens at: " + floor);
	}

	//added
	//simulate elevator move with required speed
	private void moveElevator(int numberOfFloors) {
		//delay
		try {
			for (int i = 0; i < numberOfFloors; i++) {
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
		}
	}

	private void openDoor() {
		System.out.println("Door opened");
		
	}

}
