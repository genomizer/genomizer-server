import java.io.IOException;

import command.CommandHandler;

import server.Doorman;


public class ServerMain {

	public static int port = 8080;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Invalid port: " + args[0]);
			}

		}

		CommandHandler commandHandler = new CommandHandler();
		try {
			Doorman doorman = new Doorman(commandHandler, port);
			doorman.start();
			System.out.println("Doorman started.");
		} catch (IOException e) {
			System.err.println("Error when starting server");
			//log exception
			System.exit(1);
		}

	}

}
