package command.test;

import static org.junit.Assert.*;
import org.junit.Test;
import command.ProcessCommand;
import command.ProcessStatus;

/**
 * Class used to test that ProcessStatus class works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class ProcessStatusTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		ProcessCommand cmd = new ProcessCommand();
		ProcessStatus c = new ProcessStatus(cmd);
		assertNotNull(c);

	}

}
