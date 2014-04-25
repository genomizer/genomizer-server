package process.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.*;

import process.classes.ProcessHandler;

public class ProcessHandlerTest {
	private ProcessHandler processHandler;

	@Before
	public void setup() {
		processHandler = ProcessHandler.createProcessHandler();

	}

	@Test
	public void shouldCreateProcessHandler() {
		ProcessHandler processHandler = ProcessHandler.createProcessHandler();
	
	}

	@Test
	public void shouldNotBeNull() {
		assertNotNull(processHandler);
	}

	@Test
	public void shouldFailIfProcessDoesntExist() {
		

	}

}
