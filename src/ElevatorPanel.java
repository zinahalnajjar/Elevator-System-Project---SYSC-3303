import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Graphical user interface showing elevator locations, actions, states,
 * directions, destinations, console output and requests in real-time.
 *
 */

public class ElevatorPanel extends JPanel implements ElevatorDashboardView {

	// Lists of panels for showing location of elevators
	private final ArrayList<JPanel> elevator;

	// Text field displaying elevator destinations
	private final JTextField elevatorDest;

	// Text field displaying elevator states
	private final JTextField elevatorState;

	// Text field displaying elevator error;
	private final JTextField elevatorError;

	private int currentFloor;

	/**
	 * CONSTRUCTOR.
	 */
	public ElevatorPanel() {

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

		elevator1Panel.setLayout(new BoxLayout(elevator1Panel, BoxLayout.Y_AXIS));

		elevator1Panel.add(new JLabel("Elevator 1"));

		// Labels for elevator state/dest/ section
		JLabel destLabel1 = new JLabel("\nDESTINATION:");
		JLabel stateLabel1 = new JLabel("\nSTATE:");
		JLabel errorLabel1 = new JLabel("\nERROR TYPE:");

		JLabel destLabel2 = new JLabel("\nDESTINATION:");
		JLabel stateLabel2 = new JLabel("\nSTATE:");
		JLabel errorLabel2 = new JLabel("\nERROR TYPE:");

		JLabel destLabel3 = new JLabel("\nDESTINATION:");
		JLabel stateLabel3 = new JLabel("\nSTATE:");
		JLabel errorLabel3 = new JLabel("\nERROR TYPE:");

		// Blank spaces
		// ...PENDING REMOVE THESE variables and directly use: new JLabel(" ")

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
		elevatorDest = new JTextField();

		elevatorDest.setText("None");

		elevatorDest.setEditable(false);

		// Elevator state field
		elevatorState = new JTextField();

		elevatorState.setText("Doors Open");

		elevatorState.setEditable(false);

		// Elevator error field
		elevatorError = new JTextField();
		elevatorError.setForeground(Color.GREEN);
		elevatorError.setText("No Error");

		elevatorError.setEditable(false);

		liveTrackingPanelGraphic.add(elevator1Panel);

		elevator = new ArrayList<>();

		for (int i = 0; i < 22; i++) {
			elevator.add(new JPanel());

			elevator.get(i).setBackground(Color.white);
		}

		for (int i = 22; i > 0; i--) {
			elevator.get(i - 1).add(new JLabel(String.valueOf(i)));
		}

		for (int i = 21; i >= 0; i--) {
			elevator1Panel.add(elevator.get(i));
		}

		elevator.get(0).setBackground(Color.yellow);

		// Adding elements
		elevator1Panel.add(new JLabel(" "));
		elevator1Panel.add(destLabel1);
		elevator1Panel.add(elevatorDest);
		elevator1Panel.add(blankSpace2);
		elevator1Panel.add(stateLabel1);
		elevator1Panel.add(elevatorState);
		elevator1Panel.add(blankSpace9);
		elevator1Panel.add(errorLabel1);
		elevator1Panel.add(elevatorError);
		elevator1Panel.add(blankSpace9);

		this.add(liveTrackingPanel);

		this.setSize(1000, 1000);
		this.setVisible(true);
	}

	public void updateLiveTrackerLocation(int elevatorID, int floorNum) {
		if (elevatorID == 1) {
			for (int i = 0; i < 22; i++) {
				elevator.get(i).setBackground(Color.white);
			}
			elevator.get(floorNum - 1).setBackground(Color.yellow);
		}
	}

	/**
	 * Add console output line to view
	 * 
	 * @param output String
	 */
	public void addOutput(String output) {

	}

	/**
	 * Read request txt file into arraylist of strings
	 * 
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
	 * 
	 * @param elevatorID  elevator to update
	 * @param destination new destination
	 */
	public void updateDestination(int elevatorID, int destination) {

		if (elevatorID == 1) {
			elevatorDest.setText(String.valueOf(destination));
		}

	}

	/**
	 * Update elevator state in view
	 * 
	 * @param elevatorID elevator to update
	 * @param state      new state
	 */
	public void updateState(int elevatorID, String state) {

		if (elevatorID == 1) {
			elevatorState.setText(state);
		}

	}

	/**
	 * Update elevator errors
	 * 
	 * @param elevatorID elevator to update
	 * @param error      new error
	 */
	public void updateError(int elevatorID, int error) {
		if (elevatorID == 1) {
			elevatorError.setText(String.valueOf(error));
		}
	}

	/**
	 * Signal error state by changing colour of elevator tracker and updating state
	 * field in view
	 * 
	 * @param elevatorID elevator to update
	 * @param floorNum   floor at which error occurred
	 */
	public void addErrorState(int elevatorID, int floorNum) {

		if (elevatorID == 1) {
			elevator.get(floorNum - 1).setBackground(Color.PINK);
		}

	}

	/**
	 * Clear error signal
	 * 
	 * @param elevatorID elevator to update
	 * @param floorNum   floor at which error occurred
	 */
	public void removeErrorState(int elevatorID, int floorNum) {
		if (elevatorID == 1) {
			elevator.get(floorNum - 1).setBackground(Color.YELLOW);
		}
	}

	@Override
	public void updateView(int elevatorID, int floor, int error, String state) {
		if (error == 0) {
			elevator.get(currentFloor).setBackground(Color.WHITE);
			currentFloor = floor - 1;
			elevator.get(currentFloor).setBackground(Color.YELLOW);

			elevatorError.setForeground(Color.GREEN);
			elevatorError.setText("No Error");

		} else {
			elevatorError.setForeground(Color.RED);
			elevatorError.setText(state);
		}
	}

}
