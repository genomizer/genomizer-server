package command.test;

import static org.junit.Assert.*;

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
	//TODO Implement tests later

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		UpdateAnnotationPrivilegesCommand c = new UpdateAnnotationPrivilegesCommand("", "");
		assertNotNull(c);

	}

	/**
	 * Test used to check that validate always returns true.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		UpdateAnnotationPrivilegesCommand c = new UpdateAnnotationPrivilegesCommand("", "");
		c.validate();

	}

}
