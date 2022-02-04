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
	private ArrayList<FloorData> sentInfo;

	/*
	 * constructor 
	 */
	public FloorSubsystem(SchedulerSubsystem sc) {
		this.sc = sc;
		this.sentInfo = new ArrayList<FloorData>();
		
	}
	
	/*
	 * add the info in input file  
	 */
	public void addInfo() throws IOException {
		sentInfo.addAll(InputReader.getInfo("src/InputInformation.txt"));
		//sc.toggleFloorRequestFlag(true);
	}
	
	/*
	 * returns the info that were read by the floor system 
	 */
	
	public ArrayList<FloorData> getInfo(){
		return sentInfo;
	}

	
	
	@Override
	public void run() {
		sc.putRequestFromFloor(sentInfo);
		
	}

}

