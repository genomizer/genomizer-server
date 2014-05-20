package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.GetAnnotationInformationCommand;

/**
 * Test class for GetAnnotationInformationCommand class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class GetAnnotationInformationCommandTest {

	/**
	 * Test that checks that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetAnnotationInformationCommand cmd = new GetAnnotationInformationCommand();
		assertNotNull(cmd);

	}

	/**
	 * Test that checks that validation always returns true.
	 */
	@Test
	public void testValidationTrue() {

		GetAnnotationInformationCommand cmd = new GetAnnotationInformationCommand();
		assertTrue(cmd.validate());

	}

}
