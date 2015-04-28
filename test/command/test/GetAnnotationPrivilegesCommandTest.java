package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
import org.junit.Test;
import command.GetAnnotationPrivilegesCommand;

/**
 * Class used to test that GetAnnotationPrivileges works
 * properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetAnnotationPrivilegesCommandTest {
	//TODO Implement tests when implementing the class

	/**
	 * Test creation and that it is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetAnnotationPrivilegesCommand c = new GetAnnotationPrivilegesCommand("", UserType.ADMIN);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		GetAnnotationPrivilegesCommand com = new GetAnnotationPrivilegesCommand("name", UserType.GUEST);
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

		GetAnnotationPrivilegesCommand com = new GetAnnotationPrivilegesCommand("name", UserType.UNKNOWN);
		com.validate();
		fail();
	}

}
