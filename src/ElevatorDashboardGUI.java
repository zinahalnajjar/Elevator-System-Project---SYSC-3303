import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ElevatorDashboardGUI extends JFrame implements ElevatorDashboard {

	// Individual Elevator tracking panels
	ArrayList<ElevatorPanel> panelList = new ArrayList<ElevatorPanel>();

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

		for (int i = 0; i < elevatorCount; i++) {
			liveTrackingPanelGraphic.add(new ElevatorPanel());
		}

		return liveTrackingPanel;

	}

	@Override
	public void updateDestination(int elevatorID, int destination) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateState(int elevatorID, String state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateState(int elevatorID, int position, String state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateError(int elevatorID, int error) {
		// TODO Auto-generated method stub

	}

	// MAIN
	public static void main(String[] args) {
		new ElevatorDashboardGUI(4);

	}

}
