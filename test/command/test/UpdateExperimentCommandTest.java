package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.UpdateExperimentCommand;

/**
 * Class used to check that UpdateExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class UpdateExperimentCommandTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		UpdateExperimentCommand c = new UpdateExperimentCommand();
		assertNotNull(c);

	}

	/**
	 * Test used to check that validate always returns true.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		UpdateExperimentCommand c = new UpdateExperimentCommand();
		assertTrue(c.validate());

	}

}
