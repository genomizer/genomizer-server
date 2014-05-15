package response.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import response.AddGenomeReleaseResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to test the AddGenomeReleaseResponse class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseResponseTest {

	//Builder used with almost all tests.
	public Gson gson = null;

	/**
	 * Setup method to initiate GSON builder.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test used to check that the created response is not null.
	 */
	@Test
	public void testCreateNotNull() {

		Response rsp = new AddGenomeReleaseResponse(StatusCode.OK, "testpath");
		assertNotNull(rsp);

	}

	/**
	 * Test used to check that the created response with code
	 * is retrievable.
	 */
	@Test
	public void testGetResponseCode() {

		Response rsp = new AddGenomeReleaseResponse(StatusCode.OK, "testpath");
		assertEquals(StatusCode.OK, rsp.getCode());

	}

	/**
	 * Test used to check that JSON de-serialization works properly.
	 */
	@Test
	public void testResponseJSON() {

		String json = "{\"filepath\":\"abc123\"}";
		Response rsp = new AddGenomeReleaseResponse(StatusCode.OK, "ABC");
		rsp = gson.fromJson(json, AddGenomeReleaseResponse.class);
	    String json2 = gson.toJson(rsp);

	    assertEquals(json2, json);

	}

}




