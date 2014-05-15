package server.test.dummies;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Search {

	static void sendSearchRequest() throws Exception {
		URL obj = new URL(testSettings.url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
			responseBuffer.append("\n");
		}
		in.close();

		String response = responseBuffer.toString();

		System.out.println("RESPONSE: " + response);

	}

}
