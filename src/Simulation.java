public class Simulation {

	/**
	 * Main thread
	 */
	public static void main(String[] args) {
		//This will be REFACTORED with DataGramPacket info later.
		FloorRequestData floorRequestData = new FloorRequestData();
		
		//This will be REFACTORED with DataGramPacket info later.
		FloorRequest elevatorRequest = new FloorRequest();

		//SchedulerSubsystem 
		SchedulerSubsystem schedulerSubSys = new SchedulerSubsystem(floorRequestData, elevatorRequest);
		
		//Set FloorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to FloorSubsystem
		FloorSubsystem floorSubSys = new FloorSubsystem(schedulerSubSys, floorRequestData);
		schedulerSubSys.setFloorSubsystem(floorSubSys);

		//Set ElevatorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to ElevatorSubsystem
		ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(schedulerSubSys, 1, elevatorRequest);
		schedulerSubSys.setElevatorSubsystem(elevatorSubsystem);
		
		//Create Threads
		Thread schedulerThread= new Thread(schedulerSubSys, "Scheduler");
		Thread floorSubSysThread = new Thread(floorSubSys, "FloorSubSys");
		Thread elevatorSubsystemThread = new Thread(elevatorSubsystem, "ElevatorSubSys");
		
		schedulerThread.start();
		floorSubSysThread.start();
		elevatorSubsystemThread.start();
	}
}
