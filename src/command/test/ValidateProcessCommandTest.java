package command.test;

import static org.junit.Assert.*;

import org.junit.*;

import response.Response;

import authentication.Authenticate;

import command.Command;
import command.CommandFactory;
import command.ProcessCommand;

public class ValidateProcessCommandTest {



	String uuid;



	@Before
	public void setup(){


	}

	@Test
	public void shouldValidateToTrue(){

		String username = "splutt";
		String filename = "filename1234";
		String fileid = "1";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
							"\"param2\"," +
							"\"param3\"," +
							"\"param4\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"filename\": \"" + filename + "\"," +
							"\"fileid\": \"" + fileid + "\"," +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username);


		assertTrue(processCommand.validate());
	}

	@Test
	public void sholdValidateToFalseDueToNullFilename(){
		gr,author,meta;
	}

}
