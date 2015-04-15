package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.CommandHandler;

/**
 * Testclass used to test the CommandHandler class.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class CommandHandlerTest {

	/**
	 * Test method used to test creation of the class.
	 */
	@Test
	public void testCreateClass() {

		CommandHandler cmdh = new CommandHandler();
		assertNotNull(cmdh);

	}

	/**
	 * Test used to check that the ParseRest method works properly.
	 */
	@Test
	public void testParseRest() {

		CommandHandler handler = new CommandHandler();
		String rest = handler.parseRest("/search/annotations=abc123");
		assertEquals("annotations=abc123", rest);

		rest = handler.parseRest("/file/expId=2/fileId=1");
		assertEquals("fileId=1", rest);

	}

}
