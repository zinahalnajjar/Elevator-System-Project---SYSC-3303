/**
 * Wrapper class to contain request data.
 * 
 * @author zeena
 *
 */
public class FloorRequestData {

	private FloorRequest floorRequest;

	public FloorRequestData() {

	}

	public synchronized void setFloorRequest(FloorRequest floorRequest) {
		this.floorRequest = floorRequest;
	}

	public synchronized FloorRequest getFloorRequest() {
		return floorRequest;
	}

	// clear floorRequest
	public synchronized void clearFloorRequest() {
		this.floorRequest = null;
	}
}
