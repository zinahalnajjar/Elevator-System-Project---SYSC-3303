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

//    private boolean floorRequestFlag; //true if floor has a data to give to scheduler, false otherwise
    private boolean floorReceiveFlag; //true if floor is receiving data from scheduler, false if floor is waiting
    private boolean elevatorRequestFlag; //true if elevator has a request to give to scheduler, false otherwise
    private boolean elevatorReceiveFlag; //true if elevator is receiving data from scheduler, false if elevator is waiting
    private ArrayList<FloorRequest> floorRequest; // variable for floor going to scheduler
    private ArrayList<FloorRequest> elevatorInstructions; // variable for scheduler going to elevator
    private ArrayList<FloorRequest> elevatorDecision; // variable for Elevator going into floor
    private ArrayList<FloorRequest> elevatorToFloor; // variable for the floor receiving elevator's decision
    
	private FloorSubsystem floorSubSys; //added
	private ElevatorSubsystem elevatorSubsystem;
//	private FloorRequest floorRequest;
	private FloorRequestData floorRequestData;
	private ResponseData elevatorResponse;
	private ElevatorRequest elevatorRequest;

    public SchedulerSubsystem(FloorRequestData floorRequestData, ElevatorRequest elevatorRequest) {
    	//Shared with FloorSubsystem
    	this.floorRequestData = floorRequestData;
    	
    	//Shared with ElevatorSubsystem
		this.elevatorRequest = elevatorRequest;
		
		elevatorResponse = new ResponseData();
//        floorRequestFlag = false;
        floorReceiveFlag = false;
        elevatorRequestFlag = false;
        elevatorReceiveFlag = false;
    }

    /**
     * this method allows the floor to put a request into the scheduler, this method will be called in Floor
     * @param floorRequests
     */
  //Changed TO take ONE FLOOR REQUEST at a time.
    //PENDING synchronized
    public void putRequestFromFloor(FloorRequest floorRequest){ //checked off for now
    	synchronized (floorRequestData) {
    		while(floorRequestData.getFloorRequest() != null) {
    			//wait until request data is cleared
    			try {
    				System.out.println("Awaiting FloorRequest to be CLEARED....");
    				floorRequestData.wait();
    			} catch (InterruptedException e) {
    				System.err.println(e);
    			}
    		}
    		floorRequestData.setFloorRequest(floorRequest);
//    		processFloorRequest();
    		floorRequestData.notifyAll();
		}//synchronized
    }
    
    /**
     * Process the floor request by sending the request to ElevatorSubSystem.
     * Once done, clear the request data.
     */
    private void processFloorRequest() {
    	System.out.println("Processing FloorRequest....");
//    	//request flow
//    	Floor 3 -> Sched
//    	17:32:33.183,3,UP,7
//
//    	synchronized (elevatorRequest) {
//    		while(elevatorRequest.getFloor() != null) {
//    			//wait until previous request is completed
//    			try {
//    				System.out.println("Elevator Awaiting....");
//    				elevatorRequest.wait();
//    			} catch (InterruptedException e) {
//    				System.err.println(e);
//    			}
//    		}//while
//    		elevatorRequest.setFloor(floorRequestData.getFloorRequest().getOriginFloor());
//    		elevatorRequest.clear();
//    		elevatorRequest.notifyAll();
//		}//synchronized
//    	
    	
    	elevatorSubsystem.moveTo(floorRequestData.getFloorRequest().getOriginFloor());
    	elevatorSubsystem.moveTo(floorRequestData.getFloorRequest().getDestinationFloor());
    	//pending lamp indication.
    	
    }
    


    /**
     * this method puts information into the elevator. This will be called in Scheduler
     * @param info
     */
    public synchronized void putInfoToElevator(ArrayList<FloorRequest> info){ //checked
        while (elevatorReceiveFlag) { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        if(!info.isEmpty()){
            elevatorInstructions = info;
            // change something elevatorReceiveFlag to true. it will then be called in the while loop
            elevatorReceiveFlag = true;
            notifyAll();
        }
    }

    /**
     * This method gets the information from the scheduler into elevator. This will be called in Elevator
     * @return
     */
    public synchronized ArrayList<FloorRequest> getInfoForElevator() { //checked
        while (!elevatorReceiveFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorRequest> newElevatorInfo = elevatorInstructions; // follows example from the Box class
        elevatorInstructions = null;
        // change something  elevatorReceiveFlag to false. x will then be called in the while loop
        elevatorReceiveFlag = false;
        notifyAll();
        return newElevatorInfo;
    }

    /**
     * This method puts the elevator's response. This will be called in Elevator
     * @param decision
     */
    public synchronized void putElevatorResponse(ArrayList<FloorRequest> decision){ // checked
        while (elevatorRequestFlag) { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        if(!decision.isEmpty()){
            elevatorDecision = decision;
            // change something (x) to true. x will then be called in the while loop
            elevatorRequestFlag = true;
            notifyAll();
        }
    }

    /**
     * This method gets the elevators response. This will be called in Scheduler
     * @return
     */
    public synchronized ArrayList<FloorRequest> getElevatorResponse() { //checked
        while (!elevatorRequestFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorRequest> newElevatorDecision = elevatorDecision; // follows example from the Box class
        elevatorDecision = null;
        // change something (x) to false. x will then be called in the while loop
        elevatorRequestFlag = false;
        notifyAll();
        return newElevatorDecision;
    }

    /**
     * This method puts the elevator's decision into the floor class. This will be called in scheduler
     * @param floor
     */
    public synchronized void putElevatorIntoFloor(ArrayList<FloorRequest> floor){ //check
        while (floorReceiveFlag) { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        if(!floor.isEmpty()){
            elevatorDecision = elevatorToFloor;
            // change something (x) to true. x will then be called in the while loop
            floorReceiveFlag = true;
            notifyAll();
        }
    }

    /**
     * This method gets the Elevator's decision. This will be called in floor
     * @return
     */
    public synchronized ArrayList<FloorRequest> getElevatorIntoFloor() { //check
        while (!floorReceiveFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorRequest> newElevatorToFloor = elevatorToFloor; // follows example from the Box class
        elevatorToFloor = null;
        // change something (x) to false. x will then be called in the while loop
        floorReceiveFlag = false;
        notifyAll();
        return newElevatorToFloor;
    }

    @Override
    public void run() {
        while(true) {
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
    //pending 
    private void old() {
        	//----
        	
        	ArrayList<FloorRequest> information = null;//getFloorRequest();
        	System.out.println("Request obtain from Floor by Scheduler!!");
        	putInfoToElevator(information);
        	System.out.println("Put information into Elevator by Scheduler!!");
        	ArrayList<FloorRequest> response = getElevatorResponse();
        	System.out.println("Response received from Elevator by Scheduler!!");
        	putElevatorIntoFloor(response);
        	System.out.println("Sheduler sent Elevator response to the Floor");
        	
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    /**
     * Set FloorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to FloorSubsystem
     * @param floorSubSys
     */
	public void setFloorSubsystem(FloorSubsystem floorSubSys) {
		this.floorSubSys = floorSubSys;
	}

	/**
	 * Set ElevatorSubsystem to SchedulerSubsystem so that SchedulerSubsystem can talk to ElevatorSubsystem
	 * @param elevatorSubsystem
	 */
	public void setElevatorSubsystem(ElevatorSubsystem elevatorSubsystem) {
		this.elevatorSubsystem = elevatorSubsystem;
		
	}

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

	private void processResponseData() {
		System.out.println("Scheduler Received ElevatorResponse: " + this.elevatorResponse.getResponse());
		System.out.println("Scheduler Sending ElevatorResponse to Floor.");
		//Pass the response to Floor.
		floorSubSys.setElevatorResponse(this.elevatorResponse);
		elevatorResponse.clear();
	}

	public void setFloorRequestData(FloorRequestData floorRequestData) {
		this.floorRequestData = floorRequestData;
	}
}