package command.test;

import static org.junit.Assert.*;

import command.Process;
import command.process.PutProcessCommand;
import org.junit.Test;

import java.util.UUID;

/**
 * Class used to test that Process class works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class ProcessTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		PutProcessCommand cmd = new PutProcessCommand();
		cmd.setPID(UUID.randomUUID());
		Process c = new Process(cmd);
		assertNotNull(c);
	}

}
