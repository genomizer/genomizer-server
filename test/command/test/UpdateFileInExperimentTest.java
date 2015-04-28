package command.test;

import static org.junit.Assert.*;

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
//TODO Implement tests later

	/**
	 * Test that creation works and object is not null.
	 */
	@Test
	public void testCreateNotNull() {
		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand("","", UserType.ADMIN);
		assertNotNull(c);

	}

	/**
	 * Test used to check that validate always returns true.
	 */
	@Test
	public void testValidateAlwaysTrue() throws ValidateException {
		UpdateFileInExperimentCommand c = new UpdateFileInExperimentCommand("","",UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		UpdateFileInExperimentCommand com = new UpdateFileInExperimentCommand("name", "string", UserType.USER);
		com.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		UpdateFileInExperimentCommand com = new UpdateFileInExperimentCommand("name", "string", UserType.GUEST);
		com.validate();
		fail();
	}

}
