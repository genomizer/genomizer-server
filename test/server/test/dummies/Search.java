package server.test.dummies;

import org.junit.Ignore;

import java.net.HttpURLConnection;
import java.net.URL;

@Ignore
public class Search {

	static void sendSearchRequest(String query) throws Exception {
		URL obj = new URL(testSettings.url + "/search/?annotations=" + query);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Code : " + responseCode);
		System.out.println("RESPONSE: " +testSettings.printResponse(con));

	}

}
