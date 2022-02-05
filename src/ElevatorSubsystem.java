import java.util.ArrayList;

/**
 * ElevatorSubsystem.java
 * 
 * elevator subsystem thread will try to get requests from scheduler while not operating
 * upon receiving requests, moves elevator accordingly
 * @author Zinah, Mack, Vilmos
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
	 * Constructor for Elevator Subsystem
	 * @param scheduler, scheduler thread
	 * @param elevatorId, the ID of the elevator
	 * @param elevatorRequest, the floor where the request was made
	 */
	public ElevatorSubsystem(SchedulerSubsystem scheduler, int elevatorId, ElevatorRequest elevatorRequest) {
    	//Shared with SchedulerSubsystem
		this.elevatorRequest = elevatorRequest;
		
		this.scheduler = scheduler;
		this.elevatorID = elevatorId;
		this.motorOperating = false;
		this.doorsOpen = false;
		this.currentFloor = 0;//default ground floor
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

	/**
	 * Turns motors off 
	 */
	public synchronized void motorOff() { 
		motorOperating = false;
		notifyAll();
	}
	

	/**
	 * moves elevator to desired floor
	 * @param destinationFloor, floor to move elevator to
	 */
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
		
		//Checks where the Elevator is
		//If Elevator at destination
		if(currentFloor == destinationFloor) {
			System.out.println("Elevator already at: " + destinationFloor);
		}
		else{
			System.out.println("Elevator moving from: " + currentFloor + " to: " + destinationFloor);
			motorsOn();
			//Move number of floors
			moveElevator(Math.abs(currentFloor - destinationFloor));
			motorOff();
			//For each move currentFloor gets changed here
			currentFloor = destinationFloor;
			System.out.println("Elevator reached: " + destinationFloor);
		}
		//Opens door
		openDoor(destinationFloor);
		scheduler.setElevatorResponse("Elevator reached: " + destinationFloor);
	}

	/**
	 * Opens the elevators doors
	 * @param floor, floor where the doors are to open 
	 */
	private void openDoor(int floor) {
		System.out.println("Door opens at: " + floor);
	}


	/**
	 * Simulates elevator moving floors
	 * @param numberOfFloors, floors that elevator will move
	 */
	private void moveElevator(int numberOfFloors) {
		//delay
		try {
			for (int i = 0; i < numberOfFloors; i++) {
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
        while(true) {
        	synchronized (elevatorRequest) {
        		while(elevatorRequest.getFloor() == null) {
        			//wait until request data arrives
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
        	
        }

	}
}
