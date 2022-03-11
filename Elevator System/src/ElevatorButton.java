/*
 * this class has the elevator button(up/down) when a user presses this button
 * the elevator will move either up or down and when it reaches the floor where the user is currently at it will turn on the lamp 
 */
public class ElevatorButton {

	private ElevatorSubsystem elevatorsubsystem;
	private ElevatorLamp elevatorLamp;
	// private int currentFloor;

	public ElevatorButton(ElevatorLamp elevatorLamp, ElevatorSubsystem elevatorsubsystem) {
		this.elevatorLamp = elevatorLamp;
		this.elevatorsubsystem = elevatorsubsystem;
		// this.currentFloor = 0;

	}

	public ElevatorLamp getElevatorLamp() {
		return this.elevatorLamp;
	}

	public void pressButton() {
		this.elevatorLamp.turnOn();
		//System.out.println("Elevator is currently at " + elevatorsubsystem.getcurrentFloor());

	}

}
