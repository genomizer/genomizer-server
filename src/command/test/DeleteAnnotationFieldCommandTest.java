package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.DeleteAnnotationFieldCommand;
import command.ValidateException;

/**
 * Class used to test that the DeleteAnnotationFieldCommand
 * class works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteAnnotationFieldCommandTest {

	/**
	 * Test that checks that creation works and is not null.
	 */
	@Test
	public void testCreationNotNull() {

		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("a");

		assertNotNull(c);

	}

	/**
	 * Test that checks that ValidateException is thrown when
	 * the label(header) is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationLabelNull() throws ValidateException {

		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand(null);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that ValidationException is thrown when
	 * the label(header) is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationLabelEmptyString() throws ValidateException {

		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("");
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that ValidateException is thrown when
	 * the label(header) length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationLabelLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.ANNOTATION_LABEL + 1; i++) {
			big = big + "a";
		}
		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand(big);
		c.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that no ValidateException is thrown when everything
	 * is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidationProperlyFormatted() throws ValidateException {

		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("great");
		c.validate();

		assertTrue(true);
	}

}
