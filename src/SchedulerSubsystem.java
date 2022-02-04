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
    private ArrayList<FloorData> floorRequest; // variable for floor going to scheduler
    private ArrayList<FloorData> elevatorInstructions; // variable for scheduler going to elevator
    private ArrayList<FloorData> elevatorDecision; // variable for Elevator going into floor
    private ArrayList<FloorData> elevatorToFloor; // variable for the floor receiving elevator's decision

    public SchedulerSubsystem() {
        hasInfo = false;

    }


    public void setGotInfo(boolean hasInfo) {
        this.hasInfo = hasInfo;
    }

    public boolean hasReceived() {
        return hasInfo;
    }

    /**
     * this method allows the floor to put a request into the scheduler, this method will be called in Floor
     * @param floorDatas
     */
    public void putRequestFromFloor(ArrayList<FloorData> floorDatas){
        while(hasReceived()){ // while there has been a request received (true), wait
            try {
                wait();
            }catch(InterruptedException e) {
                System.err.println(e);
            }
        }
        if(!floorDatas.isEmpty()){
            floorRequest = floorDatas;
            setGotInfo(true);
            notifyAll();
        }
    }

    /**
     * this method gets the request from the floor. This will be called in Scheduler
     */
    public ArrayList<FloorData> getFloorRequest() {
        while (!hasReceived()) { // while there are no requests received (false), wait
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newFloorRequests = floorRequest; // follows example from the Box class
        floorRequest = null;
        setGotInfo(false);
        notifyAll();
        return newFloorRequests;
    }

    /**
     * this method puts information into the elevator. This will be called in Scheduler
     * @param info
     */
    public void putInformation(ArrayList<FloorData> info){
        while () { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        if(!info.isEmpty()){
            elevatorInstructions = info;
            // change something (x) to true. x will then be called in the while loop
            notifyAll();
        }
    }

    /**
     * This method gets the information from the scheduler into elevator. This will be called in Elevator
     * @return
     */
    public ArrayList<FloorData> getInfoForElevator() {
        while () { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorInfo = elevatorInstructions; // follows example from the Box class
        elevatorInstructions = null;
        // change something (x) to false. x will then be called in the while loop
        notifyAll();
        return newElevatorInfo;
    }

    /**
     * This method puts the elevator's response. This will be called in Elevator
     * @param decision
     */
    public void putElevatorDecision(ArrayList<FloorData> decision){
        while () { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        if(!decision.isEmpty()){
            elevatorDecision = decision;
            // change something (x) to true. x will then be called in the while loop
            notifyAll();
        }
    }

    /**
     * This method gets the elevators response. This will be called in Scheduler
     * @return
     */
    public ArrayList<FloorData> getElevatorDecision() {
        while () { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorDecision = elevatorDecision; // follows example from the Box class
        elevatorDecision = null;
        // change something (x) to false. x will then be called in the while loop
        notifyAll();
        return newElevatorDecision;
    }

    /**
     * This method puts the elevator's decision into the floor class. This will be called in scheduler
     * @param floor
     */
    public void putElevatorIntoFloor(ArrayList<FloorData> floor){
        while () { // a way of determining a variable is false or not
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

        if(!floor.isEmpty()){
            elevatorDecision = elevatorToFloor;
            // change something (x) to true. x will then be called in the while loop
            notifyAll();
        }
    }

    /**
     * This method geets the Elevator's decision. This will be called in floor
     * @return
     */
    public ArrayList<FloorData> getElevatorIntoFloor() {
        while () { // a way of determining a variable is false or not, must start with ! before
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
        ArrayList<FloorData> newElevatorToFloor = elevatorToFloor; // follows example from the Box class
        elevatorToFloor = null;
        // change something (x) to false. x will then be called in the while loop
        notifyAll();
        return newElevatorToFloor;
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
