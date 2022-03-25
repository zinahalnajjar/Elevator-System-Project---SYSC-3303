/*
 * this class is responsible for setting the information read from the file 
 * @author Zinah 
 */


import java.time.LocalTime;

//Name Changed from FloorData to  FloorRequest
public class FloorMovementData {
	private LocalTime time;
	private int originFloor;
	private int destinationFloor; 
	private boolean goingUp;
	private boolean lampOn; // this turns on when the floor button is pressed 
	private int error;
	
	
	
	/**
	 * 
	 * @param time
	 * @param originFloor
	 * @param destinationFloor
	 * @param goingUp
	 * @param error
	 */
	public FloorMovementData(LocalTime time, int originFloor, int destinationFloor, boolean goingUp, int error) {
		this.time = time;
		this.originFloor = originFloor;
		this.destinationFloor = destinationFloor;
		this.goingUp = goingUp;
		this.lampOn = false;
		this.error = error;
	}

	public int getOriginFloor() {
		return originFloor;
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
	
	/* added a getError method */
	public int getError() {
		return error;
	}
	
	public String  toString() {
		String printObj = " ";
		LocalTime time = this.time;
		int originFloor =this.originFloor;
	    int destinationFloor = this.destinationFloor; 
	    boolean goingUp = this.goingUp;
	    boolean lampOn = this.lampOn;
	    printObj = time.toString()+Integer.toString(originFloor)+ Integer.toString(destinationFloor) + String.valueOf(goingUp)+ String.valueOf(lampOn) + String.valueOf(error); 
		return printObj;
		
		
	}
	
	
}
