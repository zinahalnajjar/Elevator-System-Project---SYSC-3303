import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Floor subsystem sends the data read from the input file to the schduler subsystem
 * receive commands to turn the floor button lamps off 
 * will have the above methods plus run
 */
public class FloorSubsystem implements Runnable  {
	
	private SchedulerSubsystem sc; 
	private ArrayList<InformationHandler> sentInfo;
	
	
	/*
	 * constructor 
	 */
	public FloorSubsystem(SchedulerSubsystem sc) {
		this.sc = sc;
		this.sentInfo = new ArrayList<InformationHandler>();
		
	}
	
	/*
	 * add the info in input file  
	 */
	public void addInfo() throws IOException {
		sentInfo.addAll(InputReader.getInfo("src/InputInformation.txt"));
		sc.setGotInfo(true); // lets scheduler know that it got info
	}
	
	/*
	 * returns the info that were read by the floor system 
	 */
	
	public ArrayList<InformationHandler> getInfo(){
		return sentInfo;
	}

	
	
	@Override
	public void run() {
		while(true) {
			try {
				addInfo();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}

