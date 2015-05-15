package server.test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import response.HttpStatusCode;

import java.net.HttpURLConnection;

import com.google.gson.JsonObject;

/* TODO:	- Add delete annotation after each added one.
 * 			- Add unimplemented tests.
 * 			- Implement more tests.
 */
/**
 * Class used to test that the server works properly when
 * a scenario is run.
 *
 * TODO: This test presumes that a server is running on scratchy. Modify to be standalone.
 *
 * @author tfy09jnn
 * @version 1.0
 */
@Ignore
public class ServerChainTestClass extends ServerAbstractTestClass {

	/* User story:	1. User logs in.
	 * 				2. User checks available annotations.
	 * 				3. User decides to add a new annotation field.
	 * 				4. User logs out.
	 */
	/**
	 * Test used to check Login, add annotation field,
	 * get annotation field and then logout.
	 * Here the codes are checked.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChainStatusCodeAnnoLoginGetAddLogout() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		int loginResponseCode = sendLogin(jj);

		HttpURLConnection con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int getAnnotationResponseCode = con.getResponseCode();

		con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);

		int addAnnotationResponseCode = con.getResponseCode();

		int logoutResponseCode = sendLogout();

		assertTrue(loginResponseCode == HttpStatusCode.OK);
		assertTrue(addAnnotationResponseCode == HttpStatusCode.OK);
		assertTrue(getAnnotationResponseCode == HttpStatusCode.OK);
		assertTrue(logoutResponseCode == HttpStatusCode.OK);

	}

	/* User story:	1. User logs in.
	 * 				2. User decides to add a new annotation Field.
	 * 				3. User check that the annotation field is added.
	 * 				4. User logs out.
	 */
	/**
	 * Test used to check Login, get annotation field,
	 * add annotation field and then logout.
	 * Here the codes are checked.
	 *
	 * @throws Exception
	 */
	@Test
	public void testChainStatusCodeAnnoLoginAddGetLogout() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		int loginResponseCode = sendLogin(jj);

		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"name\":\""
				+ AnnotationFieldNormal
				+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendResponseString(con, json_output);
		int addAnnotationResponseCode = con.getResponseCode();

		con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		int getAnnotationResponseCode = con.getResponseCode();

		int logoutResponseCode = sendLogout();

		assertTrue(loginResponseCode == HttpStatusCode.OK);
		assertTrue(addAnnotationResponseCode == HttpStatusCode.OK);
		assertTrue(getAnnotationResponseCode == HttpStatusCode.OK);
		assertTrue(logoutResponseCode == HttpStatusCode.OK);

	}

}
