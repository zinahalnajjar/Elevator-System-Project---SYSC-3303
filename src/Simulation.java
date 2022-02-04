public class Simulation {

	/**
	 * Main thread
	 */
	public static void main(String[] args) {
		SchedulerSubsystem scheduler = new SchedulerSubsystem();
		Thread floor = new Thread(new FloorSubsystem(scheduler), "Floor");
		Thread sched= new Thread(new SchedulerSubsystem(), "Scheduler");
		Thread elevator = new Thread(new ElevatorSubsystem(scheduler, 1), "Floor");
		
		elevator.start();
		sched.start();
		floor.start();
	}
}
