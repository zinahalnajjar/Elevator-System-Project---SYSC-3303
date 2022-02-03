import java.time.LocalTime;

/*
 * this interface is used by the floor data 
 */
public interface InformationHandler {
	 int getCurrentFloor();
	 int getDestinationFloor();
	 LocalTime getLocalTime();
	 boolean isGoingUp();
	 
	 String toString();

}
