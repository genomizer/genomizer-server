package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddFileToExperimentCommand;
import command.ValidateException;
/*
String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
	"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":\"bool\",\"grVersion\":\"releaseNr\"}";
*/
/**
 * Class used to test that the AddFileToExperimentCommand class
 * works properly. Execute method is not tested here.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddFileToExperimentCommandTest {

	private Gson gson;

	/**
	 * Setup method used to create the gson builder.
	 */
	@Before
	public void setUp() {

		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();

	}

	/**
	 * Test used to check that the creation works and
	 * object is not null.
	 */
	@Test
	public void testCreateNotNull() {

		AddFileToExperimentCommand c = new AddFileToExperimentCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * experiment-id is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdMissing() throws ValidateException {

		String json = "{\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * experiment-id is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * experiment-id length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"" + big +
				"\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * experiment-id contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExpIdInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"i/d\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_FILENAME + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"" + big +
				"\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"na/me\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Type is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Type is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Type length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_FILETYPE + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"" + big +
				"\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Type contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"ra/w\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * MetaData is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * MetaData is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * MetaData length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_METADATA + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"" + big +
				"\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * MetaData contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"met/ameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Author is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAuthorMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Author is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAuthorEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Author length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAuthorLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_AUTHOR + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"" + big +
				"\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Author contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAuthorInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"na/me\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Uploader is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUploaderMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Uploader is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUploaderEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Uploader length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUploaderToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_UPLOADER + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"" + big +
				"\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * Uploader contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUploaderInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"us/er1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * grVersion is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGrVersionMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * grVersion is and empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGrVersionEmptyString() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * grVersion length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGrVersionToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.FILE_GRVERSION + 1; i++) {
			big = big + "a";
		}
		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"" + big +
				"\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * grVersion contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGrVersionInvalidCharacters() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"rele/aseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * isPrivate is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateIsPrivateMissing() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown when
	 * everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that converting to and from JSON works
	 * properly.
	 */
	@Test
	public void testConvertJSON() {

		String json = "{\"experimentID\":\"id\",\"fileName\":\"name\",\"type\":\"raw\",\"metaData\":\"metameta\"," +
				"\"author\":\"name\",\"uploader\":\"user1\",\"isPrivate\":true,\"grVersion\":\"releaseNr\"}";
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		String compare = gson.toJson(c);

		assertEquals(compare, json);

	}

}
