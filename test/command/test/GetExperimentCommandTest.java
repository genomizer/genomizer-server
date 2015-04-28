package command.test;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
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
	 * Test used to check that creation of object works
	 * and is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetExperimentCommand c = new GetExperimentCommand("Exp-id", UserType.ADMIN);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment-id is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdNotNull() throws ValidateException {

		GetExperimentCommand c = new GetExperimentCommand(null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when experiment-id is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdEmptyString() throws ValidateException {

		GetExperimentCommand c = new GetExperimentCommand("", UserType.ADMIN);
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

		String big = "";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			big = big + "a";
		}
		GetExperimentCommand c = new GetExperimentCommand(big, UserType.ADMIN);
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

		GetExperimentCommand c = new GetExperimentCommand("properly", UserType.ADMIN);
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

		GetExperimentCommand com = new GetExperimentCommand("properly", UserType.GUEST);
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

		GetExperimentCommand com = new GetExperimentCommand("properly", UserType.UNKNOWN);
		com.validate();
		fail();
	}

}
