/*
 * class server sends and receive requests from the host 
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class MainElevatorSys {
	private static final int SERVER_PORT = 69;

	public static final byte[] validReadReply = new byte[] { 0, 3, 0, 1 };
	public static final byte[] validWriteReply = new byte[] { 0, 4, 0, 0 };

	private DatagramPacket receivedPacket;
	private DatagramSocket serverSocket;

	private int currentFloor = 0;

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

		try {
			byte[] inBytes;
			while (true) {
				System.out.println("Awaiting Host data...");
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
		int elevatorId = Integer.parseInt(tokens[3]);
		int requestedFloorNum = Integer.parseInt(tokens[4]);

		boolean atFloor = (requestedFloorNum == currentFloor);
		// CHECK if Elevator is at the floorNum
		if (!atFloor) {
			// move elevator to requestedFloorNum
			// pending print motor ON/running/STOP
			System.out.println("pending move elevator to requestedFloorNum");
			System.out.println("pending print motor ON/running/STOP");
			atFloor = true;
		}

		String reply;
		if (atFloor) {
			reply = "elevator " + elevatorId + " at " + requestedFloorNum;
		} else {
			reply = "error";
		}

		byte[] replyBytes = reply.getBytes();
		send(replyBytes, hostIP, hostPort);
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
