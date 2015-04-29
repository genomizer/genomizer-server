package command.test;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.DeleteExperimentCommand;
import command.ValidateException;

/**
 * Class used to test that the DeleteExperimentCommand class works
 * properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteExperimentCommandTest {

//	/**
//	 * Test that checks that ValidateException is thrown when
//	 * the experiment-id is null.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateNullExpId() throws ValidateException {
//
//		DeleteExperimentCommand c = new DeleteExperimentCommand(null);
//		c.validate();
//
//		fail("Expected ValidateException.");
//
//	}

//	/**
//	 * Test that checks that ValidateException is thrown when
//	 * experiment-id is an empty string.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateExpIdEmptyString() throws ValidateException {
//
//		DeleteExperimentCommand c = new DeleteExperimentCommand("");
//		c.validate();
//
//		fail("Expected ValidateException.");
//
//	}

	/**
	 * Method used to check that a ValidateException is thrown
	 * when the experiment-id is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdLength() throws ValidateException {
		String uri = "/experiment/";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			uri += "a";
		}
		Command c = new DeleteExperimentCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();
		fail("Expected ValidateException.");
	}

	/**
	 * Test that checks that no ValidateException is thrown
	 * when everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {
		Command c = new DeleteExperimentCommand();
		c.setFields("/experiment/properly", null, UserType.ADMIN);
		c.validate();
		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new DeleteExperimentCommand();
		c.setFields("/experiment/properly", null, UserType.USER);
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

		Command c = new DeleteExperimentCommand();
		c.setFields("/experiment/properly", null, UserType.GUEST);
		c.validate();
		fail();
	}

}
