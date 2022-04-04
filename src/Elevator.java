/**
 * ElevatorSubsystem.java
 * 
 * elevator subsystem thread will try to get requests from scheduler while not
 * operating upon receiving requests, moves elevator accordingly
 * 
 * @author Zinah, Mack
 *
 */
public class Elevator implements Runnable {

	//load time in milliseconds
	private static final int LOAD_TIME = 50;

	//unload time in milliseconds
	private static final int UNLOAD_TIME = 50;

	private int elevatorID;

	private int currentFloor;
	private Motor motor = new Motor();
	public State currentState;

	private ArrivalSensor arrivalSensor;

	private FloorRequest floorRequest;

	private boolean outOfService;

	private boolean delayed;

	enum State {
		STILL, MOVING
	}

	/**
	 * Constructor for Elevator Subsystem
	 * 
	 * @param scheduler,    scheduler thread
	 * @param elevatorId,   the ID of the elevator
	 * @param floorRequest, the floor where the request was made
	 */
	public Elevator(int elevatorId, FloorRequest floorRequest) {
		// Shared
		this.floorRequest = floorRequest;

		this.elevatorID = elevatorId;
		this.currentFloor = 0;// default ground floor
		currentState = State.STILL;

		this.arrivalSensor = new ArrivalSensor(elevatorId);
		printCurrentState();
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
		motor.motorsOn();
	}

	/**
	 * Turns motors off
	 */
	public synchronized void motorOff() {
		motor.motorOff();
		notifyAll();
	}

	/**
	 * moves elevator to desired floor
	 * 
	 * @param destinationFloor, floor to move elevator to
	 */
	public void moveTo(int destinationFloor) {
		/* if motor is running, Doors must be closed */
		// Check if motor is running

		while (isMotorOperating()) {
			if (currentState == State.MOVING) {
				// System.out.println("Elevator " + elevatorID + " is ON THE MOVE...");
				Output.print("Elevator", "moveTo", Output.INFO, "Elevator " + elevatorID + " is ON THE MOVE...");

			}
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
			// System.out.println("Elevator " + elevatorID + " already at: " +
			// destinationFloor);
			Output.print("Elevator", "moveTo", Output.INFO,
					"Elevator " + elevatorID + " already at: " + destinationFloor);

		} else {
			// System.out.println("Elevator " + elevatorID + " moving from: " + currentFloor
			// + " to: " + destinationFloor);
			Output.print("Elevator", "moveTo", Output.INFO,
					"Elevator " + elevatorID + " moving from: " + currentFloor + " to: " + destinationFloor);

			motorsOn();
			// moving to the destination floor
			// Move number of floors
			moveElevatorSimulate(currentFloor, destinationFloor);
			motorOff();
			currentState = State.STILL;
			// For each move currentFloor gets changed here
			currentFloor = destinationFloor;// now the elevator is at the user floor
			// System.out.println("Elevator " + elevatorID + " reached: " +
			// destinationFloor);
			Output.print("Elevator", "moveTo", Output.INFO, "Elevator " + elevatorID + " reached: " + destinationFloor);

		}
		// floor request IS SERVICED. CLEAR IT.
		floorRequest.clearOriginFloor();
		currentState = State.STILL;
		// Opens door
		// openDoor(destinationFloor);
		// System.out.println("Elevator " + elevatorID + " Door opened at: " +
		// destinationFloor);
		Output.print("Elevator", "moveTo", Output.INFO,
				"Elevator " + elevatorID + " Door opened at: " + destinationFloor);

	}

	private void printCurrentState() {
		// System.out.println("Elevator " + elevatorID + " CURRENT STATE: " +
		// currentState);
		Output.print("Elevator", "currentState", Output.INFO,
				"Elevator " + elevatorID + " CURRENT STATE: " + currentState);

	}

