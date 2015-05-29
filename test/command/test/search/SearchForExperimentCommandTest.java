package command.test.search;

import static org.junit.Assert.*;

import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Ignore;
import org.junit.Test;
import command.search.SearchCommand;

import java.util.HashMap;

/**
 * Test class used to check that the SearchForExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class SearchForExperimentCommandTest {


	/**
	 * Test used to check that no validateException is thrown and
	 * that everything works properly if everything is correctly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void textValidateProperlyFormatted() throws ValidateException {
		Command c = new SearchCommand();
		c.setFields("Hello", new HashMap<String, String>(), null, UserType.ADMIN);
		c.validate();
		assertTrue(true);
	}

	//TODO Add this test again after custom regex has been implemented.


	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = new SearchCommand();
		c.setFields("uri", new HashMap<String, String>(), null, UserType.GUEST);
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

		Command c = new SearchCommand();
		c.setFields("uri", new HashMap<String, String>(), null, UserType.UNKNOWN);
		c.validate();
		fail();
	}

}
