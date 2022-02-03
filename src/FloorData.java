/*
 *  This class contains the data associated with the floor system 
 *  this data will be shared between the floor and the scheduler
 */

import java.time.LocalTime;

public class FloorData {
	private LocalTime time;
	private int currentFloor;
	private int destinationFloor; 
	private boolean goingUp;
	
	
	/*
	 * constructor 
	 *
	 */
	
	public FloorData(LocalTime time, int currentfloor, int destinationFloor, boolean goingUp) {
		this.currentFloor = currentFloor; 
		this.time = time;
		this.destinationFloor = destinationFloor;
		this.goingUp = goingUp;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	
	public void setCurrentFloor(int currentFloor ) {
		 this.currentFloor = currentFloor;
	}
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	public void setDestinationFloor(int destinationFloor) {
		 this.destinationFloor = destinationFloor;
	}
	
	public LocalTime getLocalTime() {
		return time;
	}
	
	public void setLocalTime(LocalTime time) {
		this.time = time;
		
	}
	public boolean getGoingUp() {
		return goingUp;
	}
	
	public void setGoingUp(boolean goingUp) {
		this.goingUp = goingUp;
	}
	
	
}
