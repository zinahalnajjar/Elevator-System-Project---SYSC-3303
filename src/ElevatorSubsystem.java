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
	private ArrayList<FloorMovementData> elevatorData;
	private FloorRequest elevatorRequest;
	public State currentState;
	
	enum State {
		STILL, MOVING
	}
	
	/**
	 * Constructor for Elevator Subsystem
	 * @param scheduler, scheduler thread
	 * @param elevatorId, the ID of the elevator
	 * @param elevatorRequest, the floor where the request was made
	 */
	public ElevatorSubsystem(SchedulerSubsystem scheduler, int elevatorId, FloorRequest elevatorRequest) {
    	//Shared with SchedulerSubsystem
		this.elevatorRequest = elevatorRequest;
		
		this.scheduler = scheduler;
		this.elevatorID = elevatorId;
		this.motorOperating = false;
		this.currentFloor = 0;//default ground floor
		currentState = State.STILL;
	}
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
		/* if motor is running, Doors must be closed */
		currentState = State.MOVING; // ensure the state Doors Closed is active
		if(currentState == State.MOVING) {
			System.out.println("Doors are now closed. Elevator MOVING. ");
		}
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
		currentState = State.STILL;
		//Opens door
		//openDoor(destinationFloor);
		scheduler.setElevatorResponse("Elevator reached: " + destinationFloor);
		System.out.println("Elevator Door opened at: " + destinationFloor);
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