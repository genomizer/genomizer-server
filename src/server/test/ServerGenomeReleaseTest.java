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
	 * Test method that checks if adding and deleting is working properly.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAddDeleteAddDelete() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");
		sendLogin(jj);

		boolean firstAdd = addGenomeRelease();
		boolean firstRemove = deleteGenomeRelease();

		boolean secondAdd = addGenomeRelease();
		boolean secondDelete = deleteGenomeRelease();

		sendLogout();

		assertTrue(firstAdd);
		assertTrue(firstRemove);
		assertTrue(secondAdd);
		assertTrue(secondDelete);

	}

	/**
	 * Method used to delete a genome release.
	 *
	 * @return boolean depending on result.
	 * @throws Exception
	 */
	private boolean deleteGenomeRelease() throws Exception  {
		//Get connection and then add headers.
		HttpURLConnection con = connect("DELETE", serverURL + "/genomeRelease/human/hg385");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();

		if(responseCode == StatusCode.OK) {
			return true;
		}
		return false;
	}

	/**
	 * Method used to add a genome release.
	 *
	 * @return boolean depending on result.
	 * @throws Exception
	 */
	private boolean addGenomeRelease() throws Exception {

		//Get connection and then add headers.
		HttpURLConnection con = connect("POST", serverURL + "/genomeRelease");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"fileName\":\"com_TestFile385\",\"specie\":\"human\",\"genomeVersion\":\"hg385\"}";

		sendResponseString(con, json_output);
		int responseCode = con.getResponseCode();

		if(responseCode == StatusCode.CREATED) {
			return true;
		}
		return false;
	}

}




















