package command.test;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.LogoutCommand;
import command.ValidateException;

/**
 * Class used to test that the LogoutCommand class
 * works properly. The execute method is tested elsewhere.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class LogoutCommandTest {

//	/**
//	 * Method used to test creation and that object
//	 * is not null.
//	 */
//	@Test
//	public void testCreateNotNull() {
//
//		LogoutCommand c = new LogoutCommand("Username");
//
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that ValidateExpception is thrown when
//	 * username is an empty string.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateUsernameEmptyString() throws ValidateException {
//
//		LogoutCommand c = new LogoutCommand("");
//		c.validate();
//
//		fail("Expected ValidateException to be thrown.");
//
//	}
//
//	/**
//	 * Test used to check that ValidateException is thrown when
//	 * username is null.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidateUsernameNull() throws ValidateException {
//
//		LogoutCommand c = new LogoutCommand(null);
//		c.validate();
//
//		fail("Expected ValidateException to be thrown.");
//
//	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameLength() throws ValidateException {

		String uuid = "";
		for(int i = 0; i < MaxLength.USERNAME + 1; i++) {
			uuid = uuid + "a";
		}
		Command c = new LogoutCommand();
		c.setFields(null, uuid, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown when
	 * everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		Command c = new LogoutCommand();
		c.setFields(null, "properly", UserType.ADMIN);
		c.validate();

		assertTrue(true);
	}

}
