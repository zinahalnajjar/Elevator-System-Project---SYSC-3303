/*
 * host will work like a channel between the client and the server 
 * from client's perspective it will act like a server 
 * from server perspective it will act like a client 
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

	// Receive Client request AND reply back.
	private DatagramSocket hostSocketForClient;

	//IP and port address 
	private InetAddress serverIp;
	private int ELEVATOR_PORT = 69;

	private int HOST_PORT = 23;

	public MainSchedulerSys() throws UnknownHostException, SocketException {
		// Host details
		serverIp = InetAddress.getByName("localhost");

		// Construct a datagram socket and bind it to port HOST_PORT
		// on the local host machine.
		hostSocketForClient = new DatagramSocket(HOST_PORT);

	}

	public static void main(String[] args) {
		MainSchedulerSys host;
		try {
			host = new MainSchedulerSys();
			host.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
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
			System.out.println("Awaiting data...");
			hostSocketForClient.receive(clientPacket);

			// Client Details
			InetAddress clientIP = clientPacket.getAddress();
			int clientPort = clientPacket.getPort();

			// Get data from the received packet.
			byte[] receivedBytes = clientPacket.getData();

			// Print
			System.out.println("Received from Client - Bytes: " + Arrays.toString(receivedBytes));
			System.out.println("Received from Client - String: " + new String(receivedBytes));
			
			String request = new String(receivedBytes);
			
			if(request.startsWith("floor ")) {
				processRequestFromFloor(request, clientIP, clientPort);
			}
			else if(request.startsWith("elevator ")) {
				//pending
			}

			//------OLD CODE
	
			// send request to server 
			DatagramPacket toServerPacket = new DatagramPacket(receivedBytes, receivedBytes.length, serverIp,ELEVATOR_PORT);
			hostSocketForClient.send(toServerPacket);
			System.out.println("Forwarded to server.");
			delay();

			// receive reply from server 
			inBytes = new byte[4];
			DatagramPacket fromServerPacket = new DatagramPacket(inBytes, inBytes.length);
			System.out.println("Awaiting reply from server...");
			hostSocketForClient.receive(fromServerPacket);

			// Get data from the received packet.
			receivedBytes = fromServerPacket.getData();

			// Print
			System.out.println("Received from Server - Bytes: " + Arrays.toString(receivedBytes));
			System.out.println("Received from Server - String: " + new String(receivedBytes));

			// forward to client 
			DatagramPacket toClientPacket = new DatagramPacket(receivedBytes, receivedBytes.length, clientIP,
					clientPort);
			hostSocketForClient.send(toClientPacket);
			System.out.println("Forwarded to Client.");
			delay();

			System.out.println("=============================");

		} // while
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
	

	private void processRequestFromFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		if(request.startsWith("floor request elevator ")) {
			requestElevatorToFloor(request, clientIP, clientPort);
		}
	}
	
	private void requestElevatorToFloor(String request, InetAddress clientIP, int clientPort) throws IOException {
		byte[] requestBytes = request.getBytes();

		// send request to Elevator 
		DatagramPacket toElevatorPacket = new DatagramPacket(requestBytes, requestBytes.length, serverIp,
				ELEVATOR_PORT);
		hostSocketForClient.send(toElevatorPacket);
		System.out.println("Forwarded to Elevator.");
		delay();

		// receive reply from server 
		byte[] inBytes = new byte[4];
		DatagramPacket fromServerPacket = new DatagramPacket(inBytes, inBytes.length);
		System.out.println("Awaiting reply from server...");
		hostSocketForClient.receive(fromServerPacket);

		// Get data from the received packet.
		byte[] receivedBytes = fromServerPacket.getData();

		// Print
		System.out.println("Received from Server - Bytes: " + Arrays.toString(receivedBytes));
		System.out.println("Received from Server - String: " + new String(receivedBytes));

		// forward to client 
		DatagramPacket toClientPacket = new DatagramPacket(receivedBytes, receivedBytes.length, clientIP,
				clientPort);
		hostSocketForClient.send(toClientPacket);
		System.out.println("Forwarded to Client: " + new String(receivedBytes));
		delay();

		System.out.println("=============================");

		
	}

}