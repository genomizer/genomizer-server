package server.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

/* TODO: Add logout command.
 * 		 Make JUnit testcase.
 */
/**
 * Class used to test server with corrupted login.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerCorruptedLoginTest {

	private static Token token = null;

	/**
	 * Main method to execute commands.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		int responseCode = 0;

		//Corrupted login attempt.
		responseCode = sendCorruptedLogin();

		if(responseCode == 200 && token != null) {

			System.out.println("[FAILED] Corrupted login test success. Did receive code:"
			+ responseCode + " and wanted error code.");

		} else {

			System.out.println("[SUCCESS] Corrupted login test failed. Did receive code: "
			+ responseCode + " and wanted other code then 200.");

		}

	}

	/**
	 * Class that sends a corrupted login where
	 * the JSON password is removed.
	 *
	 * @throws Exception
	 */
	private static int sendCorruptedLogin() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "jonas");

		String json_output = jj.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);

		return responseCode;

	}

}
