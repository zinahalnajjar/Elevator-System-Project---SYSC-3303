public class ArrivalSensor {

    private int elevatorId;

    private int reachedFloor;

    /**
     * 
     * @param elevatorId
     */
    public ArrivalSensor(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public int getReachedFloor() {
        return reachedFloor;
    }

    public void setReachedFloor(int reachedFloor) {
        this.reachedFloor = reachedFloor;
        Output.print("Elevator", "ArrivalSensor", Output.INFO,
                "Elevator " + elevatorId + " APPROACHED " + reachedFloor);
    }

    public int getElevatorId() {
        return elevatorId;
    }

}
