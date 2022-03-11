/*
 * class server sends and receive requests from the host 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainElevatorSys {
	private static final int SERVER_PORT = 69;

	public static final byte[] validReadReply = new byte[] { 0, 3, 0, 1 };
	public static final byte[] validWriteReply = new byte[] { 0, 4, 0, 0 };

	private DatagramPacket receivedPacket;
	private DatagramSocket serverSocket;

	private int currentFloor;
	private boolean motorOperating;
	public State currentState;

	private List<Elevator> elevatorList = new ArrayList<Elevator>();

	enum State {
		STILL, MOVING
	}

	/*
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

	/*
	 * method to receive requests from the host
	 */
	public void start() {
		startElevatorThreads();
		System.out.println("-------Thread Started");
		try {
			byte[] inBytes;
			while (true) {
				System.out.println("Awaiting Schedular data...");
				inBytes = new byte[1024];
				receivedPacket = new DatagramPacket(inBytes, inBytes.length);

				try {
					serverSocket.receive(receivedPacket);
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}

				processReceivedBytes(receivedPacket);
				System.out.println("============================");

			} // while
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Close serverSocket...");
			serverSocket.close();
		}

	}

	private void startElevatorThreads() {

		FloorRequest floorRequest1 = new FloorRequest();
		Elevator elevator1 = new Elevator(1, floorRequest1);
		elevatorList.add(elevator1);

		Thread elevatorThread1 = new Thread(elevator1, "Elevator 1");
		elevatorThread1.start();
		System.out.println("--- AFTER elevatorThread1 STARTED.");

		FloorRequest floorRequest2 = new FloorRequest();
		Elevator elevator2 = new Elevator(2, floorRequest2);
		elevatorList.add(elevator2);

		Thread elevatorThread2 = new Thread(elevator2, "Elevator 2");
		elevatorThread2.start();
		System.out.println("--- AFTER elevatorThread2 STARTED.");

	}

	/*
	 * method to process the received requests from the hosts
	 */

	private void processReceivedBytes(DatagramPacket receivedPacket) throws Exception {
		// Host Details
		InetAddress hostIP = receivedPacket.getAddress();
		int hostPort = receivedPacket.getPort();

		// Get data from the received packet.
		byte[] receivedBytes = receivedPacket.getData();
		System.out.println("Received Length: " + receivedBytes.length);

		String request = new String(receivedBytes);
		System.out.println("Received REQUEST: " + request);

		if (request.startsWith("floor request elevator ")) {
			processFloorRequest(request, hostIP, hostPort);
		} else {
			// Throw Exception
//			PENDING SEND RESPONSE
//			throw new Exception("Invalid request.");
			System.out.println("Invalid request.");
		}

	}

	private void processFloorRequest(String request, InetAddress hostIP, int hostPort) throws Exception {

		// Format:
		// floor request elevator <ELEVATOR ID> <FLOOR NUMBER>
		String[] tokens = request.split(" ");
		int originFloor = Integer.parseInt(tokens[3]);
		int destFloor = Integer.parseInt(tokens[4]);

		// ---OLD VERSION --- BEGIN
//		//request elevator to ORIGIN floor
//		moveTo(originFloor);
//		System.out.println("Reached ORIGIN floor. Users board the car and PRESS DESTINATION: " + destFloor);
//		
//		//request elevator to DEST floor
//		moveTo(destFloor);
		// ---OLD VERSION --- END

		// request elevator to ORIGIN floor
		dispatchFloorRequest(originFloor);
		System.out.println("Reached ORIGIN floor. Users board the car and PRESS DESTINATION: " + destFloor);

		// request elevator to DEST floor
		dispatchFloorRequest(destFloor);
		System.out.println("Reached DEST floor. Users board the car and PRESS DESTINATION: " + destFloor);

		byte[] replyBytes = "DONE".getBytes();
		send(replyBytes, hostIP, hostPort);
	}

	/**
	 * Dispatch floor request to an close by elevator.
	 * 
	 * @param targetFloor
	 */
	private void dispatchFloorRequest(int targetFloor) {
		// find close by elevator
		Elevator elevator = getCloseByElevator(targetFloor);
		System.out.println("CLOSE BY Elevator: " + elevator.getElevatorID());

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

	/*
	 * send reply to host
	 */
	private void send(byte[] bytes, InetAddress hostIP, int hostPort) throws IOException {
//		//Create Send Packet 
		DatagramPacket packetSend = new DatagramPacket(bytes, bytes.length, hostIP, hostPort);
		//
//		//Send Packet
		serverSocket.send(packetSend);
		System.out.println("Sent reply: " + Arrays.toString(bytes));
		delay();

	}

	/*
	 * delay method
	 */

	private void delay() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * method to confirm that the format is valid
	 */
	private boolean processRequest(byte[] receivedBytes, String requestType) {
		int offset;
		int zeroByteIndex;
		// ignore first 2 bytes. start index from 2.
		offset = 2;
		zeroByteIndex = getIndexOfZeroByte(receivedBytes, offset);
		String fileName = getTextUntilZeroByte(receivedBytes, offset, zeroByteIndex);
		if (fileName == null || fileName.trim().isEmpty()) {
			return false;
		}
		System.out.println("FileName: " + fileName);

		// start index from PREVIOUS zeroByteIndex + 1

		offset = zeroByteIndex + 1;
		zeroByteIndex = getIndexOfZeroByte(receivedBytes, offset);
		String mode = getTextUntilZeroByte(receivedBytes, offset, zeroByteIndex);
		if (mode == null || mode.trim().isEmpty()) {
			return false;
		}
		System.out.println("Mode: " + mode);

		return true;
	}

	/*
	 * look for 0 byte index
	 */
	private int getIndexOfZeroByte(byte[] receivedBytes, int offset) {

		int zeroByteIndex = -1;
		for (int i = offset; i < receivedBytes.length; i++) {
			if (receivedBytes[i] == 0) {
				zeroByteIndex = i;
				break;
			}
		}
		return zeroByteIndex;
	}

	/*
	 * get text between 2 and zeroByteIndex 7 (excluding)
	 */
	private String getTextUntilZeroByte(byte[] receivedBytes, int offset, int zeroByteIndex) {
		// example:
		// 0 1 2 3 4 5 6 7
		// a . t x t 0
		int length = zeroByteIndex - 2; // 7 - 2 = 5 (length)
		String text = new String(receivedBytes, offset, length);
		return text;
	}

	/**
	 * moves elevator to desired floor
	 * 
	 * @param destinationFloor, floor to move elevator to
	 * @return
	 */
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
			System.out.println("Elevator already at: " + destinationFloor);

		} else {
			/* if motor is running, Doors must be closed */
			currentState = State.MOVING; // ensure the state Doors Closed is active
			if (currentState == State.MOVING) {
				System.out.println("Doors are now closed. Elevator MOVING. ");
			}
			System.out.println("Elevator moving from: " + currentFloor + " to: " + destinationFloor);
			motorsOn();
			// Move number of floors
			moveElevator(Math.abs(currentFloor - destinationFloor));
			motorOff();
			// For each move currentFloor gets changed here
			currentFloor = destinationFloor;
			System.out.println("Elevator reached: " + destinationFloor);
		}
		currentState = State.STILL;
		// Opens door
		// openDoor(destinationFloor);
		System.out.println("Elevator reached: " + destinationFloor);
		System.out.println("Elevator Door opened at: " + destinationFloor);
		return true;
	}

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

	/**
	 * Simulates elevator moving floors
	 * 
	 * @param numberOfFloors, floors that elevator will move
	 */
	private void moveElevator(int numberOfFloors) {
		// delay
		try {
			for (int i = 0; i < numberOfFloors; i++) {
				Thread.sleep(500);
			}
		} catch (InterruptedException e) {
		}
	}

	/*
	 * main method
	 */

	public static void main(String[] args) {
		MainElevatorSys server;
		try {
			server = new MainElevatorSys();
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\n\n ERROR: " + e.getMessage());
			System.exit(1);
		}
	}
}