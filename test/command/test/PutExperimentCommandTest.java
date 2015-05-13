package command.test;

import static org.junit.Assert.*;

import command.Command;

import command.ValidateException;
import command.experiment.PutExperimentCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;

/**
 * Class used to check that PutExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class PutExperimentCommandTest {
//	//TODO Implement tests later
//
//	/**
//	 * Test used to check that creation works and object
//	 * is not null.
//	 */
//	@Test
//	public void testCreationNotNull() {
//
//		PutExperimentCommand c = new PutExperimentCommand("", "");
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that validate always returns true.
//	 */
//	@Test
//	public void testValidateAlwaysTrue() {
//
//		PutExperimentCommand c = new PutExperimentCommand("", "");
//		c.validate();
//
//	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws command.ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new PutExperimentCommand();
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

		Command c = new PutExperimentCommand();
		c.setFields("uri", null, UserType.GUEST);
		c.validate();
		fail();
	}

}
