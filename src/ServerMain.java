import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
// import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

import process.StartUpCleaner;

import authentication.InactiveUuidsRemover;

import command.CommandHandler;

import server.ServerSettings;
import server.Debug;
import server.Doorman;


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

		/* We firstly need to read and validate the settings file. */
		CommandLine com = loadSettingsFile(args);

		/* We delete possible fragments from previous runs */
		StartUpCleaner.removeOldTempDirectories("resources/");

		/* The database settings should be written upon startup*/
		printDatabaseInformation();

		/* We attempt to start the doorman */
		try {
			new Doorman(new CommandHandler(),
					ServerSettings.genomizerPort).start();
		} catch (IOException e) {
			System.err.println("Error when starting server");
			if (Debug.isEnabled) e.printStackTrace();
			System.exit(1);
		}

		/* By default we run a UID remover */
		if (!com.hasOption("nri")) {
			(new Thread(new InactiveUuidsRemover())).start();
		}

	}

	/**
	 * Print the database settings currently loaded into ServerSettings.
	 */
	private static void printDatabaseInformation() {
		System.out.println("Doorman started on port " +
						   ServerSettings.genomizerPort);
		System.out.println("Database:");
		System.out.println("  username " + ServerSettings.databaseUsername);
		System.out.println("  password " + ServerSettings.databasePassword);
		System.out.println("  name     " + ServerSettings.databaseName);
		System.out.println("  host     " + ServerSettings.databaseHost);
	}

	/**
	 * This method attempts to read the settings file. It is defined to read
	 * from the file in fileSettings. It validates the final settings to be
	 * sane (e.g. not containing 'null').
	 *
	 * It parses the commandline arguments and constructs a CommandLine
	 * object containing the information.
	 * @param args The commandline arguments
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
		// Attempt to load the settings file
		if (new File(settingsFile).exists()) {
			System.out.println("Reading settings from " + settingsFile);
			ServerSettings.readSettingsFile(settingsFile);
		}

		CommandLineParser comline = new BasicParser();

		// Construct an options archetype
		Options comOptions = createOptionsObject();

		// Convert our args into a commandline object using the defined rules.
		CommandLine com = comline.parse(comOptions, args);

		handleSuppliedCommandlineOptions(com);

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
			ServerSettings.readSettingsFile(com.getOptionValue('f'));
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

	/**
	 * This method reads a 'database' file. This method is never called
	 * anywhere.
	 *
	 * TODO: Remove this piece of dead code. (When safe)
	 * @param path Path of database file
	 * @throws FileNotFoundException If database file could not be
	 * 		   found/opened.
	 */
//	private static void readDatabaseFile(String path)
//			throws FileNotFoundException {
//		File dbFile = new File(path);
//		if (dbFile.exists()) {
//			Scanner scan = new Scanner(dbFile);
//			String username = scan.next();
//			String password = scan.next();
//			String database = scan.next();
//			String host = scan.next();
//			scan.close();
//
//			ServerSettings.databaseUsername = username;
//			ServerSettings.databasePassword = password;
//			ServerSettings.databaseName = database;
//			ServerSettings.databaseHost = host;
//		}
//	}

}