	/**
	 * Simulates elevator moving floors
	 * 
	 * @param originFloor
	 * @param destinationFloor
	 * 
	 * @param numberOfFloors,  floors that elevator will move
	 */
	private void moveElevatorSimulate(int originFloor, int destinationFloor) {
		boolean goingUp = originFloor < destinationFloor;
		
		int numberOfFloors = Math.abs(originFloor - destinationFloor);
		currentState = State.MOVING; // set the state of the elevator car
		printCurrentState(); // print the current state machine of the elevator car
		// delay
		int moveTime = 0;
		int moveToNextFloorTimeMillis = 100;
		try {
			for (int i = 1; i <= numberOfFloors; i++) {
				Thread.sleep(moveToNextFloorTimeMillis);
				moveTime += moveToNextFloorTimeMillis;
				Output.print("Elevator", "TIMER", Output.INFO,
						"Elevator " + elevatorID + " ON THE MOVE for: " + moveTime + " milliseconds.");
				if(goingUp) {
					arrivalSensor.setReachedFloor(originFloor + i);
				}
				else {
					arrivalSensor.setReachedFloor(originFloor - i);
				}
			}

		} catch (InterruptedException e) {
		}
		currentState = State.STILL; // set the state of the elevator car
		printCurrentState(); // print the state of the elevator car
	}

	/**
	 * run method
	 */

	@Override
	public void run() {
		// REPEAT LISTENING FOR REQUEST AGAIN AND AGAIN
		while (true) {
			if (outOfService) {
				// End Elevator THREAD.
				Output.print("Elevator", "currentState", Output.INFO, "Elevator " + elevatorID + " THREAD STOPPED");
				return;
			}
			synchronized (floorRequest) {
				// check if any request is in queue
				while (!floorRequest.hasRequest()) {
					try {
						// NO REQUEST for floor. WAIT for a request
						// System.out.println("Elevator " + elevatorID + " WAITING...");
						Output.print("Elevator", "currentState", Output.INFO, "Elevator " + elevatorID + " WAITING...");

						floorRequest.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} // while

				// proceed
				// Move to origin floor.
				moveTo(floorRequest.getOriginFloor());
				floorRequest.clearOriginFloor();
				//Changed to measure loading time.
				
//				Output.print("Elevator", "currentState", Output.INFO,
//						"Elevator " + elevatorID + " BOARDING passengers...");
				load();

				// Move to dest floor.
				moveTo(floorRequest.getDestFloor());
				floorRequest.clearDestFloor();
				//Changed to measure unloading time.
				unload();
				
				floorRequest.notifyAll(); // NOTIFY Controller ELEVAGTOR SYS

			} // synchronized

			try {

				// System.out.println("Elevator " + elevatorID + " sleep delay");
				Output.print("Elevator", "currentState", Output.INFO, "Elevator " + elevatorID + " sleep delay");

				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // while
	}


	private void load() {
		Output.print("Elevator", "currentState", Output.INFO,
				"Elevator " + elevatorID + " BOARDING passengers..." + LOAD_TIME + " milliseconds.");

		try {
			Thread.sleep(LOAD_TIME);
		} catch (InterruptedException e) {
		}

	}

	private void unload() {
		Output.print("Elevator", "currentState", Output.INFO,
				"Elevator " + elevatorID + " UNLOADING passengers..." + UNLOAD_TIME + " milliseconds.");

		try {
			Thread.sleep(UNLOAD_TIME);
		} catch (InterruptedException e) {
		}

	}


	public boolean isOutOfService() {
		return outOfService;
	}

	public void setOutOfService(boolean outOfService) {
		this.outOfService = outOfService;

	}

	/**
	 * getter for elevatorID
	 * 
	 * @return
	 */

	public int getElevatorID() {
		return elevatorID;
	}

	/**
	 * setter for motor flag
	 * 
	 * @return
	 */
	public boolean isMotorOperating() {
		return motor.isMotorOperating();
	}

	public boolean isDelayed() {
		return this.delayed;
	}

	public void setDelayed(boolean delayed) {
		this.delayed = delayed;
	}
}