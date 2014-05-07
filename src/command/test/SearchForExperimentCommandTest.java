package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import response.Response;

import command.CommandFactory;
import command.CommandHandler;
import command.SearchForExperimentsCommand;

public class SearchForExperimentCommandTest {

	private SearchForExperimentsCommand command;

	@Before
	public void setup() {
		CommandFactory factory = new CommandFactory();
		command = (SearchForExperimentsCommand) factory.createSearchForExperimentCommand("/search/annotations=Exp1[ExpID]");
	}

	@Test
	public void shouldCreateSearchCommand() {
		assertNotNull(command);
	}

	@Test
	public void shouldParseSearchString() throws Exception {
		assertEquals("Exp1[ExpID]",command.getAnnotations());
	}

	@Test
	public void shouldTryAccessDB() throws Exception {
		Response res = command.execute();
	}
}
