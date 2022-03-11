import java.util.ArrayList;

/**
 * ElevatorSubsystem.java
 * 
 * elevator subsystem thread will try to get requests from scheduler while not
 * operating upon receiving requests, moves elevator accordingly
 * 
 * @author Zinah, Mack, Vilmos
 *
 */
public class Elevator implements Runnable {

	private SchedulerSubsystem scheduler;

	private int elevatorID;
	private int currentFloor;
	private boolean motorOperating;
	private ArrayList<FloorMovementData> elevatorData;
	public State currentState;

	private FloorRequest floorRequest;

	enum State {
		STILL, MOVING
	}

	/**
	 * Constructor for Elevator Subsystem
	 * 
	 * @param scheduler,       scheduler thread
	 * @param elevatorId,      the ID of the elevator
	 * @param floorRequest, the floor where the request was made
	 */
	public Elevator(int elevatorId, FloorRequest floorRequest) {
		// Shared
		this.floorRequest = floorRequest;

		this.elevatorID = elevatorId;
		this.motorOperating = false;
		this.currentFloor = 0;// default ground floor
		currentState = State.STILL;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public FloorRequest getElevatorRequest() {
		return floorRequest;
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
	 * 
	 * @param destinationFloor, floor to move elevator to
	 */
	public void moveTo(int destinationFloor) {
		/* if motor is running, Doors must be closed */
		currentState = State.MOVING; // ensure the state Doors Closed is active
		if (currentState == State.MOVING) {
			System.out.println("Doors are now closed. Elevator MOVING. ");
		}
		// Check if motor is running
		while (isMotorOperating()) {
			// wait until motor stops
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}

		// Checks where the Elevator is
		// If Elevator at destination
		if (currentFloor == destinationFloor) {
			System.out.println("Elevator already at: " + destinationFloor);

		} else {
			System.out.println("Elevator moving from: " + currentFloor + " to: " + destinationFloor);
			motorsOn();
			// Move number of floors
			moveElevator(Math.abs(currentFloor - destinationFloor));
			motorOff();
			// For each move currentFloor gets changed here
			currentFloor = destinationFloor;
			//floor request IS SERVICED. CLEAR IT.
			floorRequest.clear();
			System.out.println("Elevator reached: " + destinationFloor);
		}
		currentState = State.STILL;
		// Opens door
		// openDoor(destinationFloor);
		scheduler.setElevatorResponse("Elevator reached: " + destinationFloor);
		System.out.println("Elevator Door opened at: " + destinationFloor);
	}

	/**
	 * Simulates elevator moving floors
	 * 
	 * @param numberOfFloors, floors that elevator will move
	 */
	private void moveElevator(int numberOfFloors) {
		// delay
		try {
			for (int i = 0; i < numberOfFloors; i++) {
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
		}
	}

	
	//Controller ELEVAGTOR SYS <--floorRequest 1--> ELV 1
	//Controller ELEVAGTOR SYS <--floorRequest 2--> ELV 2
	//Controller ELEVAGTOR SYS <--floorRequest 3--> ELV 3
	@Override
	public void run() {
		while (true) {
			synchronized (floorRequest) {
				// CHECK IF any request to a floor
				if (floorRequest.hasRequest()) {
					// Move to requested floor.
					moveTo(floorRequest.getFloor());
					floorRequest.notifyAll();
				} else {
					try {
						// NO REQUEST WAIT
						System.out.println("ElevatOR " + elevatorID + " WAITING...");
						floorRequest.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} // synchronized
		}//while
	}

	public boolean isMotorOperating() {
		return motorOperating;
	}
}