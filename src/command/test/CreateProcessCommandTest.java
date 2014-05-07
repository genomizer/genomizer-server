package command.test;

import static org.junit.Assert.*;

import org.junit.*;

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

		uuid = Authenticate.createUserID("splutt");
		Authenticate.addUser("splutt", uuid);

		String json = "{" +
							"\"filename\": \"fileNAME\"," +
							"\"filepath\": \"path\to\local\file\"," +
							"\"expid": "66","processtype": "rawtoprofile","parameters": ["param1","param2","param3","param4"
				                ],
				 "metadata": "astringofmetadata",
				 "genomeRelease": "hg38",
				 "author": "yuri"
				}";

		String restful = "/process/rawtoprofile/66";

		String[] restfulArray = restful.split("/");

		processCommand = (ProcessCommand)cmdf.createProcessCommand(json, uuid);
	}

	@Test
	public void shouldCreateProcessCommandFromJson(){

		assertNotNull(processCommand);
		assertEquals("astringofmetadata", processCommand.getMetadata());

		assertEquals(4,processCommand.getParameters().length);
		assertEquals("param1",(processCommand.getParameters())[0]);
		assertEquals("param2",(processCommand.getParameters())[1]);
		assertEquals("param3",(processCommand.getParameters())[2]);
		assertEquals("param4",(processCommand.getParameters())[3]);

		assertEquals("hg38", processCommand.getGenomeRelease());

	}

	@Test
	public void shouldSetProcessType(){
		assertEquals("rawtoprofile",processCommand.getProcessType());
	}

	@Test
	public void shouldSetUserID(){
		assertEquals(uuid, processCommand.getUserID());
	}



}
