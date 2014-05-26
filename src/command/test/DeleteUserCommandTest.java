package command.test;

import static org.junit.Assert.*;
import org.junit.Test;

import command.DeleteUserCommand;

/**
 * Class used to test that DeleteUserCommand works
 * properly.
 *
 * @author tfy09jnn
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
	 * Test used to check that validation returns false if
	 * username is null.
	 */
	@Test
	public void testValidateUserNameNotNull() {
		DeleteUserCommand c = new DeleteUserCommand(null);
		assertFalse(c.validate());
	}

	/**
	 * Test used to check that validation returns true
	 * if everything is formatted properly.
	 */
	@Test
	public void testValidateProperlyFormatted() {
		DeleteUserCommand c = new DeleteUserCommand("username");
		assertTrue(c.validate());

	}

}
