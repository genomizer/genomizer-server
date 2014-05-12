package server.test;

import static org.junit.Assert.*;
import org.junit.Test;
import response.StatusCode;
import java.net.HttpURLConnection;

import com.google.gson.JsonObject;

/* TODO:	- Add delete annotation after each added one.
 * 			- Implement more tests.
 * 			- Add unimplemented tests.
 * 			- Add login authorization tests when code is implemented.
 * 			- Delete annotation test needs a rework, API is currently wrong.
 */
/**
 * Class used to test that the server works properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerMassTestClass extends ServerAbstractTestClass {

	/**
	 * Method used to test the login response code.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginResponseCode() throws Exception{

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);
		sendLogout();

		assertEquals(loginResponseCode, StatusCode.OK);

	}

	/**
	 * Method used to test the logout response code.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLogoutResponseCode() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		int logoutResponseCode = sendLogout();

		assertEquals(logoutResponseCode, StatusCode.OK);

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

	//TODO: Check this test later to make sure it works properly.
	/**
	 * Test used to check if an added annotation freetext exists
	 * when the server has responded.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAnnotationFreetextAddAndExists() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ "aaaaaa567"
				+ "\",\"type\":[\"freetext\"],\"default\":\"q\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get connection and then add headers.
		con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String response = getResponseString(con);
		System.out.println(response);
		String responseChecker = "\"name\":\""
				+ "aaaaaa56"
				+ "\",\"values\":[\"freetext\"],\"forced\":true";
		System.out.println(responseChecker);
		boolean wasAdded = response.contains(responseChecker);

		sendLogout();

		assertTrue(wasAdded);

	}

	@Test
	public void testContains() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		//Get connection and then add headers.
		HttpURLConnection con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String response = getResponseString(con);
		System.out.println(response);
		String responseChecker = "\"name\":\""
				+ "ABC996"
				+ "\",\"values\":[\"freetext\"],\"forced\":true";
		System.out.println(responseChecker);
		boolean wasAdded = response.contains(responseChecker);

		sendLogout();

		assertTrue(wasAdded);

	}


	/**
	 * Test used to check if an added annotation field exists
	 * when the server has responded.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAnnotationFieldAddAndExists() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Add the annotation.
		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get connection and then add headers.
		con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String response = getResponseString(con);
		String responseChecker = "\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"values\":[\"fly\",\"rat\",\"human\"],\"forced\":true";
		boolean wasAdded = response.contains(responseChecker);

		sendLogout();

		assertTrue(wasAdded);

	}

	/**
	 * Test used to check Login, add annotation field,
	 * get annotation field and then logout.
	 * Here the codes are checked.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChainStatusCodeAnnoLoginGetAddLogout() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int getAnnotationResponseCode = con.getResponseCode();

		//Get connection and then add headers.
		con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get AnnotationResponseCode
		int addAnnotationResponseCode = con.getResponseCode();

		int logoutResponseCode = sendLogout();

		assertTrue(loginResponseCode == StatusCode.OK);
		assertTrue(addAnnotationResponseCode == StatusCode.CREATED);
		assertTrue(getAnnotationResponseCode == StatusCode.OK);
		assertTrue(logoutResponseCode == StatusCode.OK);

	}

	/**
	 * Test used to check Login, get annotation field,
	 * add annotation field and then logout.
	 * Here the codes are checked.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChainStatusCodeAnnoLoginAddGetLogout() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get AnnotationResponseCode
		int addAnnotationResponseCode = con.getResponseCode();

		//Get connection and then add headers.
		con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int getAnnotationResponseCode = con.getResponseCode();

		int logoutResponseCode = sendLogout();

		assertTrue(loginResponseCode == StatusCode.OK);
		assertTrue(addAnnotationResponseCode == StatusCode.CREATED);
		assertTrue(getAnnotationResponseCode == StatusCode.OK);
		assertTrue(logoutResponseCode == StatusCode.OK);

	}

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
		/*
		//Create JSON corrupted login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "");

		int loginResponseCode = sendLogin(jj);

		assertFalse(loginResponseCode == StatusCode.OK);
		*/

		fail("Not yet implemented.");

	}

	/**
	 * Test used to check that a annotation field can be added.
	 *
	 * @throws Exception
	 */
	@Test
	public void testStatusCodeAddAnnotationFieldCommand() throws Exception {
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
		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		//Get responsecode and logout.
		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == StatusCode.CREATED);

	}

	/**
	 * Test used to check that the delete annotation works.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteAnnotationFieldCommand() throws Exception {
		/*
		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("DELETE", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "[{\"id\":1,\"values\":[\"man\",\"mouse\"]},{\"id\":2,\"values\":[]},{\"id\":3,\"values\":[\"leg\"]}]";

		sendLogout();

		assertTrue(responseCode == StatusCode.CREATED);
		*/
		fail("Not yet implemented.");
	}

	/**
	 * Test used to check that adding annotation freetext
	 * works properly.
	 *
	 * @throws Exception
	 */
	@Test
	public void testStatusCodeAddAnnotatinFieldFreeText() throws Exception {
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
		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldFreetext
				+ "\",\"type\":[\"freetext\"],\"default\":\"q\",\"forced\":false}";

		sendResponseString(con, json_output);

		//Get responsecode and logout.
		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == StatusCode.CREATED);

	}

	/**
	 * Test used to check that get annotation information works.
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
		HttpURLConnection con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == StatusCode.OK);

	}

	/**
	 * Used to test that the server responds correctly when a request
	 * that does not exist is sent.
	 *
	 * @throws Exception
	 */
	@Test
	public void testCorruptedGetAnnotationInformationCommand() throws Exception {
		/* Note: Not tested. Check what the server should respond if getting
		 * 		 a wierd request of this kind.
		 */
		/*
		Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);

		//Get connection and then add headers. (Added /corrupted)
		HttpURLConnection con = connect("GET", serverURL + "/corrupted");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		sendLogout();

		assertTrue(responseCode == 204);

		*/
		fail("Not yet implemented.");

	}

}
