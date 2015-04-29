package server;
/**
 * This class is used when writing debug information.
 * @author c11jmm
 *
 */
public class Debug {

	public static boolean isEnabled = true;

	/**
	 * Writes the message on standard output is debug option is enabled.
	 * @param message the information which is to be writte to standard out.
	 */
	public static void log(String message) {
		if(isEnabled) {
			System.out.println(message);
		}
	}
}
