package server.test;

import static org.junit.Assert.*;
import org.junit.Test;
import response.StatusCode;
import com.google.gson.JsonObject;

/* TODO:	- Add delete annotation after each added one.
 * 			- Add login authorization tests when code is implemented.
 *  		- Make unimplemented tests work.
 *  		- Implement more tests.
 *  			- Login that sends no password in JSON.
 *  			- Login that sends no username in JSON.
 */
/**
 * Class used to test that login and logout works
 * with the server.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerLoginLogoutTest extends ServerAbstractTestClass {

	/**
	 * Method used to test the login response code.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginResponseCode() throws Exception{

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		int loginResponseCode = sendLogin(jj);
		sendLogout();

		assertEquals(loginResponseCode, StatusCode.OK);

	}

	/**
	 * Method used to test the logout response code.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLogoutResponseCode() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		int logoutResponseCode = sendLogout();

		assertEquals(logoutResponseCode, StatusCode.OK);

	}

	/**
	 * Test case for login and logout from the server.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginLogoutTokenNotNull() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendLogin(jj);
		sendLogout();

		assertNotNull(token);

	}


	/**
	 * Used to test that a corrupted login attempt
	 * does not pass.
	 *
	 * @throws Exception
	 */
	/*
	@Test
	public void testCorruptedLogin() throws Exception {
	*/
		/* Note: This test should work, but the code that
		 * 		 check the login is not implemented
		 * 			2014-05-08, 15:00
		 */
		/*
		//Create JSON corrupted login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "");

		int loginResponseCode = sendLogin(jj);

		assertFalse(loginResponseCode == StatusCode.OK);
		*/
	/*
		fail("Not yet implemented.");

	}
	*/
}
