/*
 * elevator controller system 
 * it controls the movement of the elevator threads and coordinate between them when depending on the request coming from scheduler 
 * 
 * @author Zinah, Mack 
 */

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

	private int currentFloor;
	private boolean motorOperating;
	public State currentState;

	private List<Elevator> elevatorList = new ArrayList<Elevator>();

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
	 * @return
	 */
	public void start() {
		startElevatorThreads();
		//System.out.println("-------Thread Started");
		Output.print("Elevator", "Main", Output.INFO, "-------Thread Started");

		try {
			byte[] inBytes;
			while (true) {
				//System.out.println("Awaiting Schedular data...");
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
			//System.out.println("Close serverSocket...");
			Output.print("Elevator", "Main", Output.INFO,"Close serverSocket...");

			serverSocket.close();
		}

	}
	/**
	 * method that starts the three elevator threads (cars)
	 * @return
	 */

	private void startElevatorThreads() {

		FloorRequest floorRequest1 = new FloorRequest();
		Elevator elevator1 = new Elevator(1, floorRequest1);
		elevatorList.add(elevator1);

		Thread elevatorThread1 = new Thread(elevator1, "Elevator 1");
		elevatorThread1.start();
		//System.out.println("--- elevatorThread1 STARTED.");
		Output.print("Elevator", "Main", Output.INFO,"--- elevatorThread1 STARTED.");


		FloorRequest floorRequest2 = new FloorRequest();
		Elevator elevator2 = new Elevator(2, floorRequest2);
		elevatorList.add(elevator2);

		Thread elevatorThread2 = new Thread(elevator2, "Elevator 2");
		elevatorThread2.start();
		//System.out.println("--- elevatorThread2 STARTED.");
		Output.print("Elevator", "Main", Output.INFO,"--- elevatorThread2 STARTED.");

		FloorRequest floorRequest3 = new FloorRequest();
		Elevator elevator3 = new Elevator(3, floorRequest3);
		elevatorList.add(elevator3);

		Thread elevatorThread3 = new Thread(elevator3, "Elevator 3");
		elevatorThread3.start();
		//System.out.println("--- elevatorThread3 STARTED.");
		Output.print("Elevator", "Main", Output.INFO,"--- elevatorThread3 STARTED.");


	}

	/**
	 * 
	 * method to process the received requests from the hosts
	 * @param receivedPacket
	 * @return
	 * 
	 */

	private void processReceivedBytes(DatagramPacket receivedPacket) throws Exception {
		// Host Details
		InetAddress hostIP = receivedPacket.getAddress();
		int hostPort = receivedPacket.getPort();

		// Get data from the received packet.
		byte[] receivedBytes = receivedPacket.getData();
		//System.out.println("Received Length: " + receivedBytes.length);
		Output.print("Elevator", "Main", Output.INFO,"Received Length: " + receivedBytes.length);


		String request = new String(receivedBytes);
		//System.out.println("Received REQUEST: " + request);
		Output.print("Elevator", "Main", Output.INFO,"Received REQUEST: " + request);


		if (request.startsWith("floor request elevator ")) {
			processFloorRequest(request, hostIP, hostPort);
		} else {
			// Throw Exception
//			PENDING SEND RESPONSE
//			throw new Exception("Invalid request.");
			//System.out.println("Invalid request.");
			Output.print("Elevator", "Main", Output.INFO,"Invalid request.");

		}

	}
	/**
	 * processing the requests
	 * @param  request hostIP hostPort
	 * @return
	 * 
	 */

	private void processFloorRequest(String request, InetAddress hostIP, int hostPort) throws Exception {

		// Format needed:
		// floor request elevator <ELEVATOR ID> <FLOOR NUMBER>
		String[] tokens = request.split(" ");
		int originFloor = Integer.parseInt(tokens[3]);
		int destFloor = Integer.parseInt(tokens[4]);

		// request elevator to ORIGIN floor
		Elevator elevator = dispatchFloorRequest(null, originFloor);

		// request elevator to DEST floor
		dispatchFloorRequest(elevator, destFloor);

		byte[] replyBytes = "DONE".getBytes();
		send(replyBytes, hostIP, hostPort);
	}

	/**
	 * Dispatch floor request to an close by elevator.
	 * 
	 * @param targetFloor
	 * @return
	 */
	private Elevator dispatchFloorRequest(Elevator elevator, int targetFloor) {
		if (elevator == null) {
			// find close by elevator
			elevator = getCloseByElevator(targetFloor);
			//System.out.println("CLOSE BY Elevator: " + elevator.getElevatorID());
			Output.print("Elevator", "Main", Output.INFO,"CLOSE BY Elevator: " + elevator.getElevatorID());

			
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
				floorRequest.setFloor(targetFloor);
				floorRequest.notifyAll();
			}
		} // synchronized
		return elevator;
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
	 * @param  bytes, hostIP,  hostPort
	 */
	private void send(byte[] bytes, InetAddress hostIP, int hostPort) throws IOException {
//		//Create Send Packet 
		DatagramPacket packetSend = new DatagramPacket(bytes, bytes.length, hostIP, hostPort);
		//
//		//Send Packet
		serverSocket.send(packetSend);
		//System.out.println("Sent reply: " + new String(bytes));
		Output.print("Elevator", "Main", Output.INFO,"Sent reply: " + new String(bytes));

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
	public boolean moveTo(int destinationFloor) {

		// Check if motor is running
//		while (motorOperating) {
//			// wait until motor stops
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				System.err.println(e);
//			}
//		}

		// Checks where the Elevator is
		// If Elevator at destination
		if (currentFloor == destinationFloor) {
			//System.out.println("Elevator already at: " + destinationFloor);
			Output.print("Elevator", "Main", Output.INFO,"Elevator already at: " + destinationFloor);


		} else {
			/* if motor is running, Doors must be closed 
			currentState = State.MOVING; // ensure the state Doors Closed is active
			if (currentState == State.MOVING) {
				//System.out.println("Doors are now closed. Elevator MOVING. ");
				Output.print("Elevator", "Main", Output.INFO,"Doors are now closed. Elevator MOVING. ");

			}
			//System.out.println("Elevator moving from: " + currentFloor + " to: " + destinationFloor);
			Output.print("Elevator", "Main", Output.INFO,"Elevator moving from: " + currentFloor + " to: " + destinationFloor);

			motorsOn();
			// Move number of floors
			moveElevator(Math.abs(currentFloor - destinationFloor));
			motorOff();
			// For each move currentFloor gets changed here
			currentFloor = destinationFloor;
			//System.out.println("Elevator reached: " + destinationFloor);
			Output.print("Elevator", "Main", Output.INFO,"Elevator reached: " + destinationFloor);

		}
		currentState = State.STILL;
		// Opens door
		// openDoor(destinationFloor);
		//System.out.println("Elevator reached: " + destinationFloor);
		Output.print("Elevator", "Main", Output.INFO,"Elevator reached: " + destinationFloor);

		//System.out.println("Elevator Door opened at: " + destinationFloor);
		Output.print("Elevator", "Main", Output.INFO,"Elevator Door opened at: " + destinationFloor);

		return true;
	}
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
	/*

	/**
	 * Simulates elevator moving floors
	 * 
	 * @param numberOfFloors, floors that elevator will move
	 
	private void moveElevator(int numberOfFloors) {
		// delay
		try {
			for (int i = 0; i < numberOfFloors; i++) {
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
		}
	}
*/
	/**
	 * main method
	 */

	public static void main(String[] args) {
		MainElevatorSys server;
		try {
			server = new MainElevatorSys();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("\n\n ERROR: " + e.getMessage());
			Output.print("Elevator", "Main", Output.FATAL, e.getMessage());
			System.exit(1);
		}
	}
}


