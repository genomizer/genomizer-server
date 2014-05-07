package command.test;

import static org.junit.Assert.*;

import org.junit.*;

import response.Response;

import authentication.Authenticate;

import command.Command;
import command.CommandFactory;
import command.ProcessCommand;

public class CreateProcessCommandTest {

	CommandFactory cmdf;
	ProcessCommand processCommand;
	String uuid;

	@Before
	public void setup(){

		cmdf = new CommandFactory();



		String json = "{" +
							"\"filename\": \"fileNAME66\"," +
							"\"filepath\": \"path/to/local/file\"," +
							"\"expid\": \"Exp1\"," +
							"\"processtype\": \"rawtoprofile\"," +
							"\"parameters\": [" +
												"\"param1\"," +
												"\"param2\"," +
												"\"param3\"," +
												"\"param4\"" +
											"]," +
							"\"metadata\": \"astringofmetadata\"," +
							"\"genomeRelease\": \"hg38\", " +
							"\"author\": \"yuri\"}";

		processCommand = (ProcessCommand)cmdf.createProcessCommand(json, "splutt");
	}

	@Test
	public void shouldInitiateProcessCommand(){
		assertNotNull(processCommand);
	}
	@Test
	public void shouldSetMetadata(){
		assertEquals("astringofmetadata", processCommand.getMetadata());
	}

	@Test
	public void shouldAddFourParameters(){
		assertEquals(4,processCommand.getParameters().length);
	}

	@Test
	public void shouldSetParameters(){
		assertEquals("param1",(processCommand.getParameters())[0]);
		assertEquals("param2",(processCommand.getParameters())[1]);
		assertEquals("param3",(processCommand.getParameters())[2]);
		assertEquals("param4",(processCommand.getParameters())[3]);
	}

	@Test
	public void shouldSetGenomeRelease(){
		assertEquals("hg38", processCommand.getGenomeRelease());
	}

	@Test
	public void shouldSetProcessType(){
		assertEquals("rawtoprofile",processCommand.getProcessType());
	}

	@Test
	public void shouldSetUserID(){
		assertEquals("splutt", processCommand.getUsername());
	}

	@Test
	public void shouldSetFilename(){
		assertEquals("fileNAME66", processCommand.getFilename());
	}

	@Test
	public void shouldSetFilepath(){
		assertEquals("path/to/local/file",processCommand.getFilepath());
	}

	@Test
	public void shouldSetExpID(){
		assertEquals("Exp1",processCommand.getExpID());
	}

	@Test
	public void execute(){
		Response response = processCommand.execute();
		System.out.println("Execute response: " + response.getCode());
	}

}
