package server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServerSettings {

	public static String databaseUsername = "c5dv151_vt14";
	public static String databasePassword = "shielohh";
	public static String databaseHost = "postgres";
	public static String databaseName = "c5dv151_vt14";
	public static String publicAddress = "http://scratchy.cs.umu.se";
	public static int apachePort = 8000;
	public static String downloadURL = "/download.php?path=";
	public static String uploadURL = "/upload.php?path=";
	public static int genomizerPort = 7000;
	public static String passwordHash = "2fd26e9aea528153a865257a723f6d4859e9f6c4a6775c003ae91297f619c6e8";
	public static String passwordSalt = "genomizer";

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
			System.err.println("Could not write to file: " + path);
		}
	}

}
