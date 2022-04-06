import static org.junit.jupiter.api.Assertions.*;



import org.junit.jupiter.api.Test;

/**
 * Test Class for Elevator
 * @author MacK Wallace
 *
 */
public class ElevatorTest {

	/**
	 * tests that motor can turn on and therefore indicates that the elevator can move
	 */
	@Test
	public void testIsMoving(){
		FloorRequest floorReq = new FloorRequest();
		Elevator elevator = new Elevator(1,floorReq);
		Thread elThread = new Thread(elevator);
		elThread.start();
		assertFalse(elevator.isMotorOperating());
		elevator.motorsOn();
		assertTrue(elevator.isMotorOperating());
		
	}
		
}
