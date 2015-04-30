package command.test;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
import org.junit.Test;
import command.GetExperimentCommand;
import command.ValidateException;

/**
 * Test used to check that GetExperimentCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetExperimentCommandTest {

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment is an empty string
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidateEmptyString() throws ValidateException {

		String uri = "//";
		//TODO FIX THIS TEST
		Command c = new GetExperimentCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment-id value is missing
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidateValueMissing() throws ValidateException {

		String uri = "/experiment/";
	//TODO FIX THIS TEST
		Command c = new GetExperimentCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment-id length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdLength() throws ValidateException {

		String uri = "/experiment/";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			uri += "a";
		}
		Command c = new GetExperimentCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment-id characters are invalid
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidCharacters() throws ValidateException {

		String uri = "/experiment/��!?,:;[]{}";

		Command c = new GetExperimentCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * if everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		Command c = new GetExperimentCommand();
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

		Command c = new GetExperimentCommand();
		c.setFields("/experiment/properly", null, UserType.GUEST);
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

		Command c = new GetExperimentCommand();
		c.setFields("/experiment/properly", null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

}
