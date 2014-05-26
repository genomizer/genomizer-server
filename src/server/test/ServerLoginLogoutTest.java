package server.test;

import static org.junit.Assert.*;
import org.junit.Test;
import response.StatusCode;
import com.google.gson.JsonObject;

/* TODO:	- Add login authorization tests when code is implemented.
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

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);

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

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);

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

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);

		sendLogin(jj);
		sendLogout();

		assertNotNull(token);

	}

}
