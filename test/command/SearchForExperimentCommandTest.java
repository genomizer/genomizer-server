package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import command.CommandFactory;
import command.SearchForExperimentsCommand;

/**
 * Test class used to check that the SearchForExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class SearchForExperimentCommandTest {

	private SearchForExperimentsCommand command;

	@Before
	public void setup() {

		CommandFactory factory = new CommandFactory();
		command = (SearchForExperimentsCommand) factory.createSearchForExperimentCommand("/search/annotations=Exp1[ExpID]");

	}

	/**
	 * Test used to check that a search command object can
	 * be created and is not null.
	 */
	@Test
	public void shouldCreateSearchCommand() {

		assertNotNull(command);

	}

	/**
	 * Method used to check that strings are equal.
	 *
	 * @throws Exception
	 */
	@Test
	public void shouldParseSearchString() throws Exception {

		assertEquals("Exp1[ExpID]",command.getAnnotations());

	}

}
