import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketException;

import org.junit.jupiter.api.Test;


/**
 * Test Class for Elevator
 * @author MacK Wallace
 *
 */
public class ElevatorTest {

	/**
	 * tests that motor can turn off and on and therefore indicates that the elevator can move
	 */
	@Test
	public void testIsMoving(){
		FloorRequest floorReq = new FloorRequest();
		Elevator elevator = new Elevator(1,floorReq);
		assertFalse(elevator.isMotorOperating()); // check if motor is off
		elevator.motorsOn();
		assertTrue(elevator.isMotorOperating()); // check if motor is on
		
	}
	
	/**
	 * tests the initial current floor the elevator is on
	 */
	@Test
	public void testCurrentFloor() {
		FloorRequest floorReq = new FloorRequest();
		Elevator elevator = new Elevator(2,floorReq);
		assertEquals(0,elevator.getCurrentFloor());
	}
	
	/**
	 * test the initial state of the elevator
	 */
	@Test
	public void testStillState() {
		FloorRequest floorReq = new FloorRequest();
		Elevator elevator = new Elevator(2,floorReq);
		assertEquals("STILL", elevator.getCurrentState());
	}
	
	@Test
	public void testLoading() {
		
	}
		
}
