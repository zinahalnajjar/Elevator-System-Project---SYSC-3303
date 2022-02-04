import java.util.ArrayList;

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

	private boolean hasInfo;
	
	private ArrayList<InformationHandler> requests;
	
	public SchedulerSubsystem() {
		
	}
	
	public void setGotInfo(boolean hasInfo) {
		this.hasInfo = hasInfo;
	}
	
	public boolean hasReceived() {
		return hasInfo;
	}
	
	
	public synchronized ArrayList<InformationHandler> getRequest(){
		while(!hasReceived()) {
			 try {
	                wait();
	            } catch (InterruptedException e) {
	                return null;
	            }
			
		}
		
		ArrayList<InformationHandler> newRequests = requests;
		requests = null;
		notifyAll();
		return newRequests;
	
	}
	
	
	@Override
	public void run() {
		while(true) {
			getRequest();
			System.out.println("Request received!");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
