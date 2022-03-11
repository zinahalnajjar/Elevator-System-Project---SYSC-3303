/**
 * MainFloorSys.java
 * 
 * This class client sends and receives data in the form of bytes from/to
 * the scheduler
 * 
 * Comments using the // format belong to old print statements 
 * kept for debugging
 * 
 * @author Zinah, Mack, Vilmos
 *
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

	public static final byte[] validReadReply = new byte[] { 0, 3, 0, 1 };
	public static final byte[] validWriteReply = new byte[] { 0, 4, 0, 0 };

	/** send and receive packets */
	private DatagramPacket sendPacket, receivePacket;
	/** one socket that is either a read request or a write request */
	private DatagramSocket schedulerSocket;

	/** IP address and port for the Scheduler */
	private InetAddress schedulerIp;
	private int schedulerPort;


	/**
	 * base constructor
	 * 
	 * @throws UnknownHostException
	 */
	public MainFloorSys() throws UnknownHostException {
		/** Host details */
		schedulerIp = InetAddress.getByName("localhost");
		schedulerPort = 23;

	}

	/**
	 * send, read, write, invalid requests
	 * 
	 * @throws IOException
	 */
	public void packetRequests() throws IOException {
		for (int i = 1; i <= 10; i++) {
			//System.out.println("----BEGIN Request: " + i);
			Output.print("MainFloorSys", "packetRequests", Output.INFO, "----BEGIN Request: " + i);
			if (i % 2 == 0) {
				sendRequest("read");
			} else {
				sendRequest("write");
			}
			//System.out.println("----END Request: " + i);
			Output.print("MainFloorSys", "packetRequests", Output.INFO, "----END Request: " + i);
			System.out.println("=============================");
		}
		/** send INVALID REQUEST */
		//System.out.println("----BEGIN INVALID REQUEST: ");
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "----BEGIN INVALID REQUEST: ");
		byte[] invalidBytes = new byte[] { 9 };
		send(invalidBytes);
		//System.out.println("----END INVALID REQUEST: ");
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "----END INVALID REQUEST: ");
	}

	/**
	 * Send requests to host
	 * 
	 * @param request
	 * @throws IOException
	 */
	private void sendRequest(String request) throws IOException {
		//System.out.println("Sending request: " + request);
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "Sending request: " + request);

		byte[] sendBytes = request.getBytes();
 
		/** invoke the send method */
		send(sendBytes);

		//System.out.println("Sent request: " + request);
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "Sent request: " + request);
		
		delay();

		/** receive request from the Scheduler */
		byte[] inBytes = new byte[1024];
		DatagramPacket fromHostPacket = new DatagramPacket(inBytes, inBytes.length);
		//System.out.println("Awaiting reply from Scheduler...");
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "Awaiting reply from Scheduler...");
		schedulerSocket.receive(fromHostPacket);

		/** Get data from the received packet. */
		byte[] receivedBytes = fromHostPacket.getData();

		//System.out.println("Reply Received from Host.");
		Output.print("MainFloorSys", "packetRequests", Output.INFO, "Reply received from Host");

		boolean validReply = true; /** flag for valid reply */
		/** decide the type of response from the client to the Scheduler based on the
		 * request
		 * received back
		 */

		String response = new String(receivedBytes);

		/** if we have a invalid request received */
		if (validReply) {
			//System.out.println("VALID reply received: " + response);
			Output.print("MainFloorSys", "packetRequests", Output.INFO, "VALID reply received: " + response);
		} else {
			//System.out.println("INVALID reply received: " + response);
			Output.print("MainFloorSys", "packetRequests", Output.INFO, "INVALID reply received: " + response);
		}
	}

	/**
	 * delay by 0.1 seconds
	 */
	private void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Output.print("MainFloorSys", "delay", Output.FATAL, "ERROR delaying");
			e.printStackTrace();
		}
	}

	/**
	 * sends the request to the host
	 * 
	 * @param outBytes, byte[] containing the request
	 * @throws UnknownHostException
	 */
	public void send(byte outBytes[]) throws UnknownHostException {

		try {
			schedulerSocket = new DatagramSocket();
		} catch (SocketException e) {
			Output.print("MainFloorSys", "send", Output.FATAL, "SOCKET CREATION FAILURE");
			e.printStackTrace();
		}

		/** Send Packet */
		sendPacket = new DatagramPacket(outBytes, outBytes.length, schedulerIp, schedulerPort);

		try {
			schedulerSocket.send(sendPacket);
		} catch (IOException e) {
			Output.print("MainFloorSys", "send", Output.FATAL, "FAILED TO SEND PACKET");
			e.printStackTrace();
		}

	}

	/** 
	 * receive reply from the scheduler
	 */
	public void receive() {
		/** receive reply from the Scheduler
		 * Receive Bytes
		 */

		byte[] receiveBytes = new byte[1024];

		receivePacket = new DatagramPacket(receiveBytes, receiveBytes.length);

		try {
			schedulerSocket.receive(receivePacket);
		} catch (IOException e) {
			Output.print("MainFloorSys", "receive", Output.FATAL, "FAILED TO RECEIVE PACKET");
			e.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MainFloorSys c;
		try {
			c = new MainFloorSys();
			c.floorRequests();
		/**	c.packetRequests(); */
		} catch (Exception e) {
			Output.print("MainFloorSys", "main", Output.FATAL, "FAILED TO CREATE FLOOR SYSTEM");
			e.printStackTrace();
		}
	}

	/**
	 * Gets requests from an input file and executes each one(one at a time)
	 * 
	 * @throws IOException
	 */
	private void floorRequests() throws IOException {
		int i = 1;
		ArrayList<FloorMovementData> lines = getInfo();
		for (FloorMovementData floorRequest : lines) {
			//System.out.println("----*** new BEGIN Request: " + i++);
			Output.print("MainFloorSys", "floorRequests", Output.INFO,"----*** new BEGIN Request: " + i++);
			/** FORMAT:
			 * floor request elevator <ORIGIN FLOOR NUMBER> <DESTINATION FLOOR NUMBER> END
			 * Convert file format into 'RPC format'
			 */

			String request = "floor request elevator " + floorRequest.getOriginFloor() + " "
					+ floorRequest.getDestinationFloor() + " END";
			sendRequest(request);
			
			//System.out.println("----END Request: " + i);
			Output.print("MainFloorSys", "floorRequests", Output.INFO,"----END Request: " + i);
			System.out.println("=============================");
			delay();
		} 
		/** send INVALID REQUEST */
		//System.out.println("----BEGIN INVALID REQUEST: ");
		Output.print("MainFloorSys", "floorRequests", Output.INFO,"----BEGIN INVALID REQUEST: ");
		byte[] invalidBytes = new byte[] { 9 };
		send(invalidBytes);
		//System.out.println("----END INVALID REQUEST: ");
		Output.print("MainFloorSys", "floorRequests", Output.INFO,"----END INVALID REQUEST: ");

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
					//System.out.println("Line: " + line);
					Output.print("MainFloorSys", "getInfo", Output.INFO,"Line: " + line);
					String[] tokens = line.split(",");
					Integer originFloor = Integer.valueOf(tokens[1]);
					LocalTime time = LocalTime.parse(tokens[0]);
					Integer destinationFloor = Integer.valueOf(tokens[3]);
					Boolean goingUp = Boolean.valueOf(tokens[2]);
					FloorMovementData fD = new FloorMovementData(time, originFloor, destinationFloor, goingUp);
					floorInfo.add(fD);
				} catch (Exception e) {
					//System.out.println("Error: INVALID File format. Ignoring line: " + line);
					Output.print("MainFloorSys", "getInfo", Output.WARNING,"Error: INVALID File format. Ignoring line: " + line);
				}

			}
			scanner.close();
		} catch (FileNotFoundException e) {
			//System.out.println("error");
			Output.print("MainFloorSys", "getInfo", Output.FATAL,"NO FILE FOUND");
			e.printStackTrace();
		}

		return floorInfo;

	}

}
