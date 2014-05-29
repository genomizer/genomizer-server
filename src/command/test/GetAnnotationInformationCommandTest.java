package command.test;

import static org.junit.Assert.*;
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

		GetAnnotationInformationCommand c = new GetAnnotationInformationCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when calling validate.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		GetAnnotationInformationCommand c = new GetAnnotationInformationCommand();
		c.validate();

		assertTrue(true);

	}

}
