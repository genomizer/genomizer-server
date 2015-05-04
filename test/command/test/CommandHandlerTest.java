package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.CommandHandler;
import server.ProcessPool;

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

		CommandHandler cmdh = new CommandHandler(new ProcessPool());
		assertNotNull(cmdh);

	}
}
