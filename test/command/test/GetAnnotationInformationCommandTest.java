package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;
import command.GetAnnotationInformationCommand;

/**
 * Class used to test that GetAnnotationInformationCommand
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetAnnotationInformationCommandTest {

	/**
	 * Test that checks that creation works and that the
	 * created object is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetAnnotationInformationCommand c = new GetAnnotationInformationCommand(UserType.ADMIN);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when calling validate.
	 */
	@Test
	public void testValidateAlwaysTrue() throws ValidateException {

		GetAnnotationInformationCommand c = new GetAnnotationInformationCommand(UserType.ADMIN);
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

		GetAnnotationInformationCommand com = new GetAnnotationInformationCommand(UserType.GUEST);
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

		GetAnnotationInformationCommand com = new GetAnnotationInformationCommand(UserType.UNKNOWN);
		com.validate();
		fail();
	}

}
