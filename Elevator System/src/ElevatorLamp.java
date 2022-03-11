public class ElevatorLamp {
	private boolean lit;


	public ElevatorLamp() {
	
		this.lit = false;
	}
	
	
	public void turnOff() {
		this.lit = false;
		System.out.println( " lamp has been turned off.");
	}

	
	public boolean isOn() {
		return this.lit;
	}

	
	public void turnOn() {
		this.lit = true;
		System.out.println("Elevator's floor number " + ": lamp has been turned on!");
	}
}
