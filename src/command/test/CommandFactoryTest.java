package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import command.CommandFactory;

//TODO: Implement missing unit tests, make sure all methods in commandfactory are tested.
/**
 * Testclass used to test the CommandFactory class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CommandFactoryTest {

	private CommandFactory cmdf = null;

	/**
	 * Used before each test to create the commandFactory.
	 */
	@Before
	public void setUp() {

		cmdf = new CommandFactory();

	}

	/**
	 * Test object creation of CommandFactory and that
	 * it's not null.
	 */
	@Test
	public void testCommandFactory() {

		cmdf = null;
		cmdf = new CommandFactory();
		assertNotNull(cmdf);

	}

	/**
	 * Test creation of LoginCommand and that it's not null.
	 */
	@Test
	public void testCreateLoginCommandNotNull() {

		String json = "{\"username\":\"uname\",\"password\":\"pw\"}";
		assertNotNull(cmdf.createLoginCommand(json));

	}

	/**
	 * Test creation of LogoutCommand and that it's not null.
	 */
	@Test
	public void testCreateLogoutCommandNotNull() {

		String username = "UserName";
		assertNotNull(cmdf.createLogoutCommand(username));

	}

	/**
	 * Test creation of GetExperimentCommand and that it's not null.
	 */
	@Test
	public void testCreateGetExperimentCommandNotNull() {

		String restful = "RESTFUL";
		assertNotNull(cmdf.createGetExperimentCommand(restful));

	}

	/**
	 * Test creation of AddExperimentCommand and that it's not null.
	 */
	@Test
	public void testCreateAddExperimentCommandNotNull() {

		String json = "{\"name\":\"experimentId\",\"createdBy\":\"user\",\"annotations\":"
				+ "[{\"name\":\"pubmedId\",\"value\":\"abc123\"}]}";
		assertNotNull(cmdf.createAddExperimentCommand(json));

	}

	/**
	 * Test creation of UpdateExperimentCommand and that it's not null.
	 */
	@Test
	public void testCreateUpdateExperimentCommandNotNull() {

		String json = "{\"name\": \"experimentId\",\"createdBy\":\"user\",\"annotations\":"
				+ "[{\"name\":\"pubmedId\",\"value\":\"abc123\"}]}";
		String restful = "/experiment/id123";
		assertNotNull(cmdf.createUpdateExperimentCommand(json, restful));

	}

	/**
	 * Test creation of DeleteExperimentCommand and that it's not null.
	 */
	@Test
	public void testDeleteExperimentCommandNotNull() {

		String restful = "/experiment/id123";
		String json = "NONE";
		assertNotNull(cmdf.createDeleteExperimentCommand(json, restful));

	}

	/**
	 * Test creation of GetFileFromExperimentCommand and that it's not null.
	 */
	@Test
	public void testGetFileFromExperimentCommandNotNull() {

		String restful = "/file/myFileId";
		String json = "NONE";
		assertNotNull(cmdf.createGetFileFromExperimentCommand(json, restful));

	}

	/**
	 * Test creation of AddFileToExperimentCommand and that it's not null.
	 */
	@Test
	public void testAddFileToExperimentCommandNotNull() {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\","
				+ "\"metaData\":\"metameta\",\"author\":\"name\",\"uploader\":\"user1\","
				+ "\"isPrivate\":\"bool\",\"grVersion\":\"releaseNr\"}";
		assertNotNull(cmdf.createAddFileToExperimentCommand(json));

	}

	/**
	 * Test creation of UpdateFileInExperimentCommand and that it's not null.
	 */
	@Test
	public void testUpdateFileInExperimentCommandNotNull() {

		String restful = "/file/myFileId";
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\","
				+ "\"metaData\":\"metameta\",\"author\":\"name\",\"uploader\":\"user1\","
				+ "\"isPrivate\":\"bool\",\"grVersion\":\"releaseNr\"}";
		assertNotNull(cmdf.createUpdateFileInExperimentCommand(json, restful));

	}

	/**
	 * Test creation of DeleteFileFromExperimentCommand and that it's not null.
	 */
	@Test
	public void testDeleteFileFromExperimentCommandNotNull() {

		String restful = "/file/myFileId";
		String json = "NONE";
		assertNotNull(cmdf.createDeleteFileFromExperimentCommand(json, restful));

	}

	/**
	 * Test creation of SearchForExperimentCommand and that it's not null.
	 */
	@Test
	public void testSearchForExperimentCommandNotNull() {

		String restful = "/search/?annotations=pubmedStyleQuery";
		assertNotNull(cmdf.createSearchForExperimentCommand(restful));

	}

	/**
	 * Test creation of UpdateUserCommand and that it's not null.
	 */
	@Test
	public void testUpdateUserCommandNotNull() {

		String restful = "/user";
		String json = "{\"username\":\"uname\",\"password\":\"pw\","
				+ "\"privileges\":\"basic\",\"name\":\"John Johnson\","
				+ "\"email\":\"john@mail.com\"}";
		assertNotNull(cmdf.createUpdateUserCommand(json, restful));

	}

	/**
	 * Test creation of DeleteUserCommand and that it's not null.
	 */
	@Test
	public void testDeleteUserCommandNotNull() {

		String restful = "/user";
		assertNotNull(cmdf.createDeleteUserCommand(null, restful));

	}

	/**
	 * Test that creation of process command is not
	 * null.
	 */
	@Test
	public void testCreateProcessCommand() {

		String json = "{\"expid\":\"Exp1\",\"processtype\":\"rawtoprofile\"," +
				"\"parameters\":[\"-a -m 1 --best -p 10 -v 2 -q -S\",\"d_melanogaster_fb5_22\"" +
				",\"y\",\"y\",\"10 1 5 0 0\",\"y 10\",\"single 4 0\",\"150 1 7 0 0\"]," +
				"\"metadata\": \"astringofmetadata\",\"genomeRelease\":" +
				"\"hg38\",\"author\": \"yuri\"}";
		String username = "tmpUsername";
		String parsedRestful = "parsed";
		assertNotNull(cmdf.createProcessCommand(json, username, parsedRestful));

	}

	/**
	 * Test creation of GetAnnotationInformationCommand and that it's not null.
	 */
	@Test
	public void testGetAnnotationInformationCommandNotNull() {

		assertNotNull(cmdf.createGetAnnotationInformationCommand(null));

	}

	/**
	 * Test creation of AddAnnotationFieldCommand and that it's not null.
	 */
	@Test
	public void testAddAnnotationFieldCommandNotNull() {

		String restful = "/annotation/field";
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],"
				+ "\"default\":\"human\",\"forced\":false}";
		assertNotNull(cmdf.createAddAnnotationFieldCommand(json, restful));

	}

	/**
	 * Test creation of AddAnnotationValueCommand and that it's not null.
	 */
	@Test
	public void testAddAnnotationValueCommandNotNull() {

		String restful = "/annotation/value";
		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		assertNotNull(cmdf.createAddAnnotationValueCommand(json, restful));

	}

	/**
	 * Test creation of RemoveAnnotationFieldCommand and that it's not null.
	 */
	@Test
	public void testRemoveAnnotationFieldCommandNotNull() {

		String restful = "/annotation/field/myFieldName";
		String json = "NONE";
		assertNotNull(cmdf.createRemoveAnnotationFieldCommand(json, restful));

	}

	/**
	 * Test creation of GetAnnotationPrivilegesCommand and that it's not null.
	 */
	@Test
	public void testGetAnnotationPrivilegesCommandNotNull() {

		//TODO: implement.
		fail("Not yet implemented.");

	}

	/**
	 * Test creation of UpdateAnnotationPrivilegesCommand and that it's not null.
	 */
	@Test
	public void testUpdateAnnotationPrivilegesCommandNotNull() {
		//TODO: Implement.
		fail("Not yet implemented.");
	}

	/**
	 * Test that creation of AddGenomeReleaseCommand is not
	 * null.
	 */
	@Test
	public void testCreateAddGenomeReleaseCommand() {

		String json = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"GV 1.0\"}";
		assertNotNull(cmdf.createAddGenomeReleaseCommand(json));

	}

	/**
	 * Test creation of RenameAnnotationValueCommand and that it's not null.
	 */
	@Test
	public void testRenameAnnotationValueCommandNotNull() {
		//TODO: Implement.
		fail("Not yet implemented.");
	}

	/**
	 * Test that creation of AddGenomeReleaseCommand is not
	 * null.
	 */
	@Test
	public void testCreateDeleteGenomeReleaseCommand() {

		String specie = "tempSpecie";
		String genomeVersion = "tmpGenomeVersion";
		assertNotNull(cmdf.createDeleteGenomeReleaseCommand(specie, genomeVersion));

	}

	/**
	 * Test creation of DeleteAnnotationValueCommand and that it's not null.
	 */
	@Test
	public void testDeleteAnnotationValueCommandNotNull() {
		//TODO: Implement.
		fail("Not yet implemented.");
	}

	/**
	 * Test creation of EditAnnotationFieldCommand and that it's not null.
	 */
	@Test
	public void testEditAnnotationFieldCommandNotNull() {

		//TODO: Implement
		fail("Not implemented yet.");

	}

	/**
	 * Test creation of GetAllGenomeReleasesCommand and that it's not null.
	 */
	@Test
	public void testGetAllGenomeReleasesCommandNotNull() {

		assertNotNull(cmdf.createGetAllGenomeReleasesCommand());

	}

	/**
	 * Test creation of GetGenomeReleaseSpeciesCommand and that it's not null.
	 */
	@Test
	public void testGetGenomeReleaseSpeciesCommandNotNull() {

		String species = "mySpeices";
		assertNotNull(cmdf.createGetGenomeReleasesSpeciesCommand(species));

	}

	/**
	 * Test creation of GetProcessStatusCommand and that it's not null.
	 */
	@Test
	public void testGetProcessStatusCommandNotNull() {
		//TODO: Implement.
		fail("Not yet implemented.");
	}

	/**
	 * Test creation of Retrieve experiment command and that it's not null.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		String restful = "ABCDEF";
		assertNotNull(cmdf.createGetExperimentCommand(restful));

	}

}
