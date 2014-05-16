package server.test;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import org.junit.Test;

import response.StatusCode;

import com.google.gson.JsonObject;

/**
 * Class used to test Genome release add and delete.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerGenomeReleaseTest extends ServerAbstractTestClass {


	/**
	 * Method used to test that a genome release can be added
	 * and returns the proper response code.
	 * @throws Exception
	 */
	/*
	@Test
	public void testAddGenomeRelease() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");
		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/genomeRelease");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"fileName\":\"com_TestFile\", \"specie\":\"human\",\"genomeVersion\": \"hg38\"}";

		sendResponseString(con, json_output);

		String response = getResponseString(con);

		System.out.println("Response filename is: " + response);


	}
	*/
	@Test
	public void testAddGenomeReleaseResponseCode() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");
		sendLogin(jj);

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/genomeRelease");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"fileName\":\"com_TestFile\", \"specie\":\"human\",\"genomeVersion\": \"hg38\"}";

		sendResponseString(con, json_output);

		int responseCode = con.getResponseCode();
		
		System.out.println("response: " + responseCode);
		
		assertEquals(responseCode, StatusCode.CREATED);

	}


	@Test
	public void test() {
		fail("Not yet implemented");
	}

}




















