/*
 * class responsible for reading the input file and sending the requests to the scheduler 
 * 
 * @author Zinah, Mack 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class MainFloorSys {

	// Number of elevators
	private static final int ELEVATOR_COUNT = 4;

	// GUI
	private static ElevatorDashboardGUI gui;

	// send and receive packets
	private DatagramPacket sendPacket, receivePacket;
	// one socket that is either a read request or a write request
	private DatagramSocket schedulerSocket;

	// IP address and port for the Scheduler
	private InetAddress schedulerIp;
	private int schedulerPort;

	/**
	 * constructor
	 */

	public MainFloorSys() throws UnknownHostException {
		// Scheduler details
		schedulerIp = InetAddress.getByName("localhost");
		schedulerPort = 23;

	}

	/**
	 * @throws IOException
	 * 
	 */
	private void processRequest(String request) throws IOException {
		sendRequest(request);

		delay();

		receiveResponse();
	}

	private void sendRequest(String request) throws UnknownHostException {
		// System.out.println("Sending request: " + request);
		Output.print("Floor", "Main", Output.INFO, "Sending request: " + request);

		byte[] sendBytes = request.getBytes();

		// invoke the send method
		send(sendBytes);

		// System.out.println("Sent request: " + request);
		Output.print("Floor", "Main", Output.INFO, "Sent request: " + request);
	}

	private void receiveResponse() throws IOException {
		// receive request from the Scheduler
		byte[] inBytes = new byte[1024];
		DatagramPacket fromSchedulerPacket = new DatagramPacket(inBytes, inBytes.length);
		// System.out.println("Awaiting reply from Scheduler...");
		Output.print("Floor", "Main", Output.INFO, "Awaiting reply from Scheduler...");

		schedulerSocket.receive(fromSchedulerPacket);

		// Get data from the received packet.
		byte[] receivedBytes = fromSchedulerPacket.getData();

		// Print
		// System.out.println("Reply Received from Scheduler.");
		Output.print("Floor", "Main", Output.INFO, "Reply Received from Scheduler.");

		// received back
		String response = new String(receivedBytes);

//		boolean validReply = processResponse(response); // flag for valid reply
		boolean validReply = true;
		// decide the type of reponse from the client to the Scheduler based on the

		// check if we have a invalid request received
		if (validReply) {
			// System.out.println("VALID reply received: " + response);
			Output.print("Floor", "Main", Output.INFO, "VALID reply received: " + response);

		} else {
			System.out.println("INVALID reply received: " + response);
			Output.print("Floor", "Main", Output.INFO, "INVALID reply received: " + response);

		}
	}

	/**
	 * Processes each response and update GUI.
	 * 
	 * @param response
	 * @return
	 */
	private boolean processResponse(String response) {
		if (response == null) {
			return false;
		}

		response = response.trim();

		if (response.isEmpty()) {
			return false;
		}

		System.out.println("...PENDING GUI UPDATE: " + response);
		if (response.equals("DONE")) {
			return true;
		}

		if (response.startsWith("elevator ")) {
			processElevatorPosition(response);
		} else {
			// Throw Exception
//			PENDING SEND RESPONSE
//			throw new Exception("Invalid response.");
			// System.out.println("Invalid response.");
			Output.print("Floor", "Main", Output.INFO, "Invalid response.");

		}

		return true;
	}

	private void processElevatorPosition(String response) {
		System.out.println("... GUI UPDATE: " + response);

		// Format needed:
		// elevator <ELEVATOR ID> <FLOOR NUMBER> <ERROR> <STATE> END
		String[] tokens = response.split(" ");
		int elevatorID = Integer.parseInt(tokens[1]);
		int floor = Integer.parseInt(tokens[2]);
		int error = Integer.parseInt(tokens[3]);
		String state = tokens[4];
		
		//Update GUI
//		gui.updateView(elevatorID, floor, error, state);

	}

	/**
	 * delay
	 */
	private void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sending requests to Scheduler
	 */

	public void send(byte outBytes[]) throws UnknownHostException {

		try {
			schedulerSocket = new DatagramSocket();
		} catch (SocketException e) {

			e.printStackTrace();
		}

		// Send Packet
		sendPacket = new DatagramPacket(outBytes, outBytes.length, schedulerIp, schedulerPort);

		try {
			schedulerSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * receive reply from the Scheduler
	 */

	public void receive() {
		// receive reply from the Scheduler
		// Receive Bytes
		byte[] receiveBytes = new byte[1024];

		receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

		try {
			schedulerSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public static void main(String[] args) {
		try {

			// Start floor sub system
			MainFloorSys c = new MainFloorSys();
			c.floorRequests();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * send a RPC containing the requests that were read from the input file
	 * 
	 * @throws IOException
	 */
	private void floorRequests() throws IOException {
		// note the start time
		long startTime = System.currentTimeMillis();

		int i = 1;
		ArrayList<FloorMovementData> lines = getInfo();
		for (FloorMovementData floorRequest : lines) {
			Output.print("Floor", "Main", Output.INFO, "----BEGIN Request: " + i++);
			// FORMAT:
			// floor request elevator <ORIGIN FLOOR NUMBER> <DESTINATION FLOOR NUMBER> END
			// Convert file format into 'RPC format'

			String request = "floor request elevator " + floorRequest.getOriginFloor() + " "
					+ floorRequest.getDestinationFloor() + " " + floorRequest.getError() + " END";

			processRequest(request);

			Output.print("Floor", "Main", Output.INFO, "----END Request: " + i);

			System.out.println("=============================");
			delay();
		}
		// note the end time
		long endTime = System.currentTimeMillis();

		long timeTakenMillis = endTime - startTime;

		Output.print("Floor", "Main", Output.INFO,
				"  Time taken to process the entire input file: " + timeTakenMillis + " milliseconds.");

		// send INVALID REQUEST
		// System.out.println("----BEGIN INVALID REQUEST: ");
		Output.print("Floor", "Main", Output.INFO, "----BEGIN INVALID REQUEST: ");

		byte[] invalidBytes = new byte[] { 9 };
		send(invalidBytes);
		// System.out.println("----END INVALID REQUEST: ");
		Output.print("Floor", "Main", Output.INFO, "----END INVALID REQUEST: ");

	}

	/**
	 * takes a text file and converts it into requests for the floor
	 * 
	 * @return floorInfo, contains the requests as an ArrayList
	 */
	public static ArrayList<FloorMovementData> getInfo() {
		ArrayList<FloorMovementData> floorInfo = new ArrayList<>();
		try {
			File fileReader = new File("./src/InputInformation.txt");
			Scanner scanner = new Scanner(fileReader);
			while (scanner.hasNext()) {
				String line = null;
				try {
					line = scanner.nextLine();
					System.out.println("Line: " + line);
					String[] tokens = line.split(",");
					Integer originFloor = Integer.valueOf(tokens[1]);
					LocalTime time = LocalTime.parse(tokens[0]);
					Integer destinationFloor = Integer.valueOf(tokens[3]);
					Boolean goingUp = Boolean.valueOf(tokens[2]);
					Integer error = Integer.valueOf(tokens[4]); // made error as token[4] cuz I added an error number at
																// the last part of the input info text file
					FloorMovementData fD = new FloorMovementData(time, originFloor, destinationFloor, goingUp, error); // made
																														// sure
																														// to
																														// include
																														// error
					floorInfo.add(fD);
				} catch (Exception e) {
					// System.out.println("Error: INVALID File format. Ignoring line: " + line);
					Output.print("Floor", "Main", Output.WARNING, "----END INVALID REQUEST: ");

				}

			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// System.out.println("error");
			Output.print("Floor", "Main", Output.FATAL, "error");

		}

		return floorInfo;

	}

}