package response.test;

import static org.junit.Assert.*;

import org.junit.Test;

import response.DeleteGenomeReleaseResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to test the DeleteGenomeReleaseResponse class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseResponseTest {

	/**
	 * Test used to check that the created response is not null.
	 */
	@Test
	public void testCreateNotNull() {

		Response rsp = new DeleteGenomeReleaseResponse(StatusCode.OK);
		assertNotNull(rsp);

	}

	/**
	 * Test used to check that the created response with code
	 * is retrievable.
	 */
	@Test
	public void testGetResponseCode() {

		Response rsp = new DeleteGenomeReleaseResponse(StatusCode.OK);
		assertEquals(StatusCode.OK, rsp.getCode());

	}

}
