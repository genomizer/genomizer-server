package server;

public class Debug {

	public static boolean isEnabled = false;

	public static void log(String message) {
		if(isEnabled) {
			System.out.println(message);
		}
	}
}
