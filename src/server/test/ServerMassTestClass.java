package server.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class used to test the server login.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerMassTestClass extends ServerAbstractTestClass {

	/**
	 * Test case for login and logout from the server.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginLogout() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);
		int logoutResponseCode = sendLogout();

		assertEquals(loginResponseCode, 200);
		assertNotNull(token);
		assertEquals(logoutResponseCode, 200);

	}

	/**
	 * Used to test that a corrupted login attempt
	 * does not pass.
	 *
	 * @throws Exception
	 */
	@Test
	public void testCorruptedLogin() throws Exception {

		//Create JSON corrupted login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "");

		int loginResponseCode = sendLogin(jj);

		assertFalse(loginResponseCode == 200);

	}

	/**
	 * Used to test that a annotation field can be added.
	 */
	@Test
	public void testAddAnnotationFieldCommand() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		String url = "http://scratchy.cs.umu.se:7000/annotation";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\"species8\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

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

		sendLogout();

		assertTrue(responseCode == 201);

	}

}
