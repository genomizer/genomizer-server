package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Abstract class used for testing the server.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public abstract class ServerAbstractTestClass {

	//Token used to identify users.
	public static Token token = null;

	//Used together with add annotation field/freetext to change to unique names.
	protected String AnnotationFieldFreetext = "com_AnnoFTTEST9";
	protected String AnnotationFieldNormal = "com_AnnoFDTEST14";

	//Server URL and port.
	protected String port = "7000";
	protected String serverURL = "http://scratchy.cs.umu.se:" + port;
	//protected String serverURL = "http://localhost:" + port;

	/**
	 * Method used to set the token.
	 *arg0
	 * @param response from the server.
	 */
	public void setToken(String response) {

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);

	}

	/**
	 * Method used to send JSON.
	 *
	 * @param The connection to send to.
	 * @param json_to_send.
	 * @throws IOException.
	 */
	public void sendResponseString(HttpURLConnection con, String json_to_send) throws IOException {

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_to_send.getBytes());
		wr.flush();
		wr.close();

	}

	/**
	 * Class used to get the response.
	 *
	 * @param HTTP connection to get response from.
	 * @return A string with the response.
	 * @throws Exception
	 */
	public String getResponseString(HttpURLConnection con) throws Exception {

		BufferedReader in = new BufferedReader(
			    new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		return responseBuffer.toString();
	}
	//Add request header
	/**
	 * Used to open a connection.
	 *
	 * @return the connection.
	 * @throws Exception.
	 */
	public HttpURLConnection connect(String reqMethod, String restful) throws Exception {

		URL obj = new URL(restful);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//Set the request property.
		con.setRequestMethod(reqMethod);

		return con;
	}

	/**
	 * Used to handle the login attempt.
	 *
	 * @return integer representing the responseCode.
	 * @throws Exception.
	 */
	public int sendLogin(JsonObject jj) throws Exception {

		//Get the connection and add request headers.
		HttpURLConnection con = connect("POST", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");

		//Get JSON string.
		String json_output = jj.toString();

		//Write the JSON body.
		sendResponseString(con, json_output);

		int responseCode = con.getResponseCode();

		String response =getResponseString(con);

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);

		return responseCode;

	}

	/**
	 * Used to handle the logout attempt.
	 *
	 * @return integer representing the responseCode.
	 * @throws Exception.
	 */
	public int sendLogout() throws Exception {

		//Get the connection and add request headers.
		HttpURLConnection con = connect("DELETE", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		return responseCode;

	}

}
