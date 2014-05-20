package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.GetExperimentCommand;

/**
 * Test class used to check that GetExperimentCommand works
 * properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class GetExperimentCommandTest {

	/**
	 * Test that checks that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetExperimentCommand cmd = new GetExperimentCommand("ExpId");
		assertNotNull(cmd);

	}

	/**
	 * Test that checks that validation is always true.
	 */
	@Test
	public void testValidationAlwaysTrue() {

		GetExperimentCommand cmd = new GetExperimentCommand("ExpId");
		assertTrue(cmd.validate());

	}

}
