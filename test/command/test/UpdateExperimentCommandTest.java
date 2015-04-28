package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import database.subClasses.UserMethods.UserType;
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
	//TODO Implement more tests

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		UpdateExperimentCommand c = new UpdateExperimentCommand("", "", UserType.ADMIN);
		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws command.ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		UpdateExperimentCommand com = new UpdateExperimentCommand("", "", UserType.USER);
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

		UpdateExperimentCommand com = new UpdateExperimentCommand("", "", UserType.GUEST);
		com.validate();
		fail();
	}

}
