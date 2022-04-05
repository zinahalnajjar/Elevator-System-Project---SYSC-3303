
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class MainElevatorSys {
	private static final int SERVER_PORT = 69;

	private DatagramPacket receivedPacket;
	private DatagramSocket serverSocket;


	// Number of elevators
	private static final int ELEVATOR_COUNT = 4;

	// GUI
	static ElevatorDashboardGUI gui;

	private int currentFloor;
	private boolean motorOperating;
	public State currentState;
	private boolean stopRequested = false;

	private static InetAddress hostIP;

	private static int hostPort;

	private static List<Elevator> elevatorList = new ArrayList<Elevator>();

	private static MainElevatorSys server;

	enum State {
		STILL, MOVING
	}

	/**
	 * constructor
	 */
	public MainElevatorSys() throws SocketException {
		// Construct a datagram socket and bind it to port SERVER_PORT
		// on the local host machine. This socket will be used to
		// receive UDP Datagram packets.
		serverSocket = new DatagramSocket(SERVER_PORT);

		// to test socket timeout (2 seconds)
		// receiveSocket.setSoTimeout(2000);
	}

	/**
	 * 
	 * method to receive requests from the host (scheduler)
	 * 
	 * @return
	 */
	public void start() {
		startElevatorThreads();
		// System.out.println("-------Thread Started");
		Output.print("Elevator", "Main", Output.INFO, "-------Thread Started");

		try {
			byte[] inBytes;
			while (true) {
				// System.out.println("Awaiting Schedular data...");
				Output.print("Elevator", "Main", Output.INFO, "Awaiting Schedular data...");

				inBytes = new byte[1024];
				receivedPacket = new DatagramPacket(inBytes, inBytes.length);

				try {
					serverSocket.receive(receivedPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}

				processReceivedBytes(receivedPacket);
				delay();
				System.out.println("============================");

			} // while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// System.out.println("Close serverSocket...");
			Output.print("Elevator", "Main", Output.INFO, "Close serverSocket...");

			serverSocket.close();
		}

	}

	/**
	 * method that starts the three elevator threads (cars)
	 * 
	 * @return
	 */

	private void startElevatorThreads() {

		FloorRequest floorRequest1 = new FloorRequest();
		Elevator elevator1 = new Elevator(1, floorRequest1);
		elevatorList.add(elevator1);

		Thread elevatorThread1 = new Thread(elevator1, "Elevator 1");
		elevatorThread1.start();
		// System.out.println("--- elevatorThread1 STARTED.");
		Output.print("Elevator", "Main", Output.INFO, "--- elevatorThread1 STARTED.");

		FloorRequest floorRequest2 = new FloorRequest();
		Elevator elevator2 = new Elevator(2, floorRequest2);
		elevatorList.add(elevator2);

		Thread elevatorThread2 = new Thread(elevator2, "Elevator 2");
		elevatorThread2.start();
		// System.out.println("--- elevatorThread2 STARTED.");
		Output.print("Elevator", "Main", Output.INFO, "--- elevatorThread2 STARTED.");

		FloorRequest floorRequest3 = new FloorRequest();
		Elevator elevator3 = new Elevator(3, floorRequest3);
		elevatorList.add(elevator3);

		Thread elevatorThread3 = new Thread(elevator3, "Elevator 3");
		elevatorThread3.start();
		// System.out.println("--- elevatorThread3 STARTED.");
		Output.print("Elevator", "Main", Output.INFO, "--- elevatorThread3 STARTED.");

	}

	/**
	 * 
	 * method to process the received requests from the hosts
	 * 
	 * @param receivedPacket
	 * @return
	 * 
	 */

	private void processReceivedBytes(DatagramPacket receivedPacket) throws Exception {
		// Host Details
		hostIP = receivedPacket.getAddress();
		hostPort = receivedPacket.getPort();

		// Get data from the received packet.
		byte[] receivedBytes = receivedPacket.getData();
		// System.out.println("Received Length: " + receivedBytes.length);
		Output.print("Elevator", "Main", Output.INFO, "Received Length: " + receivedBytes.length);

		String request = new String(receivedBytes);
		// System.out.println("Received REQUEST: " + request);
		Output.print("Elevator", "Main", Output.INFO, "Received REQUEST: " + request);

		if (request.startsWith("floor request elevator ")) {
			processFloorRequest(request, hostIP, hostPort);
		} else {
			// Throw Exception
//			PENDING SEND RESPONSE
//			throw new Exception("Invalid request.");
			// System.out.println("Invalid request.");
			Output.print("Elevator", "Main", Output.INFO, "Invalid request.");

		}

	}

	/**
	 * processing the requests
	 * 
	 * @param request hostIP hostPort
	 * @return
	 * 
	 */

	private void processFloorRequest(String request, InetAddress hostIP, int hostPort) throws Exception {

		// Format needed:
		// floor request elevator <ELEVATOR ID> <FLOOR NUMBER> <ERROR> END
		String[] tokens = request.split(" ");
		int originFloor = Integer.parseInt(tokens[3]);
		int destFloor = Integer.parseInt(tokens[4]);
		int error = Integer.parseInt(tokens[5]); // putting this as 5 because im just following what origin and dest
													// floor are doing

		dispatchFloorRequest(originFloor, destFloor, error);

		byte[] replyBytes = "DONE".getBytes();
		send(replyBytes, hostIP, hostPort);
	}

	private void applyErrorToElevator(Elevator elevator, int error) {
		String errorInfo = null;
		if (error == 0) {
			// NO ISSUES.
			return;
		} else if (error == 1) {
			// current elevator is 'DELAYED'
			Output.print("Elevator", "currentState", Output.INFO, "Elevator " + elevator.getElevatorID() + " DELAYED");
			elevator.setDelayed(true);
			errorInfo = "DELAYED";
		} else if (error == 2) {
			// current elevator is 'STUCK'
			Output.print("Elevator", "currentState", Output.INFO, "Elevator " + elevator.getElevatorID() + " STUCK");
			Output.print("Elevator", "currentState", Output.INFO,
					"Elevator " + elevator.getElevatorID() + " is OUT OF SERVICE.");
			elevator.setOutOfService(true);
			errorInfo = "OUT OF SERVICE";
		}
		
		// Format needed:
		// elevator <ELEVATOR ID> <FLOOR NUMBER> <ERROR> <STATE> END
		
		
//		String status = "elevator " + elevator.getElevatorID()
//				+ "0"
//				+ " 9 " //NON ZERO is error
//				+ errorInfo
//				+ " END";
		
		MainElevatorSys.gui.updateView(elevator.getElevatorID(), 0, error, errorInfo);

	}

	/**
	 * Dispatch floor request to an close by elevator.
	 * 
	 * @param originFloor
	 * @param destFloor
	 * @return
	 */
	private void dispatchFloorRequest(int originFloor, int destFloor, int error) {
		// BEFORE HANDLING request
		// make sure the 'delayed' elevators are BROUGHT BACK TO SERVICE BEFORE
		// handling other requests.
		fixDelayedElevators();

		// choose a working elevator to handle the request
		Elevator elevator;
		while (true) {
			// get close by working elevator
			elevator = getCloseByElevator(originFloor);
			Output.print("Elevator", "Main", Output.INFO, "CLOSE BY Elevator: " + elevator.getElevatorID());

			// handle error condition
			applyErrorToElevator(elevator, error);

			// check if elevator is 'available for use' after error scenario.
			if (elevator.isDelayed() || elevator.isOutOfService()) {
				// set error to 0 so that the next elevator would work as usual
				error = 0;
				// get NEXT AVAILABLE elevator
				continue;
			} else {
				// elevator OK.
				// process the request, with this 'working elevator'
				break;
			}
		}

		// get 'SHARED' FloorRequest for the elevator.
		FloorRequest floorRequest = elevator.getElevatorRequest();
		synchronized (floorRequest) {
			// CHECK IF ANY REQUEST in queue
			if (floorRequest.hasRequest()) {
				try {
					// There is a request in queue to be completed
					floorRequest.wait();
				} catch (InterruptedException e) {
				}
			} else {
				// Elevator is free.
				floorRequest.setOriginFloor(originFloor);
				floorRequest.setDestFloor(destFloor);
				floorRequest.setError(error);
				floorRequest.notifyAll();
			}
		} // synchronized

	}

	private void fixDelayedElevators() {
		for (Elevator elevator : elevatorList) {
			// check if elevator is DELAYED and FIX IT.
			// OUT OF SERVICE is not to be fixed.
			if (elevator.isDelayed()) {
				elevator.setDelayed(false);
				Output.print("Elevator", "Main", Output.INFO, "Elevator: " + elevator.getElevatorID() + " IS FIXED.");
				MainElevatorSys.gui.updateView(elevator.getElevatorID(), elevator.getCurrentFloor(), 0, "NORMAL");
			}
		}
	}

	/**
	 * Return the close by elevator based on the distance to the targetFloor from
	 * each elevator's current floor.
	 * 
	 * @param targetFloor
	 * @return
	 */
	private Elevator getCloseByElevator(int targetFloor) {
		// Find distance from targetFloor for each elevator's current floor
		//
		int min = -1;
		Elevator closeByElevator = null;
		for (Elevator elevator : elevatorList) {
			// check if elevator is OUT OF SERVICE 'or' DELAYED
			if (elevator.isOutOfService() || elevator.isDelayed()) {
				// skip this elevator
				continue;
			}
			// check if elevator is on the move
			if (elevator.isMotorOperating()) {
				// elevator is on the move
				// skip this elevator
				continue;
			}
			// elevator is not moving. check the distance
			int dist = Math.abs(elevator.getCurrentFloor() - targetFloor);
			if (min == -1) {
				// first check. Just pick the elevator.
				min = dist;
				closeByElevator = elevator;
			} else if (dist < min) {
				// this elevator is close by than the previous
				min = dist;
				closeByElevator = elevator;
			}
		}
		return closeByElevator;
	}

	/**
	 * 
	 * send reply to host
	 * 
	 * @param bytes, hostIP, hostPort
	 */
	private void send(byte[] bytes, InetAddress hostIP, int hostPort) throws IOException {
//		//Create Send Packet 
		DatagramPacket packetSend = new DatagramPacket(bytes, bytes.length, hostIP, hostPort);
		//
//		//Send Packet
		serverSocket.send(packetSend);
		// System.out.println("Sent reply: " + new String(bytes));
		Output.print("Elevator", "Main", Output.INFO, "Sent reply: " + new String(bytes));

		delay();

	}

	/**
	 * delay method
	 */

	private void delay() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * moves elevator to desired floor
	 * 
	 * @param destinationFloor, floor to move elevator to
	 * @return
	 */
	/*
	 * public boolean moveTo(int destinationFloor) {
	 * 
	 * // Check if motor is running // while (motorOperating) { // // wait until
	 * motor stops // try { // wait(); // } catch (InterruptedException e) { //
	 * System.err.println(e); // } // }
	 * 
	 * // Checks where the Elevator is // If Elevator at destination if
	 * (currentFloor == destinationFloor) {
	 * //System.out.println("Elevator already at: " + destinationFloor);
	 * Output.print("Elevator", "Main", Output.INFO,"Elevator already at: " +
	 * destinationFloor);
	 * 
	 * 
	 * } else { /* if motor is running, Doors must be closed currentState =
	 * State.MOVING; // ensure the state Doors Closed is active if (currentState ==
	 * State.MOVING) {
	 * //System.out.println("Doors are now closed. Elevator MOVING. ");
	 * Output.print("Elevator", "Main",
	 * Output.INFO,"Doors are now closed. Elevator MOVING. ");
	 * 
	 * } //System.out.println("Elevator moving from: " + currentFloor + " to: " +
	 * destinationFloor); Output.print("Elevator", "Main",
	 * Output.INFO,"Elevator moving from: " + currentFloor + " to: " +
	 * destinationFloor);
	 * 
	 * motorsOn(); // Move number of floors moveElevator(Math.abs(currentFloor -
	 * destinationFloor)); motorOff(); // For each move currentFloor gets changed
	 * here currentFloor = destinationFloor;
	 * //System.out.println("Elevator reached: " + destinationFloor);
	 * Output.print("Elevator", "Main", Output.INFO,"Elevator reached: " +
	 * destinationFloor);
	 * 
	 * } currentState = State.STILL; // Opens door // openDoor(destinationFloor);
	 * //System.out.println("Elevator reached: " + destinationFloor);
	 * Output.print("Elevator", "Main", Output.INFO,"Elevator reached: " +
	 * destinationFloor);
	 * 
	 * //System.out.println("Elevator Door opened at: " + destinationFloor);
	 * Output.print("Elevator", "Main", Output.INFO,"Elevator Door opened at: " +
	 * destinationFloor);
	 * 
	 * return true; }
	 */

	/**
	 * Turns on the motor
	 */
	public synchronized void motorsOn() {
		motorOperating = true;
	}

	/**
	 * Turns motors off
	 */
	public synchronized void motorOff() {
		motorOperating = false;
		notifyAll();
	}

	public synchronized void requestStop() {
		stopRequested = true;
	}

	private synchronized boolean stopRequested() {
		return stopRequested;
	}

	/*
	 * 
	 * /** Simulates elevator moving floors
	 * 
	 * @param numberOfFloors, floors that elevator will move
	 * 
	 * private void moveElevator(int numberOfFloors) { // delay try { for (int i =
	 * 0; i < numberOfFloors; i++) { Thread.sleep(500); } } catch
	 * (InterruptedException e) { } }
	 */
	/**
	 * main method
	 */

	public static void main(String[] args) {
		try {
//			// display GUI
			gui = new ElevatorDashboardGUI(ELEVATOR_COUNT);

			// Start elevator sub system
			server = new MainElevatorSys();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("\n\n ERROR: " + e.getMessage());
			Output.print("Elevator", "Main", Output.FATAL, e.getMessage());
			System.exit(1);
		}
	}

	public static void updateStatus(String status) {
		byte[] replyBytes = status.getBytes();
		try {
			server.send(replyBytes, hostIP, hostPort);
		} catch (IOException e) {
			Output.print("Elevator", "Main", Output.CRITICAL, e.getMessage());
		}
	}
}
