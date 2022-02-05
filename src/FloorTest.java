import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class FloorTest {
// testing 
	

	
	@Test
	public void testRead() {
		FloorRequestData floorReqData = new FloorRequestData();
		ElevatorRequest elevatorReq = new ElevatorRequest();
		SchedulerSubsystem sc = new SchedulerSubsystem(floorReqData, elevatorReq);
		FloorSubsystem floor = new FloorSubsystem(sc, floorReqData);
		LocalTime localTime = LocalTime.of(6, 5);
		FloorRequest testCase = new FloorRequest(localTime, 2, 4, true);
		ArrayList<FloorRequest> floorList = floor.getInfo();

		for(int i = 0; i<floorList.size(); i++) {
			assertEquals(floorList.get(i), testCase);
		}
	
		
	}
}
