package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.DeleteUserCommand;
import command.ValidateException;

/**
 * Class used to test that DeleteUserCommand works
 * properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteUserCommandTest {

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationnotNull() {

		DeleteUserCommand c = new DeleteUserCommand("username");

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUserNameNotNull() throws ValidateException {

		DeleteUserCommand c = new DeleteUserCommand(null);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameEmptyString() throws ValidateException {

		DeleteUserCommand c = new DeleteUserCommand("");
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.USERNAME + 1; i++) {
			big = big + "a";
		}
		DeleteUserCommand c = new DeleteUserCommand(big);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that validation returns true
	 * if everything is formatted properly.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		DeleteUserCommand c = new DeleteUserCommand("username");
		c.validate();

		assertTrue(true);

	}

}
