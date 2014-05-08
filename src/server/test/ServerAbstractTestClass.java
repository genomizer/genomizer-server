package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
	protected static Token token = null;

	/**
	 * method used to set the token.
	 *
	 * @param response
	 */
	public void setToken(String response) {

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);

	}

	/**
	 * Class used to get the response.
	 * @param con
	 * @return
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

	/**
	 * Used to open a connection.
	 *
	 * @return the connection.
	 * @throws Exception
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
	 * @throws Exception
	 */
	public int sendLogin(JsonObject jj) throws Exception {

		//Get the connection.
		HttpURLConnection con = connect("POST", "http://scratchy.cs.umu.se:7000/login");

		//Add request header
		con.setRequestProperty("Content-Type", "application/json");

		//Get JSON string.
		String json_output = jj.toString();

		//Write the JSON body.
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

	/**
	 * Used to handle the logout attempt.
	 *
	 * @return integer representing the responseCode.
	 * @throws Exception
	 */
	public int sendLogout() throws Exception {

		//Get the connection.
		HttpURLConnection con = connect("DELETE", "http://scratchy.cs.umu.se:7000/login");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		return responseCode;

	}

}
