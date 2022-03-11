/**
 * MainSchedulerSys.java
 * 
 * host class; it will work like a channel between the client and the server
 * from client's perspective it will act like a server
 * from server's perspective it will act like a client
 * 
 * Comments using the // format belong to old print statements 
 * kept for debugging
 * 
 * @author Zinah, Mack, Vilmos
 *
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class MainSchedulerSys {

	/** Receive Floor request AND reply back. */
	private DatagramSocket hostSocketForFloor;

	/** IP and port address */
	private InetAddress serverIp;
	private int ELEVATOR_PORT = 69;

	private int HOST_PORT = 23;

	/**
	 * base constructor
	 * 
	 * @throws UnknownHostException
	 * @throws SocketException
	 */
	public MainSchedulerSys() throws UnknownHostException, SocketException {
		/** Host details */
		serverIp = InetAddress.getByName("localhost");

		/** Construct a datagram socket and bind it to port HOST_PORT
		 * on the local host machine.
		 */
		hostSocketForFloor = new DatagramSocket(HOST_PORT);

	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MainSchedulerSys host;
		try {
			host = new MainSchedulerSys();
			host.start();
		} catch (Exception e) {
			Output.print("Scheduler", "Main", Output.FATAL, "ERROR CREATING SCHEDULER");
			e.printStackTrace();
		}
	}

	/**
	 * start method will send and receive requests from both client and server
	 * 
	 * @throws IOException
	 */
	private void start() throws IOException {
		while (true) {

			/** data holder */
			byte[] inBytes;

			/** receive from client */
			inBytes = new byte[1024];
			DatagramPacket clientPacket = new DatagramPacket(inBytes, inBytes.length);

			/** Listen for client request */
			Output.print("MainSchedulerSys", "Main", Output.INFO, "Awaiting data...");
			hostSocketForFloor.receive(clientPacket);

			/** Floor Details */
			InetAddress clientIP = clientPacket.getAddress();
			int clientPort = clientPacket.getPort();

			/** Get data from the received packet. */
			byte[] receivedBytes = clientPacket.getData();

			/** Print */
			//System.out.println("Received from Floor - Bytes: " + Arrays.toString(receivedBytes));
			Output.print("MainSchedulerSys", "Main", Output.INFO, "Received from Floor - Bytes: " + Arrays.toString(receivedBytes));
			//System.out.println("Received from Floor - String: " + new String(receivedBytes));
			Output.print("MainSchedulerSys", "Main", Output.INFO, "Received from Floor - String: " + new String(receivedBytes));

			String request = new String(receivedBytes);

			if (request.startsWith("floor ")) {
				processRequestFromFloor(request, clientIP, clientPort);
			} else if (request.startsWith("elevator ")) {
				// pending
			}

			delay();

			System.out.println("=============================");

		} // while
	}

	/**
	 * delay by 0.1 seconds
	 */
	private void delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Output.print("MainSchedulerSys", "delay", Output.FATAL, "ERROR delaying");
			e.printStackTrace();
			
		}
	}

	/**
	 * sends the request from floor to elevator
	 * 
	 * @param request, the request
	 * @param clientIP, IP address of the client (elevator)
	 * @param clientPort, PORT of the client (elevator)
	 * @throws IOException
	 */
	private void processRequestFromFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		if (request.startsWith("floor request elevator ")) {
			requestElevatorToFloor(request, clientIP, clientPort);
		}
	}

	/**
	 * sends the reply from elevator to floor
	 * 
	 * @param request, the reply
	 * @param clientIP, IP address of client (floor)
	 * @param clientPort, PORT of the client (floor)
	 * @throws IOException
	 */
	private void requestElevatorToFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		byte[] requestBytes = request.getBytes();

		/** send request to Elevator */
		DatagramPacket toElevatorPacket = new DatagramPacket(requestBytes, requestBytes.length, serverIp,
				ELEVATOR_PORT); /** might have something to do with this in order to check the elevator ports. */
		hostSocketForFloor.send(toElevatorPacket);
		//System.out.println("Forwarded to Elevator.");
		Output.print("MainSchedulerSys", "requestElevatorToFloor", Output.INFO, "Forwarded to Elevator");
		delay();

		/** receive reply from server */
		byte[] inBytes = new byte[4];
		DatagramPacket fromServerPacket = new DatagramPacket(inBytes, inBytes.length);
		//System.out.println("Awaiting reply from Elevator...");
		Output.print("MainSchedulerSys", "requestElevatorToFloor", Output.INFO, "Awaiting reply from Elevator");
		hostSocketForFloor.receive(fromServerPacket);

		/** Get data from the received packet. */
		byte[] receivedBytes = fromServerPacket.getData();

		/** Print */
		//System.out.println("Received from Elevator - Bytes: " + Arrays.toString(receivedBytes));
		Output.print("MainSchedulerSys", "requestElevatorToFloor", Output.INFO, "Received from Elevator - Bytes: " + Arrays.toString(receivedBytes));
		//System.out.println("Received from Elevator - String: " + new String(receivedBytes));
		Output.print("MainSchedulerSys", "requestElevatorToFloor", Output.INFO, "Received from Elevator - String: " + new String(receivedBytes));
		
		/** forward to client */
		DatagramPacket toFloorPacket = new DatagramPacket(receivedBytes, receivedBytes.length, clientIP, clientPort);
		hostSocketForFloor.send(toFloorPacket);
		//System.out.println("Forwarded to Floor: " + new String(receivedBytes));
		Output.print("MainSchedulerSys", "requestElevatorToFloor", Output.INFO, "Forwarded to Floor: " + new String(receivedBytes));
		delay();

		System.out.println("=============================");

	}

}