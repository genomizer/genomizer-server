package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.DeleteExperimentCommand;

/**
 * Class used to test that the DeleteExperimentCommand class works
 * properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteExperimentCommandTest {

	/**
	 * Test that checks that creation works and is
	 * not null.
	 */
	@Test
	public void testCreateNotNull() {

		DeleteExperimentCommand c = new DeleteExperimentCommand("a");
		assertNotNull(c);

	}

	/**
	 * Test that checks that validate returns false if
	 * the header is null.
	 */
	@Test
	public void testValidateNullHeader() {

		DeleteExperimentCommand c = new DeleteExperimentCommand(null);
		assertFalse(c.validate());

	}

	/**
	 * Test that checks that validate returns true if
	 * everything is formatted properly.
	 */
	@Test
	public void testValidateProperlyFormatted() {

		DeleteExperimentCommand c = new DeleteExperimentCommand("properly");
		assertTrue(c.validate());

	}

}
