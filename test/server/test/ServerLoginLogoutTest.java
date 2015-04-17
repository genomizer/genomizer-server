package server.test;

import static org.junit.Assert.*;
import java.net.HttpURLConnection;

import org.junit.Ignore;
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
 * TODO: This test presumes that a server is running on scratchy. Modify to be standalone.
 *
 * @author tfy09jnn
 * @version 1.0
 */
@Ignore
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

	/**
	 * Test case that checks that a to long password fails
	 * the login attempt.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginBadPassword() throws Exception {



		JsonObject jj = new JsonObject();
		String pw = "";
		for(int i = 0; i < database.constants.MaxSize.PASSWORD + 1; i++) {
			pw = pw + "a";
		}
		jj.addProperty("username", username);
		jj.addProperty("password", pw);
		HttpURLConnection con = connect("POST", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");
		String json_output = jj.toString();
		sendResponseString(con, json_output);

		int loginResponseCode = con.getResponseCode();

		assertTrue(StatusCode.BAD_REQUEST == loginResponseCode);

	}

	/**
	 * Test case that checks that a to long username fails
	 * the login attempt.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginBadUsername() throws Exception {

		String un = "";
		for(int i = 0; i < database.constants.MaxSize.USERNAME + 1; i++) {
			un = un + "a";
		}
		JsonObject jj = new JsonObject();
		jj.addProperty("username", un);
		jj.addProperty("password", password);

		HttpURLConnection con = connect("POST", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");
		String json_output = jj.toString();
		sendResponseString(con, json_output);

		int loginResponseCode = con.getResponseCode();

		assertTrue(StatusCode.BAD_REQUEST == loginResponseCode);

	}

	/**
	 * Test case that checks that no username fails
	 * the login attempt.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginNoUsername() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("password", password);

		HttpURLConnection con = connect("POST", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");
		String json_output = jj.toString();
		sendResponseString(con, json_output);

		int loginResponseCode = con.getResponseCode();

		assertTrue(StatusCode.BAD_REQUEST == loginResponseCode);

	}

	/**
	 * Test case that checks that no password fails
	 * the login attempt.
	 *
	 * @throws Exception
	 */
	@Test
	public void testLoginNoPassword() throws Exception {

		JsonObject jj = new JsonObject();
		jj.addProperty("username", username);

		HttpURLConnection con = connect("POST", serverURL + "/login");
		con.setRequestProperty("Content-Type", "application/json");
		String json_output = jj.toString();
		sendResponseString(con, json_output);

		int loginResponseCode = con.getResponseCode();

		assertTrue(StatusCode.BAD_REQUEST == loginResponseCode);

	}

}
