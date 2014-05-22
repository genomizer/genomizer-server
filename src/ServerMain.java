import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.ParseException;

import command.CommandHandler;

import server.DatabaseSettings;
import server.Doorman;


public class ServerMain {

	public static int port = 7001;

	/**
	 * @param args
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws ParseException, FileNotFoundException {

		CommandLineParser comline = new BasicParser();

		Options comOptions = new Options();
		comOptions.addOption("p", "port", true, "the listening port");
		comOptions.addOption("d", true, "chooses which database to use, valid alternatives are global and test");
		comOptions.addOption("f", "file", true, "the file to read database settings from, is ignored when -d is used");
//		comOptions.addOption("p", "port", true, "the listening port");
		CommandLine com = comline.parse(comOptions, args);
		if (com.hasOption('p')) {
			port = Integer.parseInt(com.getOptionValue('p'));
		}
		if (com.hasOption('d')) {
			String database = com.getOptionValue('d');
			if (database.equals("test")) {
				DatabaseSettings.username = "c5dv151_vt14";
				DatabaseSettings.password = "shielohh";
				DatabaseSettings.database = "c5dv151_vt14";
				DatabaseSettings.host = "postgres";
			} else if (database.equals("global")) {
				//not tested
				DatabaseSettings.username = "postgres";
				DatabaseSettings.password = "pvt";
				DatabaseSettings.database = "genomizer";
				DatabaseSettings.host = "localhost:6000";
			}
		} else if (com.hasOption('f')) {
			readDatabaseFile(com.getOptionValue('f'));
		} else {
			readDatabaseFile("dbconfig");
		}

		CommandHandler commandHandler = new CommandHandler();
		try {
			Doorman doorman = new Doorman(commandHandler, port);
			doorman.start();
			System.out.println("Doorman started on port " + port);
			System.out.println("Database:");
			System.out.println("  username " + DatabaseSettings.username);
			System.out.println("  password " + DatabaseSettings.password);
			System.out.println("  database " + DatabaseSettings.database);
			System.out.println("  host     " + DatabaseSettings.host);
		} catch (IOException e) {
			System.err.println("Error when starting server");
			e.printStackTrace();
			//log exception
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

			DatabaseSettings.username = username;
			DatabaseSettings.password = password;
			DatabaseSettings.database = database;
			DatabaseSettings.host = host;
		}
	}

}
