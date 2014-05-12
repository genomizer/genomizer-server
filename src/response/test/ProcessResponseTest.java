package response.test;

import static org.junit.Assert.*;

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

	/**
	 * Test the creation of ProcessResponse and that
	 * it's not null.
	 */
	@Test
	public void testCreateProcessResponseNotNull() {

		ProcessResponse rsp = new ProcessResponse(200);
		assertNotNull(rsp);

	}


}
