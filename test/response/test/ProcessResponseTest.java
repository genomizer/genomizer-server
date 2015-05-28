package response.test;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import response.ProcessResponse;

//TODO: Add more test methods, for example, see login/logout response.

/**
 * Testclass used to test the process response
 * class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ProcessResponseTest {

	//Builder used with almost all tests.
	public Gson gson = null;

	/**
	 * Setup method to initiate gson builder.
	 */
	@Before
	public void setUp() {
		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
	}

	/**
	 * Test the creation of ProcessResponse and that
	 * it's not null.
	 */
	@Test
	public void testCreateProcessResponseNotNull() {
		ProcessResponse rsp = new ProcessResponse(200);
		assertNotNull(rsp);
	}


	/**
	 * Test used to check that conversion between JSON
	 * and objects works properly.
	 */
	@Test
	public void testProcessResponseJSON() {
		String json = "{\"message\":\"Message\"}";
		ProcessResponse rsp = gson.fromJson(json, ProcessResponse.class);
		String json2 = gson.toJson(rsp);

		assertEquals(json, json2);
	}

	/**
	 * Test used to check that LoginResponse works with
	 * the getBody() in the superclass.
	 */
	@Test
	public void testProcessResponseGetBody() {
		String json = "{\"message\":\"Message\"}" + "\n";
		ProcessResponse rsp = gson.fromJson(json, ProcessResponse.class);

		assertEquals(json, rsp.getBody());

	}
}
