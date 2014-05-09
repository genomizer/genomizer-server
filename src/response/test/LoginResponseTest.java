package response.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import response.LoginResponse;

/**
 * Testclass used to test the login response class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class LoginResponseTest {

	//Builder used with almost all tests.
	public Gson gson = null;

	/**
	 * Setup method to initiate gson builder.
	 */
	@Before
	public void setUp() {

		//Create the builder.
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test used to create the login response.
	 */
	@Test
	public void testCreateLoginResponseNotNull() {

		LoginResponse rsp = new LoginResponse(200, "abcdefg123");
		assertNotNull(rsp);

	}

	/**
	 * Test used to check that conversion between JSON
	 * and objects works properly.
	 */
	@Test
	public void testLoginResponseJSON() {

		String json = "{\"token\":\"user-id\"}";
		LoginResponse rsp = new LoginResponse(200, "abcdefg123");
		rsp = gson.fromJson(json, LoginResponse.class);
		String json2 = gson.toJson(rsp);

		assertEquals(json, json2);

	}

}