package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.AddGenomeReleaseCommand;

/**
 * Class used to test the AddGenomeRelease class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommandTest {

	/**
	 * Method used to check that the created command
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		AddGenomeReleaseCommand cmd = new AddGenomeReleaseCommand();
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
