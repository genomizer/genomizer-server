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
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertTrue(processCommand.validate());
	}
	@Test
	public void shouldValidateToTrueWhenEmptyAuthor(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertTrue(processCommand.validate());
	}

	@Test
	public void shouldValidateToTrueWhenEmptyMetadata(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertTrue(processCommand.validate());
	}
	@Test
	public void shouldValidateToFalseWhenEmptyGRversion(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "a string of metadata";
		String genomeRelease = "";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");

		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateToTrueWhenRawToProfileand8ParametersGRversion(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
							"\"param2\"," +
							"\"param3\"," +
							"\"param4\"," +
							"\"param5\"," +
							"\"param6\"," +
							"\"param7\"," +
							"\"param8\"";
		String metadata = "a string of metadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");

		assertTrue(processCommand.validate());
	}
	@Test
	public void shouldValidateToFalseWhenRawToProfileandNot8ParametersGRversion(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
							"\"param2\"," +
							"\"param3\"," +
							"\"param4\"," +
							"\"param5\"," +
							"\"param6\"," +
							"\"param8\"";
		String metadata = "a string of metadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");

		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateToFalseWhenEmptyUsername(){

		String username = "";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}

	@Test
	public void shouldValidateFalseWithoutExpid(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
		//					"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}

	@Test
	public void shouldValidateFalseWithoutProcesstype(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
		//					"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateFalseWithoutParameters(){

		String username = "splutt";

		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
		//					"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateFalseWithoutMetadata(){

		String username = "splutt";


		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
		//					"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateFalseWithoutGenomeRelease(){

		String username = "splutt";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
		//					"\"genomeRelease\": \"" + genomeRelease + "\"," +
							"\"author\": \"" + author + "\"}";

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}
	@Test
	public void shouldValidateFalseWithoutAuthor(){

		String username = "splutt";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";

		CommandFactory cmdf = new CommandFactory();



		String json = "{" +
							"\"expid\": \"" + expid + "\"," +
							"\"processtype\": \"" + processtype + "\"," +
							"\"parameters\": [" + parameters + "]," +
							"\"metadata\": \"" + metadata + "\"," +
							"\"genomeRelease\": \"" + genomeRelease + "\"}";
		//					"\"author\": \"" + author + "\"}"

		ProcessCommand processCommand = (ProcessCommand)cmdf.createProcessCommand(json, username, "rawtoprofile");


		assertFalse(processCommand.validate());
	}




}
