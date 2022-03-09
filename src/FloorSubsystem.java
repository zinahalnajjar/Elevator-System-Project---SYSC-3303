import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * FloorSubsystem.java
 * 
 * floor subsystem thread that will send 
 * @author Zinah, Mack, Vilmos
 *
 */

/*
 * Floor subsystem sends the data read from the input file to the scheduler subsystem
 * receive commands to turn the floor button lamps off 
 */
public class FloorSubsystem implements Runnable  {
	
	private SchedulerSubsystem sc; 
	private ArrayList<FloorRequest> sentInfo;
	private FloorRequestData floorRequestData;

	/**
	 * base constructor
	 * @param sc, scheduler thread
	 * @param floorRequestData, information to be passed to floor
	 */
	public FloorSubsystem(SchedulerSubsystem sc, FloorRequestData floorRequestData) {
		this.sc = sc;
		this.floorRequestData = floorRequestData;
		this.sentInfo = new ArrayList<FloorRequest>();
		
	}
	
	/**
	 * takes a text file and converts it into requests for the floor 
	 * @return floorInfo, contains the requests as an ArrayList
	 */
	public static ArrayList<FloorRequest> getInfo(){ 
		ArrayList<FloorRequest> floorInfo = new ArrayList<>();	
		try {
			File fileReader = new File("./src/InputInformation.txt");
			Scanner scanner = new Scanner(fileReader);
			while(scanner.hasNext()) {
				String line = null;
				try {
					line = scanner.nextLine();
					String[] tokens = line.split(",");
					Integer originFloor = Integer.valueOf(tokens[1]);
					LocalTime time = LocalTime.parse(tokens[0]);
					Integer destinationFloor = Integer.valueOf(tokens[3]);
					Boolean goingUp = Boolean.valueOf(tokens[2]);
					FloorRequest fD = new FloorRequest(time, originFloor, destinationFloor, goingUp);
					floorInfo.add(fD);
				} catch(Exception e) {
					System.out.println("Error: INVALID File format. Ignoring line: " + line);
				}
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("error");
		} 
		
		return floorInfo;
		
	}

	/**
	 * sends the request to scheduler
	 * @param floorRequest, floorRequest to be sent to scheduler
	 */
	private void sendFloorRequest(FloorRequest floorRequest) {
		synchronized (floorRequestData) {
    		while(floorRequestData.getFloorRequest() != null) {
    			/**wait until request data is cleared
			*/
    			try {
    				/**System.out.println("Awaiting FloorRequest to be Completed....");
				*/
    				floorRequestData.wait();
    			} catch (InterruptedException e) {
    				System.err.println(e);
    			}
    		}
    		floorRequestData.setFloorRequest(floorRequest);
    		System.out.println("Floor up/down LAMP ON.");
    		floorRequestData.notifyAll();
		}/**synchronized
		*/
		
	}

	/**
	 * sets the elevators response
	 * @param elevatorResponse, elevators response
	 */
	public void setElevatorResponse(ResponseData elevatorResponse) {
		System.out.println("Floor Received ElevatorResponse: " + elevatorResponse.getResponse());
		System.out.println("Floor up/down LAMP OFF");
		
	}
	
	@Override
	public void run() {
		ArrayList<FloorRequest> lines = getInfo();
		try {
			DatagramSocket socket = new DatagramSocket(5000); //Creates a new socket. This will be used for sending and receiving packets
			InetAddress local = InetAddress.getLocalHost(); //Gets the local address of the computer
			for (FloorRequest request : lines) {
                byte[] dataArray = byteArray(request);
                DatagramPacket packetToSend = new DatagramPacket(dataArray, dataArray.length, local, 23); //Creates a packet from the dataArray, to be sent to the intermediate host
                DatagramPacket replyPacket = new DatagramPacket(new byte[20], 20); //Creates a packet to receive the acknowledgement in.
                printPacket(packetToSend, true); //Prints the contents of the packet to be sent
                socket.send(packetToSend); //Sends the packetToSend
                socket.receive(replyPacket); //Receive the ack from the intermediate host
                printPacket(replyPacket, false);
			}
			socket.close(); //Close the socket
		 } catch (IOException e) {
	            e.printStackTrace();
	     }
	}
	
	/**
     * This method prints the information in recievedPacket, formatted according to if it was sent or recieved
     *
     * @param receivedPacket takes in the packet to be printed
     * @param sending        Boolean value that indicates if the packet is to be sent, or was recieved
     */
    public static void printPacket(DatagramPacket receivedPacket, boolean sending) {
        if (!sending) { //If the packet was recieved
            System.out.println("Floor: Received the following packet (String): " + new String(receivedPacket.getData())); //Print data as string (Binary values will not appear correctly in the string,
            System.out.println("Recived the following packet (Bytes): "); //but this is what the assignment said to do)
            for (int z = 0; z < receivedPacket.getData().length - 1; z++) { //Prints the byte array one index at a time
                System.out.print(receivedPacket.getData()[z] + ", ");
            }
            System.out.println(receivedPacket.getData()[receivedPacket.getData().length - 1]);
            System.out.println("From:" + receivedPacket.getAddress() + " on port: " + receivedPacket.getPort()); //Prints the address and port the packet was recieved on
            System.out.println(""); //Adds a newline between packet sending and receiving
        } else { //The packet is being sent
            System.out.println("Floor: Sending the following packet (String): " + new String(receivedPacket.getData()));//Print data as string (Binary values will not appear correctly in the string,
            System.out.println("Sending the following packet (Bytes): "); //but this is what the assignment said to do)
            for (int z = 0; z < receivedPacket.getData().length - 1; z++) { //Prints the byte array one index at a time
                System.out.print(receivedPacket.getData()[z] + ", ");
            }
            System.out.println(receivedPacket.getData()[receivedPacket.getData().length - 1]);
            System.out.println("To:" + receivedPacket.getAddress() + " on port: " + receivedPacket.getPort()); //Prints the address and port the packet is being sent to
            System.out.println(""); //Adds a newline between packet sending and receiving
        }
    }
    
    /**
     * Convert floor request to an array of bytes
     * @param request Request being sent
     * @return request converted to array of bytes
     */
    public static byte[] byteArray(FloorRequest request) {
        byte[] arr = request.toString().getBytes();
        return arr;
    }
}
