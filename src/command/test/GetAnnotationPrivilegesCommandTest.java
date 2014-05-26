package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.GetAnnotationPrivilegesCommand;

/**
 * Class used to test that GetAnnotationPrivileges works
 * properly.
 *
 * @author tfy09jnn
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
	 * Test used to check that validation is always true.
	 * Currently there are no other cases.
	 */
	@Test
	public void testvalidationAlwaysTrue() {
		GetAnnotationPrivilegesCommand c = new GetAnnotationPrivilegesCommand();
		assertTrue(c.validate());
	}

}
