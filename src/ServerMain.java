import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

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

		if (new File("settings.cfg").exists()) {
			System.out.println("Settings file exists, reading it...");
			readSettingsFile("settings.cfg");
			System.out.println("Done");
		}

		CommandLineParser comline = new BasicParser();

		Options comOptions = new Options();
		comOptions.addOption("p", "port", true, "the listening port");
		comOptions.addOption("d", true, "chooses which database to use, valid alternatives are global and test");
		comOptions.addOption("debug", false, "if this flag is used the server will write output for every request and response aswell as other output.");
		comOptions.addOption("f", "file", true, "the file to read database settings from, is ignored when -d is used");
		comOptions.addOption("nri", "noremoveinactive", false, "if this flag is set the server will not remove inactive users which are logged in on the server.");
//		comOptions.addOption("p", "port", true, "the listening port");
		CommandLine com = comline.parse(comOptions, args);
		if (com.hasOption('p')) {
			ServerSettings.genomizerPort = Integer.parseInt(com.getOptionValue('p'));
		}
		if (com.hasOption("debug")) {
			Debug.isEnabled = true;
		}
		if (com.hasOption('d')) {
			String database = com.getOptionValue('d');
			if (database.equals("test")) {
				ServerSettings.databaseUsername = "c5dv151_vt14";
				ServerSettings.databasePassword = "shielohh";
				ServerSettings.databaseName = "c5dv151_vt14";
				ServerSettings.databaseHost = "postgres";
			} else if (database.equals("global")) {
				//not tested
				ServerSettings.databaseUsername = "postgres";
				ServerSettings.databasePassword = "pvt";
				ServerSettings.databaseName = "genomizer";
				ServerSettings.databaseHost = "localhost:6000";
			}
		} else if (com.hasOption('f')) {
			readDatabaseFile(com.getOptionValue('f'));
		} else {
			readDatabaseFile("dbconfig");
		}

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
			System.out.println("  database " + ServerSettings.databaseName);
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
