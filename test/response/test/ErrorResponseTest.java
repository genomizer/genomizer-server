package response.test;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import response.ErrorResponse;
import response.Response;
import response.HttpStatusCode;

public class ErrorResponseTest {

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


	@Test
	public void shouldHaveCorrectCode() {
		assertEquals(404, new ErrorResponse(HttpStatusCode.NOT_FOUND, "").getCode());
	}
	
	@Test
	public void shouldGenerateJsonMessage() {
		Response resp = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Error text");
		String expected = "{\"message\":\"Error text\"}\n";
		assertEquals(expected, resp.getBody());
	}

	/**
	 * Test used to create the login response.
	 */
	@Test
	public void testCreateErrorResponseNotNull() {
		ErrorResponse rsp = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,"abcdefg123");
		assertNotNull(rsp);
	}

	/**
	 * Test used to check that conversion between JSON
	 * and objects works properly.
	 */
	@Test
	public void testErrorResponseJSON() {
		String json = "{\"message\":\"Error-message\"}";
		ErrorResponse rsp = gson.fromJson(json, ErrorResponse.class);
		String json2 = gson.toJson(rsp);
		assertEquals(json, json2);
	}

	/**
	 * Test used to check that LoginResponse works with
	 * the getBody() in the superclass.
	 */
	@Test
	public void testErrorResponseGetBody() {
		String json = "{\"message\":\"Error Message\"}" + "\n";
		ErrorResponse rsp = gson.fromJson(json, ErrorResponse.class);
		assertEquals(json, rsp.getBody());
	}

}
