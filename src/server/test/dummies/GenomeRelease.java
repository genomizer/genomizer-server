package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

public class GenomeRelease {

	public GenomeRelease() {
		// TODO Auto-generated constructor stub
	}

	static void sendGetGenomeRelease() throws Exception {

		URL obj = new URL(testSettings.url + "/genomeRelease");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());

		testSettings.sendToServer(con,"");

		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

}
