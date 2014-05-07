package command.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import command.CommandHandler;

public class CommandHandlerTest {

	@Test
	public void testProcessNewCommand() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseRest() {
		
		CommandHandler handler = new CommandHandler();
		String rest = handler.parseRest("/search/annotations=abc123");
		assertEquals("annotations=abc123", rest);
		
		rest = handler.parseRest("/file/expId=2/fileId=1");
		assertEquals("fileId=1", rest);

	}

}
