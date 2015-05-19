import authentication.InactiveUuidsRemover;
import org.apache.commons.cli.*;
import process.StartUpCleaner;
import server.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// import java.util.Scanner;


public class ServerMain {

	public static int port = 7000;
	public static String settingsFile = "settings.cfg";

	/**
	 * @param args
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws ParseException,
												  FileNotFoundException {

		/* First we need to read and validate the settings file. */
		CommandLine com = loadSettingsFile(args);

		/* We delete possible fragments from previous runs. */
		StartUpCleaner.removeOldTempDirectories("/tmp/");

		/* The database settings should be written upon startup. */
		printDatabaseInformation();

		/* Create a process pool */
		ProcessPool processPool = new ProcessPool(
				ServerSettings.nrOfProcessThreads);

		/* We attempt to start the doorman. */
		try {
			new Doorman(processPool,
					ServerSettings.genomizerPort).start();
		} catch (IOException e) {
			System.err.println("Error when starting server");
			Debug.log(e.getMessage());
			System.exit(1);
		}

		/* By default we run a UID remover. */
		if (!com.hasOption("nri")) {
			(new Thread(new InactiveUuidsRemover())).start();
		}

	}

	/**
	 * Print the database settings currently loaded into ServerSettings.
	 */
	private static void printDatabaseInformation() {
		String info = "Database information:" + "\n"
				+ "  username " + ServerSettings.databaseUsername + "\n"
				+ "  password " + ServerSettings.databasePassword + "\n"
				+ "  name     " + ServerSettings.databaseName + "\n"
				+ "  host     " + ServerSettings.databaseHost + "\n";
		System.out.print(info);
		ErrorLogger.log("SYSTEM", info);
	}


	/**
	 * This method attempts to read the settings file. It is defined to read
	 * from the file in fileSettings. It validates the final settings to be
	 * sane (e.g. not containing 'null').
	 *
	 * It parses the command-line arguments and constructs a CommandLine
	 * object containing the information.
	 * @param args Command-line arguments
	 * @return A CommandLine object containing the relevant commandline
	 * 		   options. The function also contains a side effect: It modifies
	 * 		   the static attributes of the ServerSettings class with the
	 * 		   information in the settings file.
	 * @throws FileNotFoundException if the settings file could not be found
	 * 								 or opened.
	 * @throws ParseException if the settings is formatted in an invalid way.
	 */
	private static CommandLine loadSettingsFile(String[] args)
			throws 	FileNotFoundException,
					ParseException {

		CommandLineParser comline = new BasicParser();

		// Construct an options archetype
		Options comOptions = createOptionsObject();

		// Convert our args into a commandline object using the defined rules.
		CommandLine com = comline.parse(comOptions, args);

		handleSuppliedCommandlineOptions(com);

		// Attempt to load the settings file
		if (new File(settingsFile).exists()) {
			System.out.println("Reading settings from " + settingsFile);
			ServerSettings.readSettingsFile(settingsFile);
		}

		ServerSettings.validate();

		return com;
	}

	/**
	 * Method to set correct flags for the given commandline arguments.
	 * @param com
	 * @throws FileNotFoundException
	 */
	private static void handleSuppliedCommandlineOptions(CommandLine com)
			throws FileNotFoundException {
		// Port flag
		if (com.hasOption('p')) {
			ServerSettings.genomizerPort =
					Integer.parseInt(com.getOptionValue('p'));
		}
		// Debug flag
		if (com.hasOption("debug")) {
			Debug.isEnabled = true;
			System.out.println("Debug is enabled.");
		}
		// Settings file flag
		if (com.hasOption('f')) {
			settingsFile = com.getOptionValue('f');
		}
	}

	/**
	 * Constructs the rules for parsing commandline parameters.
	 * @return The options object representing rules for parsing.
	 */
	private static Options createOptionsObject() {
		Options comOptions = new Options();
		comOptions.addOption("p", "port", true, "the listening port");
		comOptions.addOption("debug", false,
							 "if this flag is used the server will write " +
                			 "output for every request and response as well " +
                			 "as other output.");
		comOptions.addOption("f", "file", true,
							 "the file to read server settings from, is " +
							 "ignored when -d is used");
		comOptions.addOption("nri", "noremoveinactive", false,
							 "if this flag is set the server will not " +
 							 "remove inactive users which are logged in on " +
 							 "the server.");
		return comOptions;
	}

}
