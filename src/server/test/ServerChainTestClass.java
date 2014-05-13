package server.test;

import static org.junit.Assert.*;
import org.junit.Test;
import response.StatusCode;
import java.net.HttpURLConnection;

import com.google.gson.JsonObject;

/* TODO:	- Add delete annotation after each added one.
 * 			- Implement more tests.
 * 			- Add unimplemented tests.
 */
/**
 * Class used to test that the server works properly when
 * a scenario is run.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerChainTestClass extends ServerAbstractTestClass {

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

}
