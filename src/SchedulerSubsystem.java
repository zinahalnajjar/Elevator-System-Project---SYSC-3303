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

    private boolean floorRequestFlag; //true if floor has a data to give to scheduler, false otherwise
    private boolean floorReceiveFlag; //true if floor is receiving data from scheduler, false if floor is waiting
    private boolean elevatorRequestFlag; //true if elevator has a request to give to scheduler, false otherwise
    private boolean elevatorReceiveFlag; //true if elevator is receiving data from scheduler, false if elevator is waiting
    private ArrayList<FloorData> floorRequest; // variable for floor going to scheduler
    private ArrayList<FloorData> elevatorInstructions; // variable for scheduler going to elevator
    private ArrayList<FloorData> elevatorDecision; // variable for Elevator going into floor
    private ArrayList<FloorData> elevatorToFloor; // variable for the floor receiving elevator's decision

    public SchedulerSubsystem() {
        floorRequestFlag = false;
        floorReceiveFlag = false;
        elevatorRequestFlag = false;
        elevatorReceiveFlag = false;
    }

    /**
     * this method allows the floor to put a request into the scheduler, this method will be called in Floor
     * @param floorDatas
     */
    public synchronized void putRequestFromFloor(ArrayList<FloorData> floorDatas){ //checked off for now
        while(floorRequestFlag){ // while there has been a request received (true), wait
            try {
                wait();
            }catch(InterruptedException e) {
                System.err.println(e);
            }
        }
        if(!floorDatas.isEmpty()){
            floorRequest = floorDatas;
            floorRequestFlag = true;
            notifyAll();
        }
    }

    /**
     * this method gets the request from the floor. This will be called in Scheduler ?? called inside elevator instead?
     */
    public synchronized ArrayList<FloorData> getFloorRequest() { //checked
        while (!floorRequestFlag) { // while there are no requests received (false), wait
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newFloorRequests = floorRequest; // follows example from the Box class
        floorRequest = null;
        floorRequestFlag = false;
        notifyAll();
        return newFloorRequests;
    }

    /**
     * this method puts information into the elevator. This will be called in Scheduler
     * @param info
     */
    public synchronized void putInfoToElevator(ArrayList<FloorData> info){ //checked
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
    public synchronized ArrayList<FloorData> getInfoForElevator() { //checked
        while (!elevatorReceiveFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorInfo = elevatorInstructions; // follows example from the Box class
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
    public synchronized void putElevatorResponse(ArrayList<FloorData> decision){ // checked
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
    public synchronized ArrayList<FloorData> getElevatorResponse() { //checked
        while (!elevatorRequestFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorDecision = elevatorDecision; // follows example from the Box class
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
    public synchronized void putElevatorIntoFloor(ArrayList<FloorData> floor){ //check
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
    public synchronized ArrayList<FloorData> getElevatorIntoFloor() { //check
        while (!floorReceiveFlag) { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorToFloor = elevatorToFloor; // follows example from the Box class
        elevatorToFloor = null;
        // change something (x) to false. x will then be called in the while loop
        floorReceiveFlag = false;
        notifyAll();
        return newElevatorToFloor;
    }

    @Override
    public void run() {
        while(true) {
        	ArrayList<FloorData> information = getFloorRequest();
        	System.out.println("Request obtain from Floor by Scheduler!!");
        	putInfoToElevator(information);
        	System.out.println("Put information into Elevator by Scheduler!!");
        	ArrayList<FloorData> response = getElevatorResponse();
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
    }
}
