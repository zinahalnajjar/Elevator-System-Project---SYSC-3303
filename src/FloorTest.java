import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
/**
 * Test class For the floor method
 * @author MacKenzie, Faris
 *
 */
public class FloorTest {
	

	
	/**
	 * This method tests the getInfo(0 method to see if it properly reads the input file
	 */
	@Test
	public void testRead() {
		FloorRequestData floorReqData = new FloorRequestData();
		ElevatorRequest elevatorReq = new ElevatorRequest();
		SchedulerSubsystem sc = new SchedulerSubsystem(floorReqData, elevatorReq);
		FloorSubsystem floor = new FloorSubsystem(sc, floorReqData);
		LocalTime localTime = LocalTime.of(2, 11,16,573 * 1000000);
		FloorRequest testCase = new FloorRequest(localTime, 3, 6, true);
		ArrayList<FloorRequest> floorList = floor.getInfo();
		
		assertEquals(floorList.get(1).getOriginFloor(), testCase.getOriginFloor());
		assertEquals(floorList.get(1).getDestinationFloor(), testCase.getDestinationFloor());
		assertEquals(floorList.get(1).getLocalTime(), testCase.getLocalTime());
		assertFalse(floorList.get(1).isGoingUp());


		
	}
}
