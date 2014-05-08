package server.test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;

import com.google.gson.JsonObject;

/**
 * Class used to test that the server works properly.
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

	/*TODO: When all checks on password/names works properly,
	 * 		make sure that this test works.
	 */
	/**
	 * Used to test that a corrupted login attempt
	 * does not pass.
	 *
	 * @throws Exception
	 */
	@Test
	public void testCorruptedLogin() throws Exception {
		/* Note: This test should work, but the code that
		 * 		 check the login is not implemented
		 * 			2014-05-08, 15:00
		 */

		//Create JSON corrupted login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "");

		int loginResponseCode = sendLogin(jj);

		assertFalse(loginResponseCode == 200);

	}

	/*TODO: When deleteAnootationCommand works properly, remove the annotation
	 *		that was added to be able to test continuously.
	 */
	/**
	 * Used to test that a annotation field can be added.
	 */
	@Test
	public void testAddAnnotationFieldCommand() throws Exception {
		/* Note: If the annotation field is added already, this test will not
		 * 		 currently pass. Change the json_output name to something else
		 * 		 in order to get it to work.remove the annotation
	 *		that was added to be able to test continuously.
		 */

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", "http://scratchy.cs.umu.se:7000/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\"species10\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

		//Get responsecode and logout.
		int responseCode = con.getResponseCode();
		sendLogout();

		assertTrue(responseCode == 201);

	}

	/**
	 * Used to test that get annotation information works.
	 *
	 * @throws Exception
	 */

	@Test
	public void testGetAnnotationInformationCommand() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("GET", "http://scratchy.cs.umu.se:7000/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == 200);

	}

}
