package server.test;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import org.junit.Ignore;
import org.junit.Test;

import response.StatusCode;

import com.google.gson.JsonObject;

/**
 * Class used to test Genome release add and delete.
 *
 * TODO: This test presumes that a server is running on scratchy. Modify to be standalone.
 *
 * @author tfy09jnn
 * @version 1.0
 */
@Ignore
public class ServerGenomeReleaseTest extends ServerAbstractTestClass {

	/**
	 * Test method that checks if adding and deleting is working properly.
	 *
	 * @throws Exception
	 */
	@Test
	public void testAddDeleteAddDelete() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		int firstAdd = addGenomeRelease();
		int firstRemove = deleteGenomeRelease();
		int secondAdd = addGenomeRelease();
		int secondDelete = deleteGenomeRelease();

		sendLogout();

		assertTrue(StatusCode.CREATED == firstAdd);
		assertTrue(StatusCode.OK == firstRemove);
		assertTrue(StatusCode.CREATED == secondAdd);
		assertTrue(StatusCode.OK == secondDelete);

	}

	/**
	 * Test that checks that you cant add duplicate values.
	 *
	 * @throws Exception
	 */

	@Test
	public void addDuplicateValues() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		int result = addGenomeRelease();
		result = addGenomeRelease();
		deleteGenomeRelease();

		sendLogout();

		assertTrue(StatusCode.BAD_REQUEST == result);

	}

	/**
	 * Test to delete a genome release that does not exist.
	 *
	 * @throws Exception
	 */
	@Test
	public void testDeleteWhenNotExist() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		sendLogin(jj);

		HttpURLConnection con = connect("DELETE", serverURL + "/genomeRelease/human22/hg999DNE2");
		con.setRequestProperty("Authorization", token.getToken());

		int responseCode = con.getResponseCode();
		sendLogout();

		assertTrue(responseCode == StatusCode.BAD_REQUEST);

	}

	/**
	 * Method used to delete a genome release.
	 *
	 * @return the response code.
	 * @throws Exception
	 */
	private int deleteGenomeRelease() throws Exception  {

		HttpURLConnection con = connect("DELETE", serverURL + "/genomeRelease/human/hg385");
		con.setRequestProperty("Authorization", token.getToken());

		return con.getResponseCode();
	}

	/**
	 * Method used to add a genome release.
	 *
	 * @return the response code.
	 * @throws Exception
	 */
	private int addGenomeRelease() throws Exception {

		HttpURLConnection con = connect("POST", serverURL + "/genomeRelease");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		String json_output = "{\"fileName\":\"com_TestFile385\",\"specie\":\"human\",\"genomeVersion\":\"hg385\"}";

		sendResponseString(con, json_output);

		return con.getResponseCode();
	}

}
