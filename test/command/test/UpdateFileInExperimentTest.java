package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
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
////TODO Implement tests later
//
//	/**
//	 * Test that creation works and object is not null.
//	 */
//	@Test
//	public void testCreateNotNull() {
//		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand("","");
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that validate always returns true.
//	 */
//	@Test
//	public void testValidateAlwaysTrue() {
//		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand("","");
//		c.validate();
//	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new UpdateFileInExperimentCommand();
		c.setFields("uri", null, UserType.USER);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		Command c = new UpdateFileInExperimentCommand();
		c.setFields("uri", null, UserType.GUEST);
		c.validate();
		fail();
	}

}
