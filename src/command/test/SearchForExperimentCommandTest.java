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
		command = (SearchForExperimentsCommand) factory.createSearchForExperimentCommand("/search/annotations=123[expId]");
	}

	@Test
	public void shouldCreateSearchCommand() {
		assertNotNull(command);
	}

	@Test
	public void shouldParseSearchString() throws Exception {
		assertEquals("123[expId]",command.getAnnotations());
	}

	@Test
	public void shouldTryAccessDB() throws Exception {
		Response res = command.execute();
		System.out.println(res.getCode());
	}
}
