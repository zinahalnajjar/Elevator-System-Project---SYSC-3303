/*
 * class used to chnage the print statemtns in the requested output format [date][module][submodule][type]

 * 
 * @author Zinah 
 */
import java.util.Date;

public class Output {

	public static final String INFO = "INFO";
	public static final String WARNING = "WARNING";
	public static final String CRITICAL = "CRITICAL";
	public static final String FATAL = "FATAL";

	public static void print(String module, String submodule, String type, String message) {
		String dateTime = new Date().toString();
		System.out.println("[" + dateTime + "] [" + module + "] [" + submodule + "] [" + type + "] " + message);
	}
}
