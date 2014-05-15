package response.test;

import static org.junit.Assert.*;

import org.junit.Test;

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

	/**
	 * Test used to check that the created response is not null.
	 */
	@Test
	public void testCreateNotNull() {

		Response rsp = new AddGenomeReleaseResponse(StatusCode.OK);
		assertNotNull(rsp);

	}

	/**
	 * Test used to check that the created response with code
	 * is retrievable.
	 */
	@Test
	public void testGetResponseCode() {

		Response rsp = new AddGenomeReleaseResponse(StatusCode.OK);
		assertEquals(StatusCode.OK, rsp.getCode());

	}

}
