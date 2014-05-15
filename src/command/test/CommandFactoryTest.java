package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.CommandFactory;

/**
 * Testclass used to test the CommandFactory class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
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
	public void testCreateAddExperimentCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		assertNotNull(cmdf.createAddExperimentCommand(json));

	}

	/**
	 * Test creation of Retrieve experiment command.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "JSON_STRING";
		String restful = "ABCDEF";
		assertNotNull(cmdf.createRetrieveExperimentCommand(json, restful));

	}

	/**
	 * Test creation of login command.
	 */
//	@Test
//	public void testCreateLoginCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		Command cmd = cmdf.createLoginCommand(json, restful);
//		assertNotNull(cmd);
//
//	}

	/**
	 * Test creation of logout command.
	 */
//	@Test
//	public void testCreateLogoutCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createLogoutCommand());
//
//	}

	/**
	 * Test creation of sysadmin command.
	 */
//	@Test
//	public void testCreateSysadmCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createSysadmCommand(json, restful));
//
//	}

	/**
	 * Test creation of process command.
	 */
//	@Test
//	public void testCreateProcessCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "{\"parameters\": " +
//											"[\"param1\"," +
//											"\"param2\"," +
//											"\"param3\"," +
//											"\"param4\"]," +
//						"\"metadata\": \"astringofmetadata\"," +
//						"\"genomeRelease\": \"hg38\"}";
//
//
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createProcessCommand(json, restful));
//
//	}

	/**
	 * Test creation of experiment command.
	 */
//	@Test
//	public void testCreateExperimentCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createExperimentCommand(json, restful));
//
//	}

	/**
	 * Test creation of user command.
	 */
//	@Test
//	public void testCreateUserCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createUserCommand(json, restful));
//
//	}

	/**
	 * Test creation of search command.
	 */
//	@Test
//	public void testCreateSearchCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createSearchCommand(json, restful));
//
//	}

	/**
	 * Test creation of download command.
	 */
//	@Test
//	public void testCreateDownloadCommand() {
//
//		CommandFactory cmdf = new CommandFactory();
//		String json = "JSON_STRING";
//		String[] restful = {"1", "2"};
//		assertNotNull(cmdf.createDownloadCommand(json, restful));
//
//	}

//	@Test
//	public void testParseID() {
//		String restful = "/file/421";
//		CommandFactory cmdf = new CommandFactory();
//		String id = cmdf.parseID(restful);
//		assertEquals("421", id);
//	}

}
