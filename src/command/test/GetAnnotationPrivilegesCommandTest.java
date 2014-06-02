package command.test;

import static org.junit.Assert.*;
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

	/**
	 * Test creation and that it is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetAnnotationPrivilegesCommand c = new GetAnnotationPrivilegesCommand();

		assertNotNull(c);

	}
	/**
	 * Test used to check that ValidateException is not
	 * thrown when calling validate.
	 */
	@Test
	public void testvalidationAlwaysTrue() {

		GetAnnotationPrivilegesCommand c = new GetAnnotationPrivilegesCommand();
		c.validate();

		assertTrue(true);

	}

}
