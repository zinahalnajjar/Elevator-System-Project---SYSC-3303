public class ElevatorMotor {

	private boolean motorOperating;
	
	public ElevatorMotor() {
		this.motorOperating = false;
	}
	
	/**
	 * Turns on the motor
	 */
	public synchronized void motorsOn() {
		motorOperating = true;
	}

	/**
	 * Turns motors off 
	 */
	public synchronized void motorOff() { 
		motorOperating = false;
		notifyAll();
	}
	
	
}
