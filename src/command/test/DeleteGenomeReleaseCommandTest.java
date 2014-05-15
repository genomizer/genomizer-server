package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.DeleteGenomeReleaseCommand;

/**
 * Class used to test the DeleteGenomeReleaseCommand class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommandTest {

	/**
	 * Test used to check that creation works and
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		DeleteGenomeReleaseCommand cmd = new DeleteGenomeReleaseCommand();
		assertNotNull(cmd);

	}

	/**
	 * Method used to test the validate method.
	 */
	@Test
	public void testValidate() {

		fail("Not yet implemented");

	}

}
