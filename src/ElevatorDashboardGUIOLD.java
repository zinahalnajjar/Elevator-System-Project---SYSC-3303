import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Graphical user interface showing elevator locations, actions, states, directions, destinations, console output and requests in real-time.
 *
 */

public class ElevatorDashboardGUIOLD extends JFrame implements ElevatorDashboard {
	
	// Creating instances of MainSystems
	private MainElevatorSys mainElevator;
	private MainFloorSys mainFloor;
	private MainSchedulerSys mainScheduler;
	private FloorMovementData movementData;
	private Elevator elevatorThread;

    // Collection storing console outputs made by elevator
    private final DefaultListModel<String> consoleOutput = new DefaultListModel<>();

    // Lists of panels for showing location of elevators
    private final ArrayList<JPanel> elevator1;
    private final ArrayList<JPanel> elevator2;
    private final ArrayList<JPanel> elevator3;

    // Text field displaying elevator destinations
    private final JTextField elevator1Dest;
    private final JTextField elevator2Dest;
    private final JTextField elevator3Dest;
    

    // Text field displaying elevator states
    private final JTextField elevator1State;
    private final JTextField elevator2State;
    private final JTextField elevator3State;
    
    // Text field displaying elevator error;
    private final JTextField elevator1Error;
    private final JTextField elevator2Error;
    private final JTextField elevator3Error;
    
 // Text field displaying elevator CurrentFloor;
    private final JTextField elevator1CurrentFloor;
    private final JTextField elevator2CurrentFloor;
    private final JTextField elevator3CurrentFloor;


    public ElevatorDashboardGUIOLD(){
        super("Elevator System Control Panel");

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());


        // Request lists section
        JScrollPane requestScrollPane = new JScrollPane();
        DefaultListModel<String> requests = new DefaultListModel<>();
        JList<String> requestList = new JList<>(requests);

        for(String s: readFile()){
            requests.addElement(s);
        }

        requestScrollPane.getViewport().add(requestList);
        requestScrollPane.setSize(new Dimension(700, 500));
        requestScrollPane.setMaximumSize(new Dimension(700, 500));

        // Console output section
        JScrollPane consoleScrollPane = new JScrollPane();
        JList<String> consoleList = new JList<>(consoleOutput);
        consoleList.setSize(new Dimension(700, 500));
        consoleList.setMaximumSize(new Dimension(700, 500));
        consoleScrollPane.getViewport().add(consoleList);
        consoleScrollPane.setSize(new Dimension(700, 500));
        consoleScrollPane.setMaximumSize(new Dimension(700, 500));

        /*
        // Legend section
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.add(new JLabel("YELLOW boxes show the location of an elevator"));
        legendPanel.add(new JLabel("RED boxes show an elevator error"));

*/
        // Creating left side of scheduler view (console, requests, legend)
        JPanel consoleOutputPanel = new JPanel();
        consoleOutputPanel.add(new JLabel("CONSOLE OUTPUT:"));
        consoleOutputPanel.setLayout(new BoxLayout(consoleOutputPanel, BoxLayout.Y_AXIS));
        consoleOutputPanel.add(consoleScrollPane);
        consoleOutputPanel.add(new JLabel(" "));
        consoleOutputPanel.add(new JLabel("INPUT FILE:"));
        consoleOutputPanel.add(requestScrollPane);
        consoleOutputPanel.add(new JLabel(" "));
        //consoleOutputPanel.add(legendPanel);

 
 
        // Creating right side of scheduler view (live tracker)
        JPanel liveTrackingPanelHeader = new JPanel();
        JPanel liveTrackingPanel = new JPanel();
        liveTrackingPanel.setLayout(new BoxLayout(liveTrackingPanel, BoxLayout.Y_AXIS));
        JPanel liveTrackingPanelGraphic = new JPanel();
        liveTrackingPanelGraphic.setLayout(new FlowLayout());
        liveTrackingPanelHeader.add(new JLabel("ELEVATOR LIVE TRACKER"));
        liveTrackingPanel.add(liveTrackingPanelHeader);
        liveTrackingPanel.add(liveTrackingPanelGraphic);
        
        

