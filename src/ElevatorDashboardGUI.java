/*
 * GUI dashboard class 
 * @Zinah, Mack
 */

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ElevatorDashboardGUI extends JFrame implements ElevatorDashboardView {

	// Individual Elevator tracking panels
	private ArrayList<ElevatorPanel> elevatorPanelList = new ArrayList<ElevatorPanel>();

	private int elevatorCount;

	/**
	 * Constructor.
	 * 
	 * @throws HeadlessException
	 */
	public ElevatorDashboardGUI(int elevatorCount) throws HeadlessException {
		super("Elevator LIVE Dashboard");
		this.elevatorCount = elevatorCount;

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());

		this.add(getLiveTrackingPanel());

		this.setSize(1000, 1000);
		this.setVisible(true);

	}
/*
 * setting up the live tracker panel 
 */
	private JPanel getLiveTrackingPanel() {
		JPanel liveTrackingPanel = new JPanel();

		// Creating right side of scheduler view (live tracker)
		JPanel liveTrackingPanelHeader = new JPanel();
		liveTrackingPanel.setLayout(new BoxLayout(liveTrackingPanel, BoxLayout.Y_AXIS));
		JPanel liveTrackingPanelGraphic = new JPanel();
		liveTrackingPanelGraphic.setLayout(new FlowLayout());
		liveTrackingPanelHeader.add(new JLabel("ELEVATOR LIVE TRACKER"));
		liveTrackingPanel.add(liveTrackingPanelHeader);
		liveTrackingPanel.add(liveTrackingPanelGraphic);

		// adding the elevator panel to the live Tracker 
		for (int i = 0; i < elevatorCount; i++) {
			ElevatorPanel elevatorPanel = new ElevatorPanel();
			liveTrackingPanelGraphic.add(elevatorPanel);
			elevatorPanelList.add(elevatorPanel);
		}

		return liveTrackingPanel;

	}

	/**
	 * main class 
	 * @param args
	 */
	public static void main(String[] args) {
		new ElevatorDashboardGUI(4);

	}
	/**
	 * update the view of the gui with the request information 
	 * @param elevatorID, floor, error, state
	 */

	@Override
	public void updateView(int elevatorID, int floor, int error, String state) {
		// elevatorID is 1 based
		ElevatorPanel elevatorPanel = elevatorPanelList.get(elevatorID - 1);
		elevatorPanel.updateView(elevatorID, floor, error, state);
	}

}
