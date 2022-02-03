/*
 * this class contains the schduler data which will be shared with both f the floor and elevator 
 */
public class SchedulerData {
	
	private boolean indication = false; // this is for the lamp in elevator initially ti is off but then the elevator will set it on them it can be turn off again by the scheduler
	public enum motorDirection{UP, DOWN}
	public enum doorState{OPEN, CLOSE}
	
	public boolean isIndicated() {
		return indication;
	}
	
	public void setIndication (boolean indication) {
		this.indication = indication;
	}

	
}
