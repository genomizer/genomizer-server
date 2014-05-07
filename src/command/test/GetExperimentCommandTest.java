package command.test;

import static org.junit.Assert.*;


import org.junit.Test;

import response.Response;

import command.CommandHandler;
import command.GetExperimentCommand;

public class GetExperimentCommandTest {

	@Test
	public void testAnnotations() {
		CommandHandler handler = new CommandHandler();
		GetExperimentCommand cmd = new GetExperimentCommand(handler.parseRest("/experiment/abc"));
		Response rsp = cmd.execute();
	}

}
