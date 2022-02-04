
public class ElevatorSubsystem implements Runnable {
	
	private int elevatorID;
	private int currentFloor;
	private int destinationFloor;
	private boolean lampOn;
	private SchedulerSubsystem scheduler;
	private boolean motorOperating;
	private boolean doorsOpen;
	
	
	/**
	 * Constructor
	 */
	public ElevatorSubsystem(SchedulerSubsystem scheduler, int elevatorId) {
		this.scheduler = scheduler;
		this.elevatorID = elevatorId;
		this.currentFloor = 0;
		this.destinationFloor = 0;
		this.lampOn = false;
		this.motorOperating = false;
		this.doorsOpen = false;
	}
	
	/**
	 * method for receiving data and 
	 */
	public void handleRequest() {
	//for iteration 1 system will read input from floor and output it 
		System.out.println("data received");
		//receive info
	}
	
	//for iteration 1 this will do nothing
	public void sendData() {
		
	}
	

	/**
	 * Opens the doors
	 */
	public void openDoors() {doorsOpen = true;}
	
	/**
	 * Closes the doors
	 */
	public void closeDoors() {doorsOpen = false;}
	
	/**
	 * Turns on the motor
	 */
	public void motorsOn() { motorOperating = true; lampOn = true;}
	
	public void motorOff() { motorOperating = false; lampOn = false;}
	
	@Override
	public void run() {
		// check for requests from scheduler, then handle requests
		while(true) {
			try {
				Thread.sleep(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(scheduler.hasReceived() && motorOperating == false) {
				handleRequest();
				
			}
		}
		
	}

}
