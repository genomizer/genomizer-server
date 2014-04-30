package server.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.CommandHandler;


public class CommandHandlerTest {

	@Test
	public void testParseRest() {
		CommandHandler cmd = new CommandHandler();
		String[] parsedRest = cmd.parseRest("/file/123/abc");
	}

}
