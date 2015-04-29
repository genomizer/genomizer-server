package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
import org.junit.Test;
import command.UpdateAnnotationPrivilegesCommand;

/**
 * Class used to test that UpdateAnnotationPrivilegesCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class UpdateAnnotationPrivilegesCommandTest {
	//TODO Implement tests when the class is implemented

@Ignore
public class UpdateAnnotationPrivilegesCommandTest {
//	//TODO Implement tests later
//
//	/**
//	 * Test used to check that creation works and object
//	 * is not null.
//	 */
//	@Test
//	public void testCreationNotNull() {
//
//		UpdateAnnotationPrivilegesCommand c = new UpdateAnnotationPrivilegesCommand("", "");
//		assertNotNull(c);
//
//	}
//
//	/**
//	 * Test used to check that validate always returns true.
//	 */
//	@Test
//	public void testValidateAlwaysTrue() {
//
//		UpdateAnnotationPrivilegesCommand c = new UpdateAnnotationPrivilegesCommand("", "");
//		c.validate();
//
//	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		UpdateAnnotationPrivilegesCommand com = new UpdateAnnotationPrivilegesCommand("json", "string", UserType.USER);
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

		UpdateAnnotationPrivilegesCommand com = new UpdateAnnotationPrivilegesCommand("name", "string", UserType.GUEST);
		com.validate();
		fail();
	}


}
