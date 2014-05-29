package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.GetGenomeReleaseCommand;

/**
 * Test used to check that GetGenomeReleaseCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetGenomeReleaseCommandTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetGenomeReleaseCommand c = new GetGenomeReleaseCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when calling validate method.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		GetGenomeReleaseCommand c = new GetGenomeReleaseCommand();
		c.validate();

		assertTrue(true);

	}

}
