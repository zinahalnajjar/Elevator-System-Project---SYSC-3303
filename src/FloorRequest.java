import java.time.LocalTime;

//Name Changed from FloorData to  FloorRequest
public class FloorRequest {
	private LocalTime time;
	private int originFloor;
	private int destinationFloor; 
	private boolean goingUp;
	private boolean lampOn; // this turns on when the floor button is pressed 
	
	
	/**
	 * 
	 * @param time
	 * @param originFloor
	 * @param destinationFloor
	 * @param goingUp
	 */
	public FloorRequest(LocalTime time, int originFloor, int destinationFloor, boolean goingUp) {
		this.time = time;
		this.originFloor = originFloor;
		this.destinationFloor = destinationFloor;
		this.goingUp = goingUp;
		this.lampOn = false;
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
	
	
	
	
}
