public class FloorButton {
	
	private int destinationfloor;
	private boolean isUp;
	private FloorRequest floorRequest;
	
	
	
	public FloorButton( FloorRequest floorRequest) {
		this.isUp = false ;
		this.floorRequest =  floorRequest;
		this.destinationfloor = floorRequest.getDestinationFloor();
	}
	
	
	public void press() {
		System.out.println("FLOOR " + destinationfloor + ": " + isUp + "button has pressed.");
		//this.scheduler.acceptEvent(event);
		
	}
	

	public boolean isUp() {
		return this.isUp = true;
	}
	
	
	
}
