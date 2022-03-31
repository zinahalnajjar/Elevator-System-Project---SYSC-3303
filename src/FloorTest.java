import static org.junit.jupiter.api.Assertions.*;

import java.net.UnknownHostException;
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
	 * @throws UnknownHostException 
	 */
	@Test
	public void testRead() throws UnknownHostException {
		//FloorRequestData floorReqData = new FloorRequestData();
		//FloorRequest elevatorReq = new FloorRequest();
		//SchedulerSubsystem sc = new SchedulerSubsystem(floorReqData, elevatorReq);
		MainFloorSys floor = new MainFloorSys();
		LocalTime localTime = LocalTime.of(2, 11,16,573 * 1000000);
		FloorMovementData testCase = new FloorMovementData(localTime, 3, 6, true, 2);
		ArrayList<FloorMovementData> floorList = floor.getInfo();
		
		assertEquals(floorList.get(1).getOriginFloor(), testCase.getOriginFloor());
		assertEquals(floorList.get(1).getDestinationFloor(), testCase.getDestinationFloor());
		assertEquals(floorList.get(1).getLocalTime(), testCase.getLocalTime());
		assertFalse(floorList.get(1).isGoingUp());
		assertEquals(floorList.get(1).getError(), testCase.getError());

	}
}