        // Individual Elevator tracking panels
        JPanel elevator1Panel = new JPanel();
        JPanel elevator2Panel = new JPanel();
        JPanel elevator3Panel = new JPanel();

        elevator1Panel.setLayout(new BoxLayout(elevator1Panel, BoxLayout.Y_AXIS));
        elevator2Panel.setLayout(new BoxLayout(elevator2Panel, BoxLayout.Y_AXIS));
        elevator3Panel.setLayout(new BoxLayout(elevator3Panel, BoxLayout.Y_AXIS));
      
        elevator1Panel.add(new JLabel("Elevator 1"));
        elevator2Panel.add(new JLabel("Elevator 2"));
        elevator3Panel.add(new JLabel("Elevator 3"));


        // Labels for elevator state/dest/ section
        JLabel destLabel1 = new JLabel("\nDESTINATION:");
        JLabel stateLabel1 = new JLabel("\nSTATE:");
        JLabel errorLabel1 = new JLabel("\nERROR TYPE:");
        JLabel CurrentFloor1Lable = new JLabel("\nCURRENT FLOOR:");

        JLabel destLabel2 = new JLabel("\nDESTINATION:");
        JLabel stateLabel2 = new JLabel("\nSTATE:");
        JLabel errorLabel2 = new JLabel("\nERROR TYPE:");
        JLabel CurrentFloor2Lable = new JLabel("\nCURRENT FLOOR:");

     

        JLabel destLabel3 = new JLabel("\nDESTINATION:");
        JLabel stateLabel3 = new JLabel("\nSTATE:");
        JLabel errorLabel3 = new JLabel("\nERROR TYPE:");
        JLabel CurrentFloor3Lable = new JLabel("\nCURRENT FLOOR:");

       

        // Blank spaces
        JLabel blankSpace1 = new JLabel(" ");
        JLabel blankSpace2 = new JLabel(" ");
        JLabel blankSpace3 = new JLabel(" ");
        JLabel blankSpace4 = new JLabel(" ");
        JLabel blankSpace5 = new JLabel(" ");
        JLabel blankSpace6 = new JLabel(" ");
        JLabel blankSpace7 = new JLabel(" ");
        JLabel blankSpace8 = new JLabel(" ");
        JLabel blankSpace9 = new JLabel(" ");
        JLabel blankSpace10 = new JLabel(" ");
        JLabel blankSpace11 = new JLabel(" ");
        JLabel blankSpace12 = new JLabel(" ");


        // Elevator destination field
        elevator1Dest = new JTextField();
        elevator2Dest = new JTextField();
        elevator3Dest = new JTextField();
        

        elevator1Dest.setText("None");
        elevator2Dest.setText("None");
        elevator3Dest.setText("None");
        

        elevator1Dest.setEditable(false);
        elevator2Dest.setEditable(false);
        elevator3Dest.setEditable(false);
        

        // Elevator state field
        elevator1State = new JTextField();
        elevator2State = new JTextField();
        elevator3State = new JTextField();
        

        elevator1State.setText("Doors Open");
        elevator2State.setText("Doors Open");
        elevator3State.setText("Doors Open");
        

        elevator1State.setEditable(false);
        elevator2State.setEditable(false);
        elevator3State.setEditable(false);
        
        //Elevator error field
        elevator1Error = new JTextField();
        elevator2Error = new JTextField();
        elevator3Error = new JTextField();
        
        elevator1Error.setText("No Error");
        elevator2Error.setText("No Error");
        elevator3Error.setText("No Error");
        
        elevator1Error.setEditable(false);
        elevator2Error.setEditable(false);
        elevator3Error.setEditable(false);
        
        // elevator current floor 
        elevator1CurrentFloor = new JTextField();
        elevator2CurrentFloor = new JTextField();
        elevator3CurrentFloor = new JTextField();
        
        elevator1CurrentFloor.setText("");
        elevator2CurrentFloor.setText("");
        elevator3CurrentFloor.setText("");
        
