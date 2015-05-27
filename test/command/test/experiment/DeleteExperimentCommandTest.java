package command.test.experiment;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.experiment.DeleteExperimentCommand;
import command.ValidateException;

/**
 * Class used to test that the DeleteExperimentCommand class works
 * properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteExperimentCommandTest {

	/**
	 * Method used to check that a ValidateException is thrown
	 * when the experiment-id is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpInvalidIdLength() throws ValidateException {
		String uri = "/experiment/";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			uri += "a";
		}
		Command c = new DeleteExperimentCommand();
		c.setFields(uri, null, null, UserType.ADMIN);
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
		c.setFields("/experiment/properly", null, null, UserType.ADMIN);
		c.validate();
		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when invalid characters are used
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateIncorrectlyFormatted() throws ValidateException {

		Command c = new DeleteExperimentCommand();
		c.setFields("/experiment/��", null, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
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
		c.setFields("/experiment/properly", null, null, UserType.USER);
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
		c.setFields("/experiment/properly", null, null, UserType.GUEST);
		c.validate();
		fail();
	}

}
