package server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

/**
 * Class used to test the server login.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class ServerMassTestClass extends ServerAbstractTestClass {

	/**
	 * Test case for login and logout from the server.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginLogout() throws Exception {

		//Create JSON login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");


		int loginResponseCode = sendLogin(jj);
		int logoutResponseCode = sendLogout();

		assertEquals(loginResponseCode, 200);
		assertNotNull(token);
		assertEquals(logoutResponseCode, 200);

	}

	/**
	 * Used to test that a corrupted login attempt
	 * does not pass.
	 *
	 * @throws Exception
	 */
	@Test
	public void testCorruptedLogin() throws Exception {

		//Create JSON corrupted login object.
		JsonObject jj = new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "");

		int loginResponseCode = sendLogin(jj);

		assertFalse(loginResponseCode == 200);

	}





}










