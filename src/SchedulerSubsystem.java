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
    private InformationHandler floorInfo;
    private InformationHandler elevatorInfo;


    public SchedulerSubsystem() {
        this.floorInfo = null;
        this.elevatorInfo = null;
        hasInfo = false;

    }

    public InformationHandler getElevatorinfo(){
        return elevatorInfo;

    }

    public InformationHandler getFloorinfo(){
        return floorInfo;

    }


    public void setGotInfo(boolean hasInfo) {
        this.hasInfo = hasInfo;
    }

    public boolean hasReceived() {
        return hasInfo;
    }
    
    
    /**
     * This method sends instructions to the floor
     * @param floorRequests
     */
    public synchronized void sendFloorInstructions(InformationHandler floorRequests){
        while(!hasReceived()) {
             try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }

        }

        //update the data
        floorInfo = floorRequests;

    }

    /**
     * This method sends instructions to the elevator
     * @param ElevatorRequests
     */
    public synchronized void sendElevatorInstructions(InformationHandler ElevatorRequests){
        while(!hasReceived()) {
             try {
                    wait();
                } catch (InterruptedException e) {
                    return;
                }

        }

        //update the data
        elevatorInfo = ElevatorRequests;

    }
    
    /**
     * get requests made by the elevator
     * @return
     */
    public synchronized InformationHandler getElevatorResponse() {
    	
    }
    
    
    
	@Override
	public void run() {
		while(true) {
			//getRequest();
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
