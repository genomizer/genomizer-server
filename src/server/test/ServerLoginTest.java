package server.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;

import java.net.URL;
import java.io.DataOutputStream;

import com.google.gson.*;

/**
 * Class used to test the server login.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerLoginTest {

	private static Token token = null;

	/**
	 * Main method to execute commands.
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {

		int responseCode = 0;

		//Normal login attempt.
		responseCode = sendLogin();

		if(responseCode == 200 && token != null) {

			System.out.println("[SUCCESS] Legit login test success. Did receive code: "
			+ responseCode + " and wanted code: 200.");

		} else {

			System.out.println("[FAILED] Legit login test failed. Did receive code:"
			+ responseCode + " and wanted error code.");

		}

	}

	/**
	 * Class to handle the login.
	 *
	 *
	 * @throws Exception
	 */
	private static int sendLogin() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

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