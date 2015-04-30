package command.test;

import static org.junit.Assert.*;

import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import command.SearchForExperimentsCommand;

/**
 * Test class used to check that the SearchForExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class SearchForExperimentCommandTest {

	/**
	 * Test used to check that ValidateException is thrown if the
	 * file experiment id length is to long.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileExpIdLength() throws ValidateException {

		String uri = "zz";
		for(int i = 0; i < MaxLength.ANNOTATION_VALUE + 1; i++) {
			uri  += "a";
		}

		Command c = new SearchForExperimentsCommand();
		c.setFields(uri, null, UserType.ADMIN);
		c.validate();
		fail("Expected ValidateException.");
	}

	/**
	 * Test used to check that no validateException is thrown and
	 * that everything works properly if everything is correctly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void textValidateProperlyFormatted() throws ValidateException {
		Command c = new SearchForExperimentsCommand();
		c.setFields("Hello", null, UserType.ADMIN);
		c.validate();
		assertTrue(true);
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when invalid characters are used
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateIncorrectlyFormatted() throws ValidateException {

		Command c = new SearchForExperimentsCommand();
		c.setFields("uri��", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}
	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new SearchForExperimentsCommand();
		c.setFields("uri", null, UserType.GUEST);
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

		Command c = new SearchForExperimentsCommand();
		c.setFields("uri", null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

}
