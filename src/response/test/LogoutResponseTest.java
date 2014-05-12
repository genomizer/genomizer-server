package response.test;

import static org.junit.Assert.*;

import org.junit.Test;

import response.LogoutResponse;

/**
 * Testclass used to test the LogoutResponse class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class LogoutResponseTest {

	/**
	 * Test the creation of a logout response.
	 */
	@Test
	public void testCreateLogoutResponseNotNull() {

		LogoutResponse rsp = new LogoutResponse(200);
		assertNotNull(rsp);

	}

}








