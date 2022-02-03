/*
 * this class contains the schduler data which will be shared with both f the floor and elevator 
 */
public class SchedulerData {
	
	private boolean elevatorIndication = false; // this is for the lamp in elevator initially ti is off but then the elevator will set it on them it can be turn off again by the scheduler
	private boolean floorIndication = false;
	public enum motorDirection{UP, DOWN}
	public enum doorState{OPEN, CLOSE}
	
	public boolean isElevatorIndicated() {
		return elevatorIndication;
	}
	
	public void setElevatorIndication (boolean elevatorIndication) {
		this.elevatorIndication = elevatorIndication;
	}
	
	public boolean isFloorIndicated() {
		return floorIndication;
	}
	
	public void setIndication (boolean floorIndication) {
		this.floorIndication = floorIndication;
	}

	
}
