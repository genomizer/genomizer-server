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

import server.ErrorLogger;
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

		// Read and parse settings file
		if (new File("settings.cfg").exists()) {
			Debug.log("Reading settings from settings.cfg");
			ServerSettings.readSettingsFile("settings.cfg");
		}

		// Set apache server settings
		CommandLineParser comline = new BasicParser();

		Options comOptions = new Options();

		comOptions.addOption("p", "port", true, "the listening port");
		comOptions.addOption("debug", false,
				"if this flag is used the server will write output " +
				"for every request and response aswell as other output.");
		comOptions.addOption("f", "file", true,
				"the file to read server settings from, " +
				"is ignored when -d is used");
		comOptions.addOption("nri", "noremoveinactive", false,
				"if this flag is set the server will not remove inactive " +
				"users which are logged in on the server.");

		CommandLine com = comline.parse(comOptions, args);

		if (com.hasOption('p')) {
			ServerSettings.genomizerPort = Integer.parseInt(com.getOptionValue('p'));
		}
		if (com.hasOption("debug")) {
			Debug.isEnabled = true;
		}
		if (com.hasOption('f')) {
			ServerSettings.readSettingsFile(com.getOptionValue('f'));
		}

		ServerSettings.validate();

		StartUpCleaner.removeOldTempDirectories("resources/");

		CommandHandler commandHandler = new CommandHandler();
		try {
			Doorman doorman = new Doorman(commandHandler, ServerSettings.genomizerPort);
			doorman.start();

			if (!com.hasOption("nri")) {
				(new Thread(new InactiveUuidsRemover())).start();
			}

			String msg = "Doorman started on port "
					+ ServerSettings.genomizerPort + "\n"
					+ "Database:\n"
					+ "  username " + ServerSettings.databaseUsername + "\n"
					+ "  password " + ServerSettings.databasePassword + "\n"
					+ "  name     " + ServerSettings.databaseName + "\n"
					+ "  host     " + ServerSettings.databaseHost;


			Debug.log(msg);
			//ErrorLogger.log("SYSTEM", msg);


		} catch (IOException e) {
			Debug.log("Error when starting server");
			//ErrorLogger.log("SYSTEM", "Could not start server!");
			System.exit(1);
		}

	}

	public static void readDatabaseFile(String path) throws FileNotFoundException {
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
