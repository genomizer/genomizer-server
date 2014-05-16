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
		String json = "{\"name\":\"experimentId\",\"createdBy\":\"user\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"}]}";

		assertNotNull(cmdf.createAddExperimentCommand(json));

	}

	/**
	 * Test creation of Retrieve experiment command.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		CommandFactory cmdf = new CommandFactory();
		String restful = "ABCDEF";
		assertNotNull(cmdf.createGetExperimentCommand(restful));

	}

	/**
	 * Test creation of login command.
	 */
	@Test
	public void testCreateLoginCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "{\"username\":\"uname\",\"password\":\"pw\"}";
		assertNotNull(cmdf.createLoginCommand(json));

	}

	/**
	 * Test creation of logout command.
	 */
	@Test
	public void testCreateLogoutCommand() {

		CommandFactory cmdf = new CommandFactory();
		String username = "ABCD";

		assertNotNull(cmdf.createLogoutCommand(username));

	}

	/**
	 * Test that creation of AddGenomeReleaseCommand is not
	 * null.
	 */
	@Test
	public void testCreateAddGenomeReleaseCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"GV 1.0\"}";

		assertNotNull(cmdf.createAddGenomeReleaseCommand(json));

	}

	/**
	 * Test that creation of AddGenomeReleaseCommand is not
	 * null.
	 */
	@Test
	public void testCreateDeleteGenomeReleaseCommand() {

		CommandFactory cmdf = new CommandFactory();
		String specie = "tempSpecie";
		String genomeVersion = "tmpGenomeVersion";

		assertNotNull(cmdf.createDeleteGenomeReleaseCommand(specie, genomeVersion));

	}

	/**
	 * Test that creation of process command is not
	 * null.
	 */
	@Test
	public void testCreateProcessCommand() {

		CommandFactory cmdf = new CommandFactory();
		String json = "{\"expid\":\"Exp1\",\"processtype\":\"rawtoprofile\"," +
				"\"parameters\":[\"-a -m 1 --best -p 10 -v 2 -q -S\",\"d_melanogaster_fb5_22\"" +
				",\"y\",\"y\",\"10 1 5 0 0\",\"y 10\",\"single 4 0\",\"150 1 7 0 0\"]," +
				"\"metadata\": \"astringofmetadata\",\"genomeRelease\":" +
				"\"hg38\",\"author\": \"yuri\"}";
		String username = "tmpUsername";
		String parsedRestful = "parsed";
		assertNotNull(cmdf.createProcessCommand(json, username, parsedRestful));

	}

}
