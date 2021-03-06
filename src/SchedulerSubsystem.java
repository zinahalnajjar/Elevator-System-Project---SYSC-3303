import java.util.ArrayList;

/**
 * works as a channel to communicate data between the floors and the elevator
 * receives an input data from the floor subsystem
 * receives data from the elevator subsystem
 * sends data back to the elevator subsystem
 * sends data back to the floor subsystem
 * 
 * ------old code----
 * @Author Zinah, Mack, Vilmos
 *
 */
public class SchedulerSubsystem implements Runnable{
    
	private FloorSubsystem floorSubSys; 
	private ElevatorSubsystem elevatorSubsystem;
	private FloorRequestData floorRequestData;
	private ResponseData elevatorResponse;
	private FloorRequest elevatorRequest;
	
	private State currentState;

	/**
	 * note:
	 * EVENT (FLOOR_REQUEST_RECEIVED, ELEVATOR_RESPONSE_RECEIVED) 
	 * causes ACTION (RESPONSE_SENT) results in STATE CHANGE (ELEVATOR MOVING)
	 *
	 */
	enum State{
		WAITING_FLOOR_REQUEST
	}
	
	/**
	 * base constructor
	 * @param floorRequestData, object of FloorRequestData contains request data
	 * @param elevatorRequest, the elevators response
	 */
    public SchedulerSubsystem(FloorRequestData floorRequestData, FloorRequest elevatorRequest) {
    	//Shared with FloorSubsystem
    	this.floorRequestData = floorRequestData;
    	
    	//Shared with ElevatorSubsystem
		this.elevatorRequest = elevatorRequest;
		
		elevatorResponse = new ResponseData();
        
        
        currentState = State.WAITING_FLOOR_REQUEST;
    }
    
    /**
     * process the floor request by sending the request to ElevatorSubSystem.
     * Once done, clear the request data.
     */
    private void processFloorRequest() {
    	System.out.println("Processing FloorRequest....");
    	elevatorSubsystem.moveTo(floorRequestData.getFloorRequest().getOriginFloor());
    	elevatorSubsystem.moveTo(floorRequestData.getFloorRequest().getDestinationFloor());
    	//pending lamp indication.
    }

    /**
     * Set FloorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to FloorSubsystem
     * @param floorSubSys, the floor subsystem
     */
	public void setFloorSubsystem(FloorSubsystem floorSubSys) {
		this.floorSubSys = floorSubSys;
	}

	/**
	 * Set ElevatorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to ElevatorSubsystem
	 * @param elevatorSubsystem, elevator subsystem
	 */
	public void setElevatorSubsystem(ElevatorSubsystem elevatorSubsystem) {
		this.elevatorSubsystem = elevatorSubsystem;
		
	}

	/**
	 * setter for the elevator response
	 * @param response, response from elevator
	 */
	public void setElevatorResponse(String response) {
	
    	synchronized (elevatorResponse) {
    		while(elevatorResponse.getResponse() != null) {
    			//wait until request data is cleared
    			try {
    				System.out.println("Awaiting ElevatorResponse to be CLEARED....");
    				elevatorResponse.wait();
    			} catch (InterruptedException e) {
    				System.err.println(e);
    			}
    		}
    		elevatorResponse.setResponse(response);
    		processResponseData();
    		elevatorResponse.notifyAll();
		}//synchronized
		
	}

	/**
	 * passes the response of elevator to floor
	 */
	private void processResponseData() {
		System.out.println("Scheduler Received ElevatorResponse: " + this.elevatorResponse.getResponse());
		System.out.println("Scheduler Sending ElevatorResponse to Floor.");
		//Pass the response to Floor.
		floorSubSys.setElevatorResponse(this.elevatorResponse);
		elevatorResponse.clear();
	}
	
	/**
	 * sets the floor request  
	 * @param floorRequestData, new request
	 */
	public void setFloorRequestData(FloorRequestData floorRequestData) {
		this.floorRequestData = floorRequestData;
	}
	
    @Override
    public void run() {
        while(currentState == State.WAITING_FLOOR_REQUEST) {
        	synchronized (floorRequestData) {
        		while(floorRequestData.getFloorRequest() == null) {
        			//wait until request data arrrives
        			try {
        				System.out.println("Awaiting FloorRequest....");
        				floorRequestData.wait();
        			} catch (InterruptedException e) {
        				System.err.println(e);
        			}
        		}//while
        		processFloorRequest();
            	//clear floorRequest
            	floorRequestData.clearFloorRequest();
        		floorRequestData.notifyAll();
			}//synchronized
        	
        }//while (true)
    }
}