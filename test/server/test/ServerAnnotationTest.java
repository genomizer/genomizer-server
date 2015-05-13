package server.test;

import static org.junit.Assert.*;
import java.net.HttpURLConnection;

import org.junit.Ignore;
import org.junit.Test;
import response.HttpStatusCode;
import com.google.gson.JsonObject;

/**
 * Class used to test that the annotation handling works
 * properly with the server.
 *
 * TODO: This test presumes that a server is running on scratchy. Modify to be standalone.
 *
 * @author tfy09jnn
 * @version 1.0
 * */
@Ignore
public class ServerAnnotationTest extends ServerAbstractTestClass {

	/**
	* Test used to check if an added annotation freetext exists
	* when the server has responded.
	*
	* @throws Exception
	*/
	@Test
	public void testAnnotationFreetextAddAndExists() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		String json_output = "{\"name\":\""
		+ AnnotationFieldFreetext
		+ "\",\"type\":[\"freetext\"],\"default\":\"q\",\"forced\":true}";

		sendAddAnnotationFieldOrFreetext(json_output);
		String response = sendGetAnnotationInfo();

		String responseChecker = "\"name\":\""
		+ AnnotationFieldFreetext
		+ "\",\"values\":[\"freetext\"],\"forced\":true";

		boolean wasAdded = response.contains(responseChecker);

		int deleteResponse = sendDeleteAnnotation(AnnotationFieldFreetext);

		sendLogout();

		assertTrue(wasAdded);
		assertTrue(deleteResponse == HttpStatusCode.OK);

	}

	/**
	* Test used to check if an added annotation field exists
	* when the server has responded.
	*
	* @throws Exception
	*/
	@Test
	public void testAnnotationFieldAddAndExists() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		String json_output = "{\"name\":\""
		+ AnnotationFieldNormal
		+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		sendAddAnnotationFieldOrFreetext(json_output);
		String response = sendGetAnnotationInfo();

		String responseChecker = "\"name\":\""
		+ AnnotationFieldNormal
		+ "\",\"values\":[\"fly\",\"rat\",\"human\"],\"forced\":true";

		boolean wasAdded = response.contains(responseChecker);
		int deleteResponse = sendDeleteAnnotation(AnnotationFieldNormal);

		sendLogout();

		assertTrue(wasAdded);
		assertTrue(deleteResponse == HttpStatusCode.OK);

	}

	/**
	* Test used to check that a annotation field can be added.
	*
	* @throws Exception
	*/
	@Test
	public void testStatusCodeAddAnnotationFieldCommand() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		String json_output = "{\"name\":\""
		+ AnnotationFieldNormal
		+ "\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";

		int responseCode = sendAddAnnotationFieldOrFreetext(json_output);
		int deleteResponse = sendDeleteAnnotation(AnnotationFieldNormal);

		sendLogout();

		assertTrue(responseCode == HttpStatusCode.OK);
		assertTrue(deleteResponse == HttpStatusCode.OK);

	}

	/**
	* Test used to check that adding annotation freetext
	* works properly.
	*
	* @throws Exception
	*/
	@Test
	public void testStatusCodeAddAnnotatinFieldFreeText() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		String json_output = "{\"name\":\""
		+ AnnotationFieldFreetext
		+ "\",\"type\":[\"freetext\"],\"default\":\"q\",\"forced\":false}";

		int responseCode = sendAddAnnotationFieldOrFreetext(json_output);

		sendLogout();

		assertTrue(responseCode == HttpStatusCode.OK);

	}

	/**
	* Test used to check that get annotation information works.
	*
	* @throws Exception
	*/
	@Test
	public void testGetAnnotationInformationCommand() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		String response = sendGetAnnotationInfo();
		sendLogout();

		assertNotNull(response);

	}

	/**
	 * Method used to delete an annotation.
	 *
	 * @return response code
	 * @throws Exception
	 */
	private int sendDeleteAnnotation(String annotationFieldName) throws Exception {

		HttpURLConnection con = connect("DELETE", serverURL + "/annotation/field/" + annotationFieldName);
		con.setRequestProperty("Authorization", token.getToken());

		return con.getResponseCode();

	}

	/**
	 * Method that sends a get annotation information
	 * to the server.
	 *
	 * @return String with the response.
	 * @throws Exception
	 */
	private String sendGetAnnotationInfo() throws Exception {

		HttpURLConnection con = connect("GET", serverURL + "/annotation");
		con.setRequestProperty("Authorization", token.getToken());

		return getResponseString(con);

	}

	/**
	 * Method that sends eather freetext or field.
	 *
	 * @return response code.
	 * @throws Exception
	 */
	private int sendAddAnnotationFieldOrFreetext(String json) throws Exception {

		HttpURLConnection con = connect("POST", serverURL + "/annotation");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		sendResponseString(con, json);

		return con.getResponseCode();

	}

}
