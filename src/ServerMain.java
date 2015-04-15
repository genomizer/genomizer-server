import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

import process.classes.StartUpCleaner;

import authentication.InactiveUuidsRemover;

import command.CommandHandler;
import database.constants.ServerDependentValues;

import server.ServerSettings;
import server.Debug;
import server.Doorman;


public class ServerMain {

	public static int port = 7000;

	/**
	 * @param args
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws ParseException, FileNotFoundException {

		/* We firstly need to read and validate the settings.cfg file. */
		CommandLine com = loadSettingsFile(args);

		StartUpCleaner.removeOldTempDirectories("resources/");

		CommandHandler commandHandler = new CommandHandler();
		try {
			Doorman doorman = new Doorman(commandHandler, ServerSettings.genomizerPort);
			doorman.start();
			if (!com.hasOption("nri")) {
				(new Thread(new InactiveUuidsRemover())).start();
			}
			System.out.println("Doorman started on port " + ServerSettings.genomizerPort);
			if(Debug.isEnabled) {
				System.out.println("Debug is enabled.");
			}
			System.out.println("Database:");
			System.out.println("  username " + ServerSettings.databaseUsername);
			System.out.println("  password " + ServerSettings.databasePassword);
			System.out.println("  name     " + ServerSettings.databaseName);
			System.out.println("  host     " + ServerSettings.databaseHost);
		} catch (IOException e) {
			System.err.println("Error when starting server");
			e.printStackTrace();
			//log exception
			System.exit(1);
		}

	}

	/**
	 * This method attempts to read settings from file. It is hardcoded to
	 * read from 'settings.cfg', located in current working folder. It
	 * validates the final settings to be sane (e.g. not containing 'null').
	 *
	 * It parses the commandline arguments and constructs a CommandLine
	 * object containing the information.
	 * @param args The commandline arguments
	 * @return A CommandLine object containing the relevant commandline
	 * 		   options. The function also contains a side effect: It modifies
	 * 		   the static attributes of the ServerSettings class with the
	 * 		   information in the settings.cfg file.
	 * @throws FileNotFoundException if settings.cfg file could not be found
	 * 								 or opened.
	 * @throws ParseException if settings.cfg is formatted in an invalid way.
	 */
	private static CommandLine loadSettingsFile(String[] args)
			throws 	FileNotFoundException,
					ParseException {
		// Attempt to load settings.cfg file
		if (new File("settings.cfg").exists()) {
			System.out.println("Reading settings from settings.cfg");
			readSettingsFile("settings.cfg");
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
			ServerSettings.genomizerPort = Integer.parseInt(com.getOptionValue('p'));
		}
		// Debug flag
		if (com.hasOption("debug")) {
			Debug.isEnabled = true;
		}
		// Settingsfile flag
		if (com.hasOption('f')) {
			readSettingsFile(com.getOptionValue('f'));
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
							 "the file to read server settings from, is "+
							 "ignored when -d is used");
		comOptions.addOption("nri", "noremoveinactive", false,
							 "if this flag is set the server will not "+
                             "remove inactive users which are logged in on "+
                             "the server.");
		return comOptions;
	}

	public static void readSettingsFile(String path) throws FileNotFoundException {
		File dbFile = new File(path);
		if (dbFile.exists()) {
			Scanner scan = new Scanner(dbFile);
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				int index = line.indexOf("=");
				String key = line.substring(0, index).trim();
				String value = line.substring(index+1).trim();
				switch (key.toLowerCase()) {
				case "databaseuser":
					ServerSettings.databaseUsername = value;
					break;
				case "databasepassword":
					ServerSettings.databasePassword = value;
					break;
				case "databasehost":
					ServerSettings.databaseHost = value;
					break;
				case "databasename":
					ServerSettings.databaseName = value;
					break;
				case "publicaddress":
					ServerSettings.publicAddress = value;
					break;
				case "apacheport":
					ServerSettings.apachePort = Integer.parseInt(value);
					break;
				case "downloadurl":
					ServerSettings.downloadURL = value;
					break;
				case "uploadurl":
					ServerSettings.uploadURL = value;
					break;
				case "genomizerport":
					ServerSettings.genomizerPort = Integer.parseInt(value);
					break;
				case "passwordhash":
					ServerSettings.passwordHash = value;
					break;
				case "passwordsalt":
					ServerSettings.passwordSalt = value;
					break;
				default:
					System.err.println("Unrecognized setting: " + key);
					break;
				}
			}
			scan.close();
			ServerDependentValues.DownloadURL = ServerSettings.publicAddress + ":" + ServerSettings.apachePort + ServerSettings.downloadURL;
			ServerDependentValues.UploadURL = ServerSettings.publicAddress + ":" + ServerSettings.apachePort + ServerSettings.uploadURL;
		} else {
			System.err.println("Error, " + path + " does not exist, using default settings.");
		}
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
	private static void readDatabaseFile(String path) throws FileNotFoundException {
		File dbFile = new File(path);
		if (dbFile.exists()) {
			Scanner scan = new Scanner(dbFile);
			String username = scan.next();
			String password = scan.next();
			String database = scan.next();
			String host = scan.next();
			scan.close();

			ServerSettings.databaseUsername = username;
			ServerSettings.databasePassword = password;
			ServerSettings.databaseName = database;
			ServerSettings.databaseHost = host;
		}
	}

}
