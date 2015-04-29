package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
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

//	private SearchForExperimentsCommand command;
//
//	@Before
//	public void setup() {
//
//		CommandFactory factory = new CommandFactory();
//		command = (SearchForExperimentsCommand) factory.createSearchForExperimentCommand("/search/annotations=Exp1[ExpID]");
//
//	}
//
//	/**
//	 * Test used to check that a search command object can
//	 * be created and is not null.
//	 */
//	@Test
//	public void shouldCreateSearchCommand() {
//
//		assertNotNull(command);
//
//	}
//
//	/**
//	 * Method used to check that strings are equal.
//	 *
//	 * @throws Exception
//	 */
//	@Test
//	public void shouldParseSearchString() throws Exception {
//
//		assertEquals("Exp1[ExpID]",command.getAnnotations());
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

		SearchForExperimentsCommand com = new SearchForExperimentsCommand("string", UserType.GUEST);
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

		SearchForExperimentsCommand com = new SearchForExperimentsCommand("string", UserType.UNKNOWN);
		com.validate();
		fail();
	}

}
