
import java.time.LocalTime;

public class FloorData {
	private LocalTime time;
	private int currentFloor;
	private int destinationFloor; 
	private boolean goingUp;
	private boolean lampOn; // this turns on when the floor button is pressed 
	/*
	 * constructor 
	 *
	 */
	
	public FloorData(String[] strings) {
		String[] inputinfo = strings;
		this.currentFloor = Integer.valueOf(inputinfo[1]); 
		this.time = LocalTime.parse(inputinfo[0]);
		this.destinationFloor =Integer.valueOf(inputinfo[3]);
		this.goingUp = Boolean.valueOf(inputinfo[2]);
		this.lampOn = false;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}
	
	
	public int getDestinationFloor() {
		return destinationFloor;
	}
	
	
	public LocalTime getLocalTime() {
		return time;
	}
	
	
	public boolean isGoingUp() {
		return goingUp;
	}
	
	
	
	
}

