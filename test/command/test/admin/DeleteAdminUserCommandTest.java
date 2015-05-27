package command.test.admin;

import static org.junit.Assert.*;

import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.admin.DeleteAdminUserCommand;
import command.ValidateException;

/**
 * Class used to test that DeleteUserCommand works
 * properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteAdminUserCommandTest {

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationnotNull() {
		DeleteAdminUserCommand c = new DeleteAdminUserCommand();
		assertNotNull(c);
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateEmptyClass() throws ValidateException {
		DeleteAdminUserCommand c = new DeleteAdminUserCommand();
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameLength() throws ValidateException {
		String uri = "/user/";
		for(int i = 0; i < MaxLength.USERNAME + 1; i++) {
			uri += "a";
		}
		Command c = new DeleteAdminUserCommand();
		c.setFields(uri, null, null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that validation returns true
	 * if everything is formatted properly.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {
		Command c = new DeleteAdminUserCommand();
		c.setFields("/test/username", null, null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {
		Command c = new DeleteAdminUserCommand();
		c.setFields("/test/username", null, null, UserType.ADMIN);
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
		Command c = new DeleteAdminUserCommand();
		c.setFields("/test/username", null, null, UserType.USER);
		c.validate();
	}

}
