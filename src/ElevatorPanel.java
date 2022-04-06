import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

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
	private final ArrayList<JPanel> floorPanelList;

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
		JLabel stateLabel1 = new JLabel("\nSTATE:");

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

		// Elevator error field
		elevatorError = new JTextField();
		elevatorError.setForeground(Color.GREEN);
		elevatorError.setText("No Error");

		elevatorError.setEditable(false);

		liveTrackingPanelGraphic.add(elevator1Panel);

		floorPanelList = new ArrayList<>();

		for (int i = 0; i < 22; i++) {
			floorPanelList.add(new JPanel());

			floorPanelList.get(i).setBackground(Color.white);
		}

		for (int i = 22; i > 0; i--) {
			floorPanelList.get(i - 1).add(new JLabel(String.valueOf(i)));
		}

		for (int i = 21; i >= 0; i--) {
			elevator1Panel.add(floorPanelList.get(i));
		}

		floorPanelList.get(0).setBackground(Color.yellow);

		// Adding elements
		elevator1Panel.add(new JLabel(" "));
		elevator1Panel.add(new JLabel(" "));
		elevator1Panel.add(new JLabel(" "));
		elevator1Panel.add(blankSpace2);
		elevator1Panel.add(stateLabel1);
		elevator1Panel.add(blankSpace9);
		elevator1Panel.add(elevatorError);
		elevator1Panel.add(blankSpace9);

		this.add(liveTrackingPanel);

		this.setSize(1000, 1000);
		this.setVisible(true);
	}

	public void updateLiveTrackerLocation(int elevatorID, int floorNum) {
		if (elevatorID == 1) {
			for (int i = 0; i < 22; i++) {
				floorPanelList.get(i).setBackground(Color.white);
			}
			floorPanelList.get(floorNum - 1).setBackground(Color.yellow);
		}
	}

	@Override
	public void updateView(int elevatorID, int floor, int error, String state) {
		if (error == 0) {
			floorPanelList.get(currentFloor).setBackground(Color.WHITE);
			if (floor > 0) {
				currentFloor = floor - 1;
			}
			floorPanelList.get(currentFloor).setBackground(Color.YELLOW);

			elevatorError.setForeground(Color.GREEN);
			elevatorError.setText("NORMAL");

		} else {
			elevatorError.setForeground(Color.RED);
			elevatorError.setText(state);
		}
	}

}
