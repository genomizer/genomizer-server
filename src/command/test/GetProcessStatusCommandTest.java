package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.WorkHandler;

public class GetProcessStatusCommandTest {

	private static WorkHandler workHandler = new WorkHandler();

	@Before
	public void setUp() throws Exception {
		workHandler.addWork(new ProcessCommandMock());
	}

	@Test
	public void shouldContainStuff() {
		workHandler.getProcessStatus();

	}

}
