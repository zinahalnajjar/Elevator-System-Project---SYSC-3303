/*
 * class contains the elevator data which will be shared between the elevator and schdeuler 
 */
public class ElevatorData {
	
	private boolean lampOn = true; // when a floor button is pressed this becomes true
	private int floorButton; // this is for the floor buttons inside of the elevator
	private String elevatorButton; // this is for the up/down elevator buttons 
	
	/*
	 * constructor 
	 */
	public ElevatorData(int floorButton, String elevatorButton, boolean lampOn) {
		this.elevatorButton = elevatorButton;
		this.floorButton = floorButton;
		this.lampOn = lampOn; 
		
	}
	
	public String getElevatorButton() {
		return elevatorButton;
	}
	
	public void setElevatorButton(String elevatorButton) {
		this.elevatorButton = elevatorButton;
	}
	
	public int getFloorButton() {
		return floorButton;
	}
	
	public void setFloorButton(int floorButton) {
		this.floorButton = floorButton;
	}
	
	public boolean isLampOn() {
		if(lampOn == false) {
			lampOn =true;
		}
		return lampOn;
	}
	
	public void lampOn(boolean lampOn) {
		this.lampOn = lampOn;
	}

}
