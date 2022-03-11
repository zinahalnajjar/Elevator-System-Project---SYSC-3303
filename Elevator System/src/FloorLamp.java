public class FloorLamp {
	
	private boolean lit;
	
	/**
	 * Creates a new FloorLamp
	 */
	public FloorLamp(int floorNum) {
		
		this.lit = false;
	}
	
	/**
	 * Turns the light on
	 */
	public void turnOn() {
		this.lit = true;
		System.out.println( "lamp has been turned on!");
	}
	
	/**
	 * Turns the light off
	 */
	public void turnOff() {
		this.lit = false;
		System.out.println("lamp has been turned off.");
	}
	
	/**
	 * Checks if the light is on
	 * @return True of the light is on, false otherwise
	 */
	public boolean isOn() {
		return this.lit;
	}
	
	
}
