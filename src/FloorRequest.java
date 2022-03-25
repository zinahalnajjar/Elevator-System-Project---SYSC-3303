/**
 * ElevatorRequest.java
 * 
 * 
 * @author Zinah, Mack, Vilmos
 *
 */
public class FloorRequest {
	// Keep it as Integer so that it can be set to null when we call 'clear()'.
	private Integer originFloor;
	private Integer destFloor;
	private int elevatorID;
	private Integer error;

	/**
	 * base constructor
	 */
	public FloorRequest() {
	}

	/**
	 * getter for floor
	 * 
	 * @return floor, value of floor
	 */
	public Integer getOriginFloor() {
		return originFloor;
	}

	/**
	 * setter for floor
	 * 
	 * @param floor, value of floor
	 */
	public void setOriginFloor(Integer originFloor) {
		this.originFloor = originFloor;
	}

	public Integer getDestFloor() {
		return destFloor;
	}

	public void setDestFloor(Integer destFloor) {
		this.destFloor = destFloor;
	}
	
	/**
	 * Making getter and setter for error
	 * @return error
	 */
	public Integer getTheError() {
		return error;
	}
	
	public void setTheError(Integer error) {
		this.error = error;
	}

	/**
	 * clears value of OriginFloor
	 */
	public void clearOriginFloor() {
		this.originFloor = null;
	}

	/**
	 * clears value of OriginFloor
	 */
	public void clearDestFloor() {
		this.destFloor = null;
	}

	/**
	 * Returns true, if there is any floor request, otherwise false. NOT SURE IF WE GOTTA DO SOMETHING ABOUT THE ERROR HERE
	 * 
	 * @return
	 */
	public boolean hasRequest() {
		return (this.originFloor != null) || (this.destFloor != null);
	}
}