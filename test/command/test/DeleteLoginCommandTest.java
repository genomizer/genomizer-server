package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.connection.DeleteLoginCommand;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.ValidateException;

/**
 * Class used to test that the DeleteLoginCommand class
 * works properly. The execute method is tested elsewhere.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteLoginCommandTest {

	/**
	 * Test used to check that ValidateException is not thrown when
	 * username is valid.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void ShouldValidateCorrectUuid() throws ValidateException {
		Command c = new DeleteLoginCommand();
		c.setFields(null, null, "userName", UserType.ADMIN);
		c.validate();

		assertTrue(true);
	}


	/**
	 * Test used to check that ValidateException is thrown when
	 * username length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidMaxUsernameLength() throws ValidateException {

		String uuid = "";
		for(int i = 0; i < MaxLength.USERNAME + 1; i++) {
			uuid = uuid + "a";
		}
		Command c = new DeleteLoginCommand();
		c.setFields(null, null, uuid, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is null
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUuidNull() throws ValidateException {
		Command c = new DeleteLoginCommand();
		c.setFields(null, null, null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is an empty string
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUuidEmptyString() throws ValidateException {
		Command c = new DeleteLoginCommand();
		c.setFields(null, null, "", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}


	/**
	 * Test used to check that ValidateException is thrown when
	 * username is an empty string
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidCharactersUuid() throws ValidateException {
		Command c = new DeleteLoginCommand();
		c.setFields(null, null, "��!?,:;[]{}", UserType.ADMIN);
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

		Command c = new DeleteLoginCommand();
		c.setFields(null, null, "properly", UserType.ADMIN);
		c.validate();

		assertTrue(true);
	}

}
