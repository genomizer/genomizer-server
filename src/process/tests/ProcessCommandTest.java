package process.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.*;

import process.classes.ProcessCommand;

public class ProcessCommandTest {
	private ProcessCommand processCommand;
	private String process;
	private String param[];
	private String inFilePath;
	private String outFilePath;
	
	@Before
	public void setup() {
		processCommand = new ProcessCommand("rawToProfile", "");

	}

	@Test
	public void shouldCreateProcessHandler() {

	}

	@Test
	public void shouldNotBeNull() {
		assertNotNull(processCommand);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfProcessDoesntExist() {
		process = "hej";
		param = null;
		inFilePath = null;
		outFilePath = null;
		processCommand.execute();

	}

	@Test
	public void executeScriptShouldReturnStringOnSuccess() {
		process = "rawToProfile";
		param = null;
		inFilePath = null;
		outFilePath = null;
		String result = processCommand.execute();
		assertEquals("rawToProfile", result);
	}



}
