package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.Command;
import command.CommandFactory;

public class CommandFactoryTest {

	/**
	 * Test object creation.
	 */
	@Test
	public void testCommandFactory() {

		CommandFactory cmdf = new CommandFactory();
		assertNotNull(cmdf);

	}

	/**
	 * Test creation of upload command.
	 */
	@Test
	public void testCreateUploadCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createUploadCommand(json, restful));

	}

	/**
	 * Test creation of Retrieve experiment command.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createRetrieveExperimentCommand(json, restful));

	}

	/**
	 * Test creation of login command.
	 */
	@Test
	public void testCreateLoginCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		Command cmd = cmdf.createLoginCommand(json, restful);
		assertNotNull(cmd);

	}

	/**
	 * Test creation of logout command.
	 */
	@Test
	public void testCreateLogoutCommand() {

		CommandFactory cmdf = new CommandFactory();
		String restful = "restful";
		assertNotNull(cmdf.createLogoutCommand(restful));

	}

	/**
	 * Test creation of sysadmin command.
	 */
	@Test
	public void testCreateSysadmCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createSysadmCommand(json, restful));

	}

	/**
	 * Test creation of process command.
	 */
	@Test
	public void testCreateProcessCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createProcessCommand(json, restful));

	}

	/**
	 * Test creation of experiment command.
	 */
	@Test
	public void testCreateExperimentCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createExperimentCommand(json, restful));

	}

	/**
	 * Test creation of user command.
	 */
	@Test
	public void testCreateUserCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createUserCommand(json, restful));

	}

	/**
	 * Test creation of search command.
	 */
	@Test
	public void testCreateSearchCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createSearchCommand(json, restful));

	}

	/**
	 * Test creation of download command.
	 */
	@Test
	public void testCreateDownloadCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "restful";
		assertNotNull(cmdf.createDownloadCommand(json, restful));

	}

}
