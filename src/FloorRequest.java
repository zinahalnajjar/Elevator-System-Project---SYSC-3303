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
	 * Returns true, if there is any floor request, otherwise false.
	 * 
	 * @return
	 */
	public boolean hasRequest() {
		return (this.originFloor != null) || (this.destFloor != null);
	}
}