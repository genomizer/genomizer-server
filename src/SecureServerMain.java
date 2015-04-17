import authentication.InactiveUuidsRemover;
import command.CommandHandler;
import database.constants.ServerDependentValues;
import org.apache.commons.cli.*;
import process.classes.StartUpCleaner;
import server.Debug;
import server.SecureDoorman;
import server.ServerSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class SecureServerMain {

	public static int port = 7000;

	/**
	 * @param args Arguments
	 * @throws org.apache.commons.cli.ParseException
	 * @throws java.io.FileNotFoundException
	 */
	public static void main(String[] args) throws ParseException, FileNotFoundException {

		if (new File("settings.cfg").exists()) {
			System.out.println("Reading settings from settings.cfg");
			readSettingsFile("settings.cfg");
		}

		CommandLineParser comline = new BasicParser();

		Options comOptions = new Options();
		comOptions.addOption("p", "port", true, "the listening port");
		comOptions.addOption("debug", false, "if this flag is used the server will write output for every request and response aswell as other output.");
		comOptions.addOption("f", "file", true, "the file to read server settings from, is ignored when -d is used");
		comOptions.addOption("nri", "noremoveinactive", false, "if this flag is set the server will not remove inactive users which are logged in on the server.");
		CommandLine com = comline.parse(comOptions, args);
		if (com.hasOption('p')) {
			ServerSettings.genomizerPort = Integer.parseInt(com.getOptionValue('p'));
		}
		if (com.hasOption("debug")) {
			Debug.isEnabled = true;
		}
		if (com.hasOption('f')) {
			readSettingsFile(com.getOptionValue('f'));
		}

		ServerSettings.validate();

		StartUpCleaner.removeOldTempDirectories("resources/");

		CommandHandler commandHandler = new CommandHandler();
		try {
			SecureDoorman secureDoorman = new SecureDoorman(commandHandler, ServerSettings.genomizerPort);
			secureDoorman.start();
			if (!com.hasOption("nri")) {
				(new Thread(new InactiveUuidsRemover())).start();
			}
			System.out.println("SecureDoorman started on port " + ServerSettings.genomizerPort);
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
