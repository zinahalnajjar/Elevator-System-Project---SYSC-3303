/**
 * ElevatorRequest.java
 * 
 * 
 * @author Zinah, Mack, Vilmos
 *
 */
public class ElevatorRequest {
	// Keep it as Integer so that it can be set to null when we call 'clear()'.
	private Integer floor;

	/**
	 * base constructor
	 */
	public ElevatorRequest() {
	}

	/**
	 * getter for floor
	 * @return floor, value of floor
	 */
	public Integer getFloor() {
		return floor;
	}

	/**
	 * setter for floor
	 * @param floor, value of floor
	 */
	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	/**
	 * clears value of floor
	 */
	public void clear() {
		this.floor = null;
	}

	/**
	 * Returns true, if there is any floor request, otherwise false.
	 * @return
	 */
	public boolean hasRequest() {
		return (this.floor != null);
	}
}