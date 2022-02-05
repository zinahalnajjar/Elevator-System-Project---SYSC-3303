public class ElevatorRequest {
	// Keep it as Integer so that it can be set to null when we call 'clear()'.
	private Integer floor;

	/*
	 * constructor
	 *
	 */

	public ElevatorRequest() {
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public void clear() {
		this.floor = null;
	}
}
