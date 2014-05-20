package server.test.dummies;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class testSettings {
	public static final int port = 7000;

//	public static String host = "localhost";
	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;



	static String printResponse(HttpURLConnection con) throws IOException{

		StringBuffer responseBuffer = null;
		BufferedReader in = null;
		int responseCode = 0;

		if((responseCode = con.getResponseCode()) >= 400) {
			in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}

		String inputLine;
		responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		System.out.println("Response Code: " + responseCode);
		return responseBuffer.toString();
	}

	static void sendToServer(HttpURLConnection con, String json_output) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

	}

}