        elevator1CurrentFloor.setEditable(false);
        elevator2CurrentFloor.setEditable(false);
        elevator3CurrentFloor.setEditable(false);
     
        


        liveTrackingPanelGraphic.add(elevator1Panel);
        liveTrackingPanelGraphic.add(elevator2Panel);
        liveTrackingPanelGraphic.add(elevator3Panel);


       
        elevator1= new ArrayList<>();
        elevator2= new ArrayList<>();
        elevator3= new ArrayList<>();
       

        for(int i = 0; i < 22; i++){
            elevator1.add(new JPanel());
            elevator2.add(new JPanel());
            elevator3.add(new JPanel());
          

            elevator1.get(i).setBackground(Color.white);
            elevator2.get(i).setBackground(Color.white);
            elevator3.get(i).setBackground(Color.white);
           
        }

        for(int i = 22; i > 0; i--){
            elevator1.get(i-1).add(new JLabel(String.valueOf(i)));
            elevator2.get(i-1).add(new JLabel(String.valueOf(i)));
            elevator3.get(i-1).add(new JLabel(String.valueOf(i)));
          
        }

        for(int i = 21; i >= 0; i--){
            elevator1Panel.add(elevator1.get(i));
            elevator2Panel.add(elevator2.get(i));
            elevator3Panel.add(elevator3.get(i));
            
        }

        elevator1.get(0).setBackground(Color.yellow);
        elevator2.get(0).setBackground(Color.yellow);
        elevator3.get(0).setBackground(Color.yellow);
        
        


        // Adding elements
        elevator1Panel.add(blankSpace1);
        elevator1Panel.add(destLabel1);
        elevator1Panel.add(elevator1Dest);
        elevator1Panel.add(blankSpace2);
        elevator1Panel.add(stateLabel1);
        elevator1Panel.add(elevator1State);
        elevator1Panel.add(blankSpace9);
        elevator1Panel.add(errorLabel1);
        elevator1Panel.add(elevator1Error);
        elevator1Panel.add(blankSpace9);
        elevator1Panel.add(CurrentFloor1Lable);
        elevator1Panel.add(elevator1CurrentFloor);
    


        elevator2Panel.add(blankSpace3);
        elevator2Panel.add(destLabel2);
        elevator2Panel.add(elevator2Dest);
        elevator2Panel.add(blankSpace4);
        elevator2Panel.add(stateLabel2);
        elevator2Panel.add(elevator2State);
        elevator2Panel.add(blankSpace10);
        elevator2Panel.add(errorLabel2);
        elevator2Panel.add(elevator2Error);
        elevator2Panel.add(blankSpace10);
        elevator2Panel.add(CurrentFloor2Lable);
        elevator2Panel.add(elevator2CurrentFloor);
  
       

        elevator3Panel.add(blankSpace5);
        elevator3Panel.add(destLabel3);
        elevator3Panel.add(elevator3Dest);
        elevator3Panel.add(blankSpace6);
        elevator3Panel.add(stateLabel3);
        elevator3Panel.add(elevator3State);
        elevator3Panel.add(blankSpace11);
        elevator3Panel.add(errorLabel3);
        elevator3Panel.add(elevator3Error);
        elevator3Panel.add(blankSpace11);
        elevator3Panel.add(CurrentFloor3Lable);
        elevator3Panel.add(elevator3CurrentFloor);
        
        
    
      

        this.add(consoleOutputPanel);
        this.add(liveTrackingPanel);

