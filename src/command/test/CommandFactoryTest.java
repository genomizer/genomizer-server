package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import command.CommandFactory;

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

		assertNotNull(cmdf.createLogoutCommand("userName"));

	}

	/**
	 * Test creation of GetExperimentCommand and that it's not null.
	 */
	@Test
	public void testCreateGetExperimentCommandNotNull() {

		assertNotNull(cmdf.createGetExperimentCommand("restful"));

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
		assertNotNull(cmdf.createUpdateExperimentCommand(json, "/experiment/id123"));

	}

	/**
	 * Test creation of DeleteExperimentCommand and that it's not null.
	 */
	@Test
	public void testDeleteExperimentCommandNotNull() {

		assertNotNull(cmdf.createDeleteExperimentCommand("NONE", "/experiment/id123"));

	}

	/**
	 * Test creation of GetFileFromExperimentCommand and that it's not null.
	 */
	@Test
	public void testGetFileFromExperimentCommandNotNull() {

		assertNotNull(cmdf.createGetFileFromExperimentCommand("NONE", "/file/myFileId"));

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

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\","
				+ "\"metaData\":\"metameta\",\"author\":\"name\",\"uploader\":\"user1\","
				+ "\"isPrivate\":\"bool\",\"grVersion\":\"releaseNr\"}";
		assertNotNull(cmdf.createUpdateFileInExperimentCommand(json, "/file/myFileId"));

	}

	/**
	 * Test creation of DeleteFileFromExperimentCommand and that it's not null.
	 */
	@Test
	public void testDeleteFileFromExperimentCommandNotNull() {

		assertNotNull(cmdf.createDeleteFileFromExperimentCommand("NONE", "/file/myFileId"));

	}

	/**
	 * Test creation of SearchForExperimentCommand and that it's not null.
	 */
	@Test
	public void testSearchForExperimentCommandNotNull() {

		assertNotNull(cmdf.createSearchForExperimentCommand("/search/?annotations=pubmedStyleQuery"));

	}

	/**
	 * Test creation of UpdateUserCommand and that it's not null.
	 */
	@Test
	public void testUpdateUserCommandNotNull() {

		String json = "{\"username\":\"uname\",\"password\":\"pw\","
				+ "\"privileges\":\"basic\",\"name\":\"John Johnson\","
				+ "\"email\":\"john@mail.com\"}";
		assertNotNull(cmdf.createUpdateUserCommand(json, "/user"));

	}

	/**
	 * Test creation of DeleteUserCommand and that it's not null.
	 */
	@Test
	public void testDeleteUserCommandNotNull() {

		assertNotNull(cmdf.createDeleteUserCommand("/user"));

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
		assertNotNull(cmdf.createProcessCommand(json, "userName", "parsedRestful"));

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

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],"
				+ "\"default\":\"human\",\"forced\":false}";
		assertNotNull(cmdf.createAddAnnotationFieldCommand(json, "/annotation/field"));

	}

	/**
	 * Test creation of AddAnnotationValueCommand and that it's not null.
	 */
	@Test
	public void testAddAnnotationValueCommandNotNull() {

		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		assertNotNull(cmdf.createAddAnnotationValueCommand(json, "/annotation/value"));

	}

	/**
	 * Test creation of RemoveAnnotationFieldCommand and that it's not null.
	 */
	@Test
	public void testRemoveAnnotationFieldCommandNotNull() {

		String[] insert = {"NONE","/annotation/field/myFieldName"};
		assertNotNull(cmdf.createRemoveAnnotationFieldCommand(insert[0], insert[1]));

	}

	/**
	 * Test creation of GetAnnotationPrivilegesCommand and that it's not null.
	 */
	@Test
	public void testGetAnnotationPrivilegesCommandNotNull() {

		String json = "{\"name\":\"a\",\"oldValue\":\"b\",\"newValue\":\"c\"}";
		assertNotNull(cmdf.createGetAnnotationPrivilegesCommand(json));

	}
	//TODO: Should probably be removed.
	/**
	 * Test creation of UpdateAnnotationPrivilegesCommand and that it's not null.
	 */
	@Test
	public void testUpdateAnnotationPrivilegesCommandNotNull() {

		assertNotNull(cmdf.createUpdateAnnotationPrivilegesCommand("a", "b"));

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
	 * Test that creation of AddGenomeReleaseCommand is not
	 * null.
	 */
	@Test
	public void testCreateDeleteGenomeReleaseCommand() {

		assertNotNull(cmdf.createDeleteGenomeReleaseCommand("specie", "genVersion"));

	}

	/**
	 * Test creation of DeleteAnnotationValueCommand and that it's not null.
	 */
	@Test
	public void testDeleteAnnotationValueCommandNotNull() {

		assertNotNull(cmdf.createDeleteAnnotationValueCommand("NONE", "valueName", "fieldName"));

	}

	/**
	 * Test creation of EditAnnotationFieldCommand and that it's not null.
	 */
	@Test
	public void testEditAnnotationFieldCommandNotNull() {

		String json = "{\"oldName\":\"a\",\"newName\":\"b\"}";
		assertNotNull(cmdf.createEditAnnotationFieldCommand(json));

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

		assertNotNull(cmdf.createGetGenomeReleasesSpeciesCommand("species"));

	}

	/**
	 * Test creation of GetProcessStatusCommand and that it's not null.
	 */
	@Test
	public void testGetProcessStatusCommandNotNull() {

		assertNotNull(cmdf.createGetProcessStatusCommand(null));

	}

	/**
	 * Test creation of Retrieve experiment command and that it's not null.
	 */
	@Test
	public void testCreateRetrieveExperimentCommand() {

		assertNotNull(cmdf.createGetExperimentCommand("restful"));

	}

}
