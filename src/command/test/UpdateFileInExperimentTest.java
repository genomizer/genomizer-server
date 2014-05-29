package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.UpdateFileInExperimentCommand;

/**
 * Class used to test that UpdateFileInExperiment works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class UpdateFileInExperimentTest {

	/**
	 * Test that creation works and object is not null.
	 */
	@Test
	public void testCreateNotNull() {

		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand();
		assertNotNull(c);

	}

	/**
	 * Test used to check that validate always returns true.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand();
		assertTrue(c.validate());
	}

}
