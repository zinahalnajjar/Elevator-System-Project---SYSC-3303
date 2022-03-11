/**
 * FloorRequestData.java
 * 
 * Wrapper class to contain request data.
 * @author Zinah, Mack, Vilmos
 *
 */
public class FloorRequestData {

	private FloorMovementData floorRequest;

	/**
	 * base constructor
	 */
	public FloorRequestData() {

	}

	/**
	 * setter for floor request
	 * @param floorRequest, floor request data
	 */
	public synchronized void setFloorRequest(FloorMovementData floorRequest) {
		this.floorRequest = floorRequest;
	}

	/**
	 * getter for floor request
	 * @return floorRequest, floor request data
	 */
	public synchronized FloorMovementData getFloorRequest() {
		return floorRequest;
	}

	/**
	 * clears data inside floorRequest
	 */
	public synchronized void clearFloorRequest() {
		this.floorRequest = null;
	}
}
