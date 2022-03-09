public class ElevatorDoor {
	
	
	private boolean doorOpen;
	
	public ElevatorDoor() {
		this.doorOpen = false;
	}
	
	/**
	 * Turns on the motor
	 */
	public synchronized void doorOpen() {
		doorOpen = true;
	}

	/**
	 * Turns motors off 
	 */
	public synchronized void doorClose() { 
		doorOpen = false;
		
	}

}
