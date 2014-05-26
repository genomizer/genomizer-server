package command.test;

import static org.junit.Assert.*;
import org.junit.Test;

import command.DeleteFileFromExperimentCommand;

/**
 * Class used to test that DelteFileFromExperimentCommand
 * class works properly.
 * 
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteFileFromExperimentCommandTest {

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {
		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand("a");
		assertNotNull(c);
	}
	
	/**
	 * Test used to check that validation always returns true.
	 */
	@Test
	public void testValidateTrue() {
		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand(null);
		assertTrue(c.validate());
		
	}

}
