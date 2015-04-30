
package server;
import database.constants.ServerDependentValues;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class ServerSettings {

	public static String databaseUsername = null;
	public static String databasePassword = null;
	public static String databaseHost = null;
	public static String databaseName = null;
	public static URI    wwwTunnelURL = null;
	public static int genomizerPort = -1;
	public static String fileLocation = "/var/www/data/";
	public static String bowtieLocation = "bowtie";
	public static int nrOfProcessThreads = 5;

	private static String downloadURL = "/download?path=";
	private static String uploadURL = "/upload?path=";

	public static void writeSettings(String path){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path, false));
			String dataInfo =
					"databaseUsername = " + databaseUsername + "\n"
					+ "databasePassword = " + databasePassword + "\n"
					+ "databaseHost = " + databaseHost + "\n"
					+ "databaseName = " + databaseName + "\n"
					+ "wwwTunnelURL = " + wwwTunnelURL + "\n"
					+ "genomizerPort = " + genomizerPort + "\n"
					+ "fileLocation = " + fileLocation + "\n"
					+ "nrOfProcessThreads = " + nrOfProcessThreads + "\n"
					+ "bowtieLocation = " + bowtieLocation + "\n";

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
		nullCheck(wwwTunnelURL, "wwwTunnelURL");
		nullCheck(genomizerPort, "genomizerPort");
		nullCheck(fileLocation, "fileLocation");
		nullCheck(nrOfProcessThreads, "nrOfProcessThreads");
		nullCheck(bowtieLocation, "bowtieLocation");
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

	private static <T> void nullCheck(T parameter, String name) {
		if (parameter == null) {
			String msg = "Error! parameter " + name + " is not set. Check in " +
					"settings.cfg if it is set and spelled correctly, " +
					"capitalization does not matter.\nExiting";
			Debug.log(msg);
			ErrorLogger.log("SYSTEM", msg);
			System.exit(1);
		}
	}

	public static void readSettingsFile(String path)
			throws FileNotFoundException, URISyntaxException, MalformedURLException {
		File dbFile = new File(path);

		if (dbFile.exists()) {
			Scanner scan = new Scanner(dbFile);

			while (scan.hasNextLine()) {
				String line = scan.nextLine();

				// Skip comments and empty lines.
				if (line.trim().startsWith("#")
						|| line.trim().equals("")) {
					continue;
				}
				line = line.replaceAll("#.*$","");

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
				case "wwwtunnelurl":
					wwwTunnelURL = new URI(value);
					break;
				case "genomizerport":
					genomizerPort = Integer.parseInt(value);
					break;
				case "filelocation":
					fileLocation = value;
					break;
				case "nrofprocessthreads":
					nrOfProcessThreads = Integer.parseInt(value);
					break;
				case "bowtielocation":
					bowtieLocation = value;
					break;
				default:
					String msg = "Unrecognized setting: " + key;
					Debug.log(msg);
					ErrorLogger.log("SYSTEM", msg);
					break;
				}
			}
			scan.close();
			ServerDependentValues.DownloadURL = wwwTunnelURL.resolve(downloadURL);
			ServerDependentValues.UploadURL = wwwTunnelURL.resolve(uploadURL);

			String dataInfo =
					"\tdatabaseUsername = " + databaseUsername + "\n"
							+ "\tdatabasePassword = " + databasePassword + "\n"
							+ "\tdatabaseHost = " + databaseHost + "\n"
							+ "\tdatabaseName = " + databaseName + "\n"
							+ "\twwwTunnelURL = " + wwwTunnelURL + "\n"
							+ "\tgenomizerPort = " + genomizerPort + "\n"
							+ "\tfileLocation = " + fileLocation + "\n"
							+ "\tnrOfProcessThreads = " + nrOfProcessThreads + "\n"
							+ "\tbowtieLocation = " + bowtieLocation
							+ "\n";

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
