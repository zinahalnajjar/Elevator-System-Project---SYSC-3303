/*
 * client sends and receive data in the form of byte from the Scheduler 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MainFloorSys {

	public static final byte[] validReadReply = new byte[] { 0, 3, 0, 1 };
	public static final byte[] validWriteReply = new byte[] { 0, 4, 0, 0 };

	// send and receive packets
	private DatagramPacket sendPacket, receivePacket;
	// one socket that is either a read request or a write request
	private DatagramSocket schedulerSocket;

	// IP address and port for the Scheduler
	private InetAddress schedulerIp;
	private int schedulerPort;

	/*
	 * constructor
	 */

	public MainFloorSys() throws UnknownHostException {
		// Host details
		schedulerIp = InetAddress.getByName("localhost");
		schedulerPort = 23;

	}

	/*
	 * send read, write, invalid requests
	 */

	public void packetRequests() throws IOException {
		for (int i = 1; i <= 10; i++) {
			System.out.println("----BEGIN Request: " + i);
			if (i % 2 == 0) {
				sendRequest("read");
			} else {
				sendRequest("write");
			}
			System.out.println("----END Request: " + i);
			System.out.println("=============================");
		}
		// send INVALID REQUEST
		System.out.println("----BEGIN INVALID REQUEST: ");
		byte[] invalidBytes = new byte[] { 9 };
		send(invalidBytes);
		System.out.println("----END INVALID REQUEST: ");
	}

	/**
	 * @throws IOException
	 * 
	 */
	private void sendRequest(String request) throws IOException {
		System.out.println("Sending request: " + request);

		byte[] sendBytes = request.getBytes();

		// invoke the send method
		send(sendBytes);

		System.out.println("Sent request: " + request);
		delay();

		// receive request from the Scheduler
		byte[] inBytes = new byte[1024];
		DatagramPacket fromHostPacket = new DatagramPacket(inBytes, inBytes.length);
		System.out.println("Awaiting reply from Scheduler...");
		schedulerSocket.receive(fromHostPacket);

		// Get data from the received packet.
		byte[] receivedBytes = fromHostPacket.getData();

		// Print
		System.out.println("Reply Received from Host.");

		boolean validReply = true; // flag for valid reply
		// decide the type of reponse from the client to the Scheduler based on the
		// request
		// received back

		String response = new String(receivedBytes);

		// if we have a invalid request received
		if (validReply) {
			System.out.println("VALID reply received: " + response);
		} else {
			System.out.println("INVALID reply received: " + response);
		}
	}

	/*
	 * delay
	 */
	private void delay() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * /*sending requests to Scheduler
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
	/*
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
		MainFloorSys c;
		try {
			c = new MainFloorSys();
			c.floorRequests();
//			c.packetRequests();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void floorRequests() throws IOException {
		for (int i = 1; i <= 1; i++) {
			System.out.println("----BEGIN Request: " + i);
			// FORMAT:
			// floor request elevator <ELEVATOR ID> <FLOOR NUMBER> END
			sendRequest("floor request elevator 1 3 END");
			System.out.println("----END Request: " + i);
			System.out.println("=============================");
			delay();
		}
		// send INVALID REQUEST
		System.out.println("----BEGIN INVALID REQUEST: ");
		byte[] invalidBytes = new byte[] { 9 };
		send(invalidBytes);
		System.out.println("----END INVALID REQUEST: ");

	}

}
