package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import database.constants.ServerDependentValues;

public class ServerSettings {

	public static String databaseUsername = null;
	public static String databasePassword = null;
	public static String databaseHost = null;
	public static String databaseName = null;
	public static String publicAddress = null;
	public static int apachePort = -1;
	public static String downloadURL = null;
	public static String uploadURL = null;
	public static int genomizerPort = -1;
	public static String passwordHash = null;
	public static String passwordSalt = null;

	public static void writeSettings(String path){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, false));
			out.write("databaseUsername = " + databaseUsername + "\n");
			out.write("databasePassword = " + databasePassword + "\n");
			out.write("databaseHost = " + databaseHost + "\n");
			out.write("databaseName = " + databaseName + "\n");
			out.write("publicAddress = " + publicAddress + "\n");
			out.write("apachePort = " + apachePort + "\n");
			out.write("downloadURL = " + downloadURL + "\n");
			out.write("uploadURL = " + uploadURL + "\n");
			out.write("genomizerPort = " + genomizerPort + "\n");
			out.write("passwordHash = " + passwordHash + "\n");
			out.write("passwordSalt = " + passwordSalt + "\n");
			out.close();
		} catch (IOException e) {
			ErrorLogger.log("SYSTEM", "Could not write to file: " + path);
		}
	}

	public static void validate() {
		nullCheck(databaseUsername, "databaseUsername");
		nullCheck(databasePassword, "databasePassword");
		nullCheck(databaseHost, "databaseHost");
		nullCheck(databaseName, "databaseName");
		nullCheck(publicAddress, "publicAddress");
		nullCheck(apachePort, "apachePort");
		nullCheck(downloadURL, "downloadURL");
		nullCheck(uploadURL, "uploadURL");
		nullCheck(genomizerPort, "genomizerPort");
		nullCheck(passwordHash, "passwordHash");
		nullCheck(passwordSalt, "passwordSalt");
	}

	private static void nullCheck(int parameter, String name) {
		if (parameter == -1) {
			ErrorLogger.log("SYSTEM",
					"Error! parameter " + name + " is not set. " +
					"Check in settings.cfg if it is set and spelled " +
					"correctly, capitalization does not matter.");
			ErrorLogger.log("SYSTEM", "Exiting");
			System.exit(1);
		}
	}

	private static void nullCheck(String parameter, String name) {
		if (parameter == null) {
			ErrorLogger.log("SYSTEM",
					"Error! parameter " + name + " is not set. " +
					"Check in settings.cfg if it is set and spelled " +
					"correctly, capitalization does not matter.");
			ErrorLogger.log("SYSTEM", "Exiting");
			System.exit(1);
		}
	}

	public static void readSettingsFile(String path) throws FileNotFoundException {
		File settingsFile = new File(path);

		if (settingsFile.exists()) {
			Scanner scan = new Scanner(settingsFile);

			while (scan.hasNextLine()) {

				String line = scan.nextLine();
				int index = line.indexOf("=");
				String key = line.substring(0, index).trim();
				String value = line.substring(index+1).trim();

				switch (key.toLowerCase()) {
				case "databaseuser":
					databaseUsername = value;
					break;
				case "databasepassword":
					databasePassword = value;
					break;
				case "databasehost":
					databaseHost = value;
					break;
				case "databasename":
					databaseName = value;
					break;
				case "publicaddress":
					publicAddress = value;
					break;
				case "apacheport":
					apachePort = Integer.parseInt(value);
					break;
				case "downloadurl":
					downloadURL = value;
					break;
				case "uploadurl":
					uploadURL = value;
					break;
				case "genomizerport":
					genomizerPort = Integer.parseInt(value);
					break;
				case "passwordhash":
					passwordHash = value;
					break;
				case "passwordsalt":
					passwordSalt = value;
					break;
				default:
					ErrorLogger.log("SYSTEM", "Unrecognized setting: " + key);
					break;
				}
			}
			scan.close();

			ServerDependentValues.DownloadURL =
					publicAddress + ":" + apachePort + downloadURL;
			ServerDependentValues.UploadURL =
					publicAddress + ":" + apachePort + uploadURL;

		} else {
			ErrorLogger.log("SYSTEM",
					"Error, \"" + path + "\" does not exist, using default settings.");
		}
	}

}
