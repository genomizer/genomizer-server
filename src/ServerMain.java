import java.io.IOException;

import command.CommandHandler;

import server.DatabaseSettings;
import server.Doorman;


public class ServerMain {

	public static int port = 7000;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length < 2) {
			//System.err.println("TOO FEW ARGUMENTS.");
			System.out.println("Using default settings: port " + port);
			//System.exit(1);
		} else {

			System.out.println("ARG 0 = " + args[0]);
			System.out.println("ARG 1 = " + args[1]);

				String database = args[0];
				if (database.equals("test")) {
					DatabaseSettings.database = "c5dv151_vt14";
					DatabaseSettings.username = "c5dv151_vt14";
					DatabaseSettings.password = "shielohh";
					DatabaseSettings.host = "postgres";
				} else if (database.equals("global")) {
					//not tested
					DatabaseSettings.username = "pvt";
					DatabaseSettings.password = "pvt";
					DatabaseSettings.database = "genomizer";
					DatabaseSettings.host = "localhost:6000";
				}

			port = Integer.valueOf(args[1]);
		}
		CommandHandler commandHandler = new CommandHandler();
		try {
			Doorman doorman = new Doorman(commandHandler, port);
			doorman.start();
			System.out.println("Doorman started.");
		} catch (IOException e) {
			System.err.println("Error when starting server");
			e.printStackTrace();
			//log exception
			System.exit(1);
		}

	}

}
