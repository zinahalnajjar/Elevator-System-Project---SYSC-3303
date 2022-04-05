
public interface ElevatorDashboard {
	
	void updateDestination(int elevatorID, int destination);
	void updateState(int elevatorID, String state);
	void updateState(int elevatorID, int position, String state);
	void updateError(int elevatorID, int error);
	//void updateCurrentFloor(Model model, int currentFloor);
	


}