package server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import response.Response;

import command.Command;
import command.CommandHandler;
import command.GetFileFromExperimentCommand;

public class GetFileFromExperimentCommandTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void printResults() {

		Response rsp;

		CommandHandler cmdh = new CommandHandler();
		String[] rest = cmdh.parseRest("GET /file/123");
		Command cmd = new GetFileFromExperimentCommand(rest);
		cmd.validate();
		rsp = cmd.execute();
	}

}
