/*
 * scheduler works like a channel between the elevator and the floor 
 * @author Zinah, Mack 
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainSchedulerSys {

	// Receive Floor request AND reply back.
	private DatagramSocket hostSocketForFloor;

	// IP and port address
	private InetAddress serverIp;
	private int ELEVATOR_PORT = 69;

	private int HOST_PORT = 23;

	public MainSchedulerSys() throws UnknownHostException, SocketException {
		// Host details
		serverIp = InetAddress.getByName("localhost");

		// Construct a datagram socket and bind it to port HOST_PORT
		// on the local host machine.
		hostSocketForFloor = new DatagramSocket(HOST_PORT);

	}
	/**
	 * main
	 * @param args
	 */

	public static void main(String[] args) {
		MainSchedulerSys host;
		try {
			host = new MainSchedulerSys();
			host.start();
		} catch (Exception e) {
			Output.print("Scheduler", "Main", Output.FATAL, e.getMessage());
		}
	}
	/**
	 * start method will send and receive requests from both client and server
	 */

	private void start() throws IOException {
		while (true) {

			// data holder
			byte[] inBytes;

			// receive from client
			inBytes = new byte[1024];
			DatagramPacket clientPacket = new DatagramPacket(inBytes, inBytes.length);

			// Listen for client request
			Output.print("Scheduler", "Main", Output.INFO, "Awaiting data...");
			hostSocketForFloor.receive(clientPacket);

			// Floor Details
			InetAddress clientIP = clientPacket.getAddress();
			int clientPort = clientPacket.getPort();

			// Get data from the received packet.
			byte[] receivedBytes = clientPacket.getData();

			// Print
			//System.out.println("Received from Floor: " + new String(receivedBytes));
			Output.print("Scheduler", "Main", Output.INFO, "Received from Floor: " + new String(receivedBytes));

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
	 * method reponsible for making sure that the request recieved is a floor request 
	 * @param request
	 * @param clientIP
	 * @param clientPort
	 * @throws IOException
	 */

	private void processRequestFromFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		if (request.startsWith("floor request elevator ")) {
			requestElevatorToFloor(request, clientIP, clientPort);
		}
	}

	/**
	 * method responsible for sending and receiving responses (UDP) between the elevator and the scheduler 
	 * @param request
	 * @param clientIP
	 * @param clientPort
	 * @throws IOException
	 */
	private void requestElevatorToFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		byte[] requestBytes = request.getBytes();

		// send request to Elevator
		DatagramPacket toElevatorPacket = new DatagramPacket(requestBytes, requestBytes.length, serverIp,
				ELEVATOR_PORT); // might have something to do with this in order to check the elevator ports.
		hostSocketForFloor.send(toElevatorPacket);
		
		//System.out.println("Forwarded to Elevator.");
		Output.print("Scheduler", "Main", Output.INFO,  "Forwarded to Elevator.");
		delay();

		// receive reply from server (elevator)
		byte[] inBytes = new byte[4];
		DatagramPacket fromServerPacket = new DatagramPacket(inBytes, inBytes.length);
		//System.out.println("Awaiting reply from Elevator...");
		Output.print("Scheduler", "Main", Output.INFO,  "Awaiting reply from Elevator...");
		hostSocketForFloor.receive(fromServerPacket);

		// Get data from the received packet.
		byte[] receivedBytes = fromServerPacket.getData();

		// Print
		//System.out.println("Received from Elevator: " + new String(receivedBytes));
		Output.print("Scheduler", "Main", Output.INFO, "Received from Elevator: " + new String(receivedBytes));


		// forward to client
		DatagramPacket toFloorPacket = new DatagramPacket(receivedBytes, receivedBytes.length, clientIP, clientPort);
		hostSocketForFloor.send(toFloorPacket);
		//System.out.println("Forwarded to Floor: " + new String(receivedBytes));
		Output.print("Scheduler", "Main", Output.INFO, "Forwarded to Floor: " + new String(receivedBytes));

		delay();

		System.out.println("=============================");

	}

}