package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EditAnnotationValueDummy {

	public static final int port = 7000;


	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;
	public static Token token = null;

	public static void main(String args[]) throws Exception {
		
		

	}




	private static String printResponse(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		System.out.println("Response Code: " + responseCode);
		return responseBuffer.toString();
	}

	private static void sendToServer(HttpURLConnection con, String json_output) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

	}
}