        this.setSize(1000,1000);
        this.setVisible(true);
    }

    public void updateLiveTrackerLocation(int elevatorID, int floorNum){
        if(elevatorID == 1){
            for(int i = 0; i < 22; i++){
                elevator1.get(i).setBackground(Color.white);
            }
            elevator1.get(floorNum-1).setBackground(Color.yellow);
        }
        else if(elevatorID == 2){
            for(int i = 0; i < 22; i++){
                elevator2.get(i).setBackground(Color.white);
            }
            elevator2.get(floorNum-1).setBackground(Color.yellow);
        }
        else if(elevatorID == 3){
            for(int i = 0; i < 22; i++){
                elevator3.get(i).setBackground(Color.white);
            }
            elevator3.get(floorNum-1).setBackground(Color.yellow);
        }
        
    }


    /**
     * Add console output line to view
     * @param output String
     */
    public void addOutput(String output){
        consoleOutput.addElement(output);

    }

    /**
     * Read request txt file into arraylist of strings
     * @return ArrayList of request strings
     */
    public ArrayList<String> readFile() {
        ArrayList<String> dataLines = new ArrayList<>();
        try {
            File file = new File("./src/InputInformation.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                dataLines.add(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return dataLines;
    }

    /**
     * Update elevator destination in view
     * @param elevatorID elevator to update
     * @param destination new destination
     */
    public void updateDestination(int elevatorID, int destination){
    	destination = movementData.getDestinationFloor();
    	elevatorID = elevatorThread.getElevatorID();
    	
        if(elevatorID == 1){
            elevator1Dest.setText(String.valueOf(destination));
        }
        else if(elevatorID == 2){
            elevator2Dest.setText(String.valueOf(destination));
        }
        else if(elevatorID == 3){
            elevator3Dest.setText(String.valueOf(destination));
        }
        
    }

    /**
     * Update elevator state in view
     * @param elevatorID elevator to update
     * @param state new state
     */
    public void updateState(int elevatorID, String state){
    	elevatorID = elevatorThread.getElevatorID();
    	state = elevatorThread.getCurrentState();
    	
        if(elevatorID == 1){
            elevator1State.setText(state);
        }
        else if(elevatorID == 2){
            elevator2State.setText(state);
        }
        else if(elevatorID == 3){
            elevator3State.setText(state);
        }
        
    }
    
    
    /**
     * Update elevator errors
     * @param elevatorID elevator to update
     * @param error new error
     */
    public void updateError(int elevatorID, int error) {
    	elevatorID = elevatorThread.getElevatorID();
    	error = movementData.getError();
    	 if(elevatorID == 1){
             elevator1Error.setText(String.valueOf(error));
         }
         else if(elevatorID == 2){
        	 elevator1Error.setText(String.valueOf(error));
         }
         else if(elevatorID == 3){
        	 elevator1Error.setText(String.valueOf(error));
         }
    }

   

    /**
     * Signal error state by changing colour of elevator tracker and updating state field in view
     * @param elevatorID elevator to update
     * @param floorNum floor at which error occurred
     */
    public void addErrorState(int elevatorID, int floorNum){
    	elevatorID = elevatorThread.getElevatorID();
    	floorNum = elevatorThread.getCurrentFloor();
    	
        if(elevatorID == 1){
            elevator1.get(floorNum-1).setBackground(Color.PINK);
        }
        else if(elevatorID == 2){
            elevator2.get(floorNum-1).setBackground(Color.PINK);
        }
        else if(elevatorID == 3){
            elevator3.get(floorNum-1).setBackground(Color.PINK);
        }
        
    }

    /**
     * Clear error signal
     * @param elevatorID elevator to update
     * @param floorNum floor at which error occurred
     */
    public void removeErrorState(int elevatorID, int floorNum){
    	elevatorID = elevatorThread.getElevatorID();
    	floorNum = elevatorThread.getCurrentFloor();
    	
        if(elevatorID == 1){
            elevator1.get(floorNum-1).setBackground(Color.YELLOW);
        }
        else if(elevatorID == 2){
            elevator1.get(floorNum-1).setBackground(Color.YELLOW);
        }
        else if(elevatorID == 3){
            elevator1.get(floorNum-1).setBackground(Color.YELLOW);
        }
        else{
            elevator1.get(floorNum-1).setBackground(Color.YELLOW);
        }
    }
    
   

    public static void main(String[] args) {
        new ElevatorDashboardGUIOLD();
    }

	@Override
	public void updateState(int elevatorID, int position, String state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateView(int elevatorID, int floor, int error, String state) {
		// TODO Auto-generated method stub
		
	}

	

}
