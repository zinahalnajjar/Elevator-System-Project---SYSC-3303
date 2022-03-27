/*
 * responsible for detecting the movement of each elevator
 * it prints each floor the elevator passes by 
 * @Author Zinah 
 */
public class ArrivalSensor {

    private int elevatorId;

    private int reachedFloor;

    /**
     * constructor
     * @param elevatorId
     */
    public ArrivalSensor(int elevatorId) {
        this.elevatorId = elevatorId;
    }
    
    /*
     * @return reachedFloor
     */

    public int getReachedFloor() {
        return reachedFloor;
    }
    /*
     * setter for the floor that the elevtaor is currenlty at 
     */

    public void setReachedFloor(int reachedFloor) {
        this.reachedFloor = reachedFloor;
        Output.print("Elevator", "ArrivalSensor", Output.INFO,
                "Elevator " + elevatorId + " APPROACHED " + reachedFloor);
    }

    /*
     * @retrun elevatorId
     */
    public int getElevatorId() {
        return elevatorId;
    }

}
