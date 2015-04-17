package server;

import database.constants.ServerDependentValues;

import java.io.*;
import java.util.Scanner;

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
	public static String webUrlUpload = null;


	public static void writeSettings(String path){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, false));
			String dataInfo =
					"databaseUsername = " + databaseUsername + "\n"
					+ "databasePassword = " + databasePassword + "\n"
					+ "databaseHost = " + databaseHost + "\n"
					+ "databaseName = " + databaseName + "\n"
					+ "publicAddress = " + publicAddress + "\n"
					+ "apachePort = " + apachePort + "\n"
					+ "downloadURL = " + downloadURL + "\n"
					+ "uploadURL = " + uploadURL + "\n"
					+ "genomizerPort = " + genomizerPort + "\n"
					+ "passwordHash = " + passwordHash + "\n"
					+ "passwordSalt = " + passwordSalt + "\n"
					+ "webUrlUpload = " + webUrlUpload + "\n";

			out.write(dataInfo);
			out.close();

			Debug.log("Written the following settings:\n" + dataInfo);
			ErrorLogger.log("SYSTEM", "Written the following settings:\n" +
					dataInfo);

		} catch (IOException e) {
			String msg = "Could not write to file: " + path;
			Debug.log(msg);
			ErrorLogger.log("SYSTEM", msg);
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
		nullCheck(webUrlUpload, "webUrlUpload");
	}

	private static void nullCheck(int parameter, String name) {
		if (parameter == -1) {
			String msg = "Error! parameter " + name + " is not set. Check in " +
					"settings.cfg if it is set and spelled correctly, " +
					"capitalization does not matter.\nExiting";
			Debug.log(msg);
			ErrorLogger.log("SYSTEM", msg);
			System.exit(1);
		}
	}

	private static void nullCheck(String parameter, String name) {
		if (parameter == null) {
			String msg = "Error! parameter " + name + " is not set. Check in " +
					"settings.cfg if it is set and spelled correctly, " +
					"capitalization does not matter.\nExiting";
			Debug.log(msg);
			ErrorLogger.log("SYSTEM", msg);
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
				case "weburlupload":
					webUrlUpload = value;
					break;
				default:
					String msg = "Unrecognized setting: " + key;
					Debug.log(msg);
					ErrorLogger.log("SYSTEM", msg);
					break;
				}
			}
			scan.close();
			ServerDependentValues.DownloadURL = publicAddress + ":" +
					apachePort + downloadURL;
			ServerDependentValues.UploadURL = publicAddress + ":" +
					apachePort + uploadURL;

			String dataInfo =
					"\tdatabaseUsername = " + databaseUsername + "\n"
							+ "\tdatabasePassword = " + databasePassword + "\n"
							+ "\tdatabaseHost = " + databaseHost + "\n"
							+ "\tdatabaseName = " + databaseName + "\n"
							+ "\tpublicAddress = " + publicAddress + "\n"
							+ "\tapachePort = " + apachePort + "\n"
							+ "\tdownloadURL = " + downloadURL + "\n"
							+ "\tuploadURL = " + uploadURL + "\n"
							+ "\tgenomizerPort = " + genomizerPort + "\n"
							+ "\tpasswordHash = " + passwordHash + "\n"
							+ "\tpasswordSalt = " + passwordSalt + "\n"
							//+ "\tfileLocation = " + fileLocation + "\n"
							+ "\twebUrlUpload = " + webUrlUpload + "\n";

			Debug.log("Imported the following settings:\n" + dataInfo);
			ErrorLogger.log("SYSTEM", "Imported the following settings:\n" +
					dataInfo);

		} else {
			String msg = "Error, " + path + " does not exist, applying " +
					"default settings..";
			Debug.log(msg);
			ErrorLogger.log("SYSTEM", msg);
		}
	}
}
