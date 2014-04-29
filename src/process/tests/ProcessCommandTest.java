package process.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import org.junit.*;

import process.classes.ProcessCommand;

public class ProcessCommandTest {
	private ProcessCommand processHandler;
	private String process;
	private String param[];
	private String inFilePath;
	private String outFilePath;
	@Before
	public void setup() {
		processHandler = ProcessCommand.createProcessHandler();

	}

	@Test
	public void shouldCreateProcessHandler() {
		ProcessCommand processHandler = ProcessCommand.createProcessHandler();

	}

	@Test
	public void shouldNotBeNull() {
		assertNotNull(processHandler);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailIfProcessDoesntExist() {
		process = "hej";
		param = null;
		inFilePath = null;
		outFilePath = null;
		processHandler.runExecutable(process, param, inFilePath, outFilePath);

	}

	@Test
	public void executeScriptShouldReturnStringOnSuccess() {
		process = "rawToProfile";
		param = null;
		inFilePath = null;
		outFilePath = null;
		String result = processHandler.runExecutable(process, param, inFilePath, outFilePath);
		assertEquals("rawToProfile", result);
	}



}
