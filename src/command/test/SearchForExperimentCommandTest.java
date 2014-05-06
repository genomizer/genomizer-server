package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import command.CommandFactory;
import command.CommandHandler;
import command.SearchForExperimentsCommand;

public class SearchForExperimentCommandTest {

	private SearchForExperimentsCommand command;

	@Before
	public void setup() {
		CommandHandler handler = new CommandHandler();
		String[] rests = handler.parseRest("/search/annotations=123[expId]");
		for (String s : rests) {
			System.out.println(s);
		}
		System.out.println("----------------");
		CommandFactory factory = new CommandFactory();
		command = (SearchForExperimentsCommand) factory.createSearchForExperimentCommand("", rests);
	}

	@Test
	public void shouldCreateSearchCommand() {
		assertNotNull(command);
	}

	@Test
	public void shouldParseSearchString() throws Exception {
		assertEquals("annotations=123[expId]",command.getAnnotations());
	}

	@Test
	public void shouldTryAccessDB() throws Exception {
		command.execute();
	}
}
