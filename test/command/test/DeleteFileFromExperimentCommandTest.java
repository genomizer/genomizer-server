package command.test;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.DeleteFileFromExperimentCommand;
import command.ValidateException;

/**
 * Class used to test that DelteFileFromExperimentCommand
 * class works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteFileFromExperimentCommandTest {

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand("a", UserType.ADMIN);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown if the
	 * header is null.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdNull() throws ValidateException {

		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand(null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown if the
	 * file experiment id is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdEmptyString() throws ValidateException {

		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand("", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown if the
	 * file experiment id length is to long.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand(big, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that no validateException is thrown and
	 * that everything works properly if everything is correctly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void textValidateProperlyFormatted() throws ValidateException {

		DeleteFileFromExperimentCommand c = new DeleteFileFromExperimentCommand("Hello", UserType.ADMIN);
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

		DeleteFileFromExperimentCommand com = new DeleteFileFromExperimentCommand("string", UserType.USER);
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

		DeleteFileFromExperimentCommand com = new DeleteFileFromExperimentCommand("string", UserType.GUEST);
		com.validate();
		fail();
	}

}
