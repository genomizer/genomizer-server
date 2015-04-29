package command.test;

import static org.junit.Assert.*;

import command.AddFileToExperimentCommand;
import command.Command;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
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
//	/**
//	 * Test that checks that ValidateException is thrown when
//	 * the label(header) is null.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidationLabelNull() throws ValidateException {
//
//		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand(null);
//		c.validate();
//
//		fail("Expected ValidateException.");
//
//	}

//	/**
//	 * Test that checks that ValidationException is thrown when
//	 * the label(header) is an empty string.
//	 *
//	 * @throws ValidateException
//	 */
//	@Test(expected = ValidateException.class)
//	public void testValidationLabelEmptyString() throws ValidateException {
//
//		DeleteAnnotationFieldCommand c = new DeleteAnnotationFieldCommand("");
//		c.validate();
//
//		fail("Expected ValidateException.");
//
//	}

	/**
	 * Test that checks that ValidateException is thrown when
	 * the label(header) length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationLabelLength() throws ValidateException {
		String uri = "/annotation/field/";
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			uri += "a";
		}

		Command c = new DeleteAnnotationFieldCommand();
		c.setFields(uri, null, UserType.ADMIN);
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

		String uri = "/annotation/field/great";
		Command c = new DeleteAnnotationFieldCommand();
		c.setFields(uri, null, UserType.ADMIN);

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

		String uri = "/annotation/field/great";
		Command c = new DeleteAnnotationFieldCommand();
		c.setFields(uri, null, UserType.USER);

		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		String uri = "/annotation/field/great";
		Command c = new DeleteAnnotationFieldCommand();
		c.setFields(uri, null, UserType.GUEST);

		c.validate();
		fail();
	}
}
