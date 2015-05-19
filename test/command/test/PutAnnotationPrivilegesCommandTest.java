package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import command.annotation.*;
import database.subClasses.UserMethods.UserType;
import org.junit.Test;

/**
 * Class used to test that PutAnnotationPrivilegesCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class PutAnnotationPrivilegesCommandTest {
	//TODO Implement tests when the class is implemented
//
//	/**
//	 * Test used to check that creation works and object
//	 * is not null.
//	 */
//	@Test
//	public void testCreationNotNull() {
//
//		PutAnnotationPrivilegesCommand c = new PutAnnotationPrivilegesCommand("", "");
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
//		PutAnnotationPrivilegesCommand c = new PutAnnotationPrivilegesCommand("", "");
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

		PutAnnotationPrivilegesCommand com = new PutAnnotationPrivilegesCommand();
		com.setFields("json", "", "string", UserType.USER);
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

		PutAnnotationPrivilegesCommand com = new PutAnnotationPrivilegesCommand();
		com.setFields("name", "", "string", UserType.GUEST);
		com.validate();
		fail();
	}


}
