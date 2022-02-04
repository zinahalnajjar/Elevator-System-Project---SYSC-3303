import java.util.ArrayList;

/**
 * elevator subsystem receives data from the scheduler 
 * performs instructions from data
 * @author vilmos
 *
 */
public class ElevatorSubsystem implements Runnable {
	
	private SchedulerSubsystem scheduler;

	private int elevatorID;
	private boolean motorOperating;
	private boolean doorsOpen;
	private ArrayList<FloorData> elevatorData;
	
	/**
	 * Constructor
	 */
	public ElevatorSubsystem(SchedulerSubsystem scheduler, int elevatorId) {
		this.scheduler = scheduler;
		this.elevatorID = elevatorId;
		this.motorOperating = false;
		this.doorsOpen = false;
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
	public void motorsOn() { motorOperating = true;}
	
	public void motorOff() { motorOperating = false;}
	
	@Override
	public void run() {
		// check for requests from scheduler, then handle requests
		while(true) {
			
			if(motorOperating == false) {
				System.out.println("Data available");
				System.out.println("----------------"); 
				elevatorData = scheduler.getInfoForElevator();
				System.out.println("Current Floor: " + elevatorData.get(0).getCurrentFloor() + "/n");
				System.out.println("Destination Floor: " + elevatorData.get(0).getDestinationFloor() + "/n");
				System.out.println("Current time: " + elevatorData.get(0).getLocalTime() + "/n");
			}
			
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
