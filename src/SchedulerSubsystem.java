/**
 * works as a channel to communicate data between the floors and the elevator
 * receives an input data from the floor subsystem
 * receives data from the elevator subsystem
 * sends data back to the elevator subsystem
 * sends data back to the floor subsystem
 * @Author MacKenzie Wallace
 * @version Feb 5, 2021
 */
public class SchedulerSubsystem implements Runnable{

	SharedData controller;
	
	public SchedulerSubsystem(SharedData controller) {
		this.controller = controller;
		
	}
	
	
	@Override
	public void run() {
		while(true) {
			// controller invokes a method from the SharedData that calls to schedule the elevator
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
