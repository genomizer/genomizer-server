package server.test.dummies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Process {



	public static void process() throws IOException{


		String filename = "filename1234";
		String fileid = "1";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";


		URL obj = new URL(testSettings.url + "/process");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Authorization", Login.getToken());

		String json = "{" +
				"\"filename\": \"" + filename + "\"," +
				"\"fileId\": \"" + fileid + "\"," +
				"\"expid\": \"" + expid + "\"," +
				"\"processtype\": \"" + processtype + "\"," +
				"\"parameters\": [" + parameters + "]," +
				"\"metadata\": \"" + metadata + "\"," +
				"\"genomeRelease\": \"" + genomeRelease + "\"," +
				"\"author\": \"" + author + "\"}";


		testSettings.sendToServer(con, json);

		System.out.println("\nSending 'PUT' request to URL : " + testSettings.url);
		System.out.println("Reponse Body: " + testSettings.printResponse(con));
	}
}
