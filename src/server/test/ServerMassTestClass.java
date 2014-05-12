package server.test;

import static org.junit.Assert.*;
import org.junit.Test;

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
	 * Method used to test the login response code.
	 */
	@Test
	public void testLoginResponseCode() throws Exception{

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);
		sendLogout();

		assertEquals(loginResponseCode, 200);

	}

	/**
	 * Method used to test the logout response code.
	 */
	@Test
	public void testLogoutResponseCode() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		int logoutResponseCode = sendLogout();

		assertEquals(logoutResponseCode, 200);

	}

	/**
	 * Test case for login and logout from the server.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginLogoutTokenNotNull() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		sendLogout();

		assertNotNull(token);

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

	//@Test
	//public void testCorruptedLogin() throws Exception {
		/* Note: This test should work, but the code that
		 * 		 check the login is not implemented
		 * 			2014-05-08, 15:00
		 */

		//Create JSON corrupted login object.
	//	JsonObject jj = new JsonObject();
	//	jj.addProperty("username", "jonas");
	//	jj.addProperty("password", "");

	//	int loginResponseCode = sendLogin(jj);

	//	assertFalse(loginResponseCode == 200);

	//}

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
		 *		 that was added to be able to test continuously.
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

		String json_output = "{\"name\":\"com_annofield_test\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get responsecode and logout.
		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == 201);

	}

	//TODO: Delete not implemented properly yet. API is wrong(2014-05-12).
	/**
	 * Testcase used to test that the delete annotation works.
	 */
	/*
	@Test
	public void testDeleteAnnotationFieldCommand() {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("DELETE", "http://scratchy.cs.umu.se:7000/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "[{\"id\":1,\"values\":[\"man\",\"mouse\"]},{\"id\":2,\"values\":[]},{\"id\":3,\"values\":[\"leg\"]}]";

		sendLogout();

		assertTrue(responseCode == 201);

	}
	*/

	/**
	 * Testcase used to check that adding annotation freetext
	 * works properly.
	 * @throws Exception
	 */
	@Test
	public void testAddAnnotatinFieldFreeText() throws Exception {
		/* Note: If the annotation freetext is added already, this test will not
		 * 		 currently pass. Change the json_output name to something else
		 * 		 in order to get it to work.remove the annotation
		 *		 that was added to be able to test continuously.
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

		String json_output = "{\"name\":\"com_freetext_test\",\"type\":[\"freetext\"],\"default\":\"q\",\"forced\":false}";

		sendResponseString(con, json_output);

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

	/**
	 * Used to test that the server responds correctly when a request
	 * that does not exist is sent.
	 *
	 * @throws Exception
	 */

	//@Test
	//public void testCorruptedGetAnnotationInformationCommand() throws Exception {
		/* Note: Not tested. Check what the server should respond if getting
		 * 		 a wierd request of this kind.
		 */

		//Create JSON login object.
	//	JsonObject jj = new JsonObject();
	//	jj.addProperty("username", "jonas");
	//	jj.addProperty("password", "losenord");

	//	sendLogin(jj);

		//Get connection and then add headers. (Added /corrupted)
	//	HttpURLConnection con = connect("GET", "http://scratchy.cs.umu.se:7000/corrupted");
	//	con.setRequestProperty("Content-Type", "application/json");
	//	con.setRequestProperty("Authorization", token.getToken());

	//	int responseCode = con.getResponseCode();

	//	sendLogout();

	//	assertTrue(responseCode == 204);

	//}

}




