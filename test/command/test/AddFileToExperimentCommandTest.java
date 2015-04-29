package command.test;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.AddFileToExperimentCommand;
import command.ValidateException;

/**
 * Class used to test that the AddFileToExperimentCommand class
 * works properly. Execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
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

		String json = jsonBuilder(null,"name","raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("","name","raw","metameta","name","user1","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder(big,"name","raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("i/d","name","raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id",null,"raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","","raw","metameta","name","user1","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_FILENAME + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id",big,"raw","metameta","name","user1","releaseNr");
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName contains slashes.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameContainsSlashes() throws ValidateException {

		String json = jsonBuilder("id","nam/e","raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name",null,"metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","","metameta","name","user1","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_FILETYPE + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id","name",big,"metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","ra/w","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw",null,"name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","","name","user1","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_METADATA + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id","name","raw",big,"name","user1","releaseNr");
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
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataInvalidCharacters() throws ValidateException {

		String json = jsonBuilder("id","name","raw","metam/eta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta",null,"user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","","user1","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_AUTHOR + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id","name","raw","metameta",big,"user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","na/me","user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","name",null,"releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","name","","releaseNr");
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
		for(int i = 0; i < MaxLength.FILE_UPLOADER + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id","name","raw","metameta","name",big,"releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","name","use/r1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","name","user1",null);
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

		String json = jsonBuilder("id","name","raw","metameta","name","user1","");
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
		for(int i = 0; i < MaxLength.FILE_GRVERSION + 1; i++) {
			big = big + "a";
		}
		String json = jsonBuilder("id","name","raw","metameta","name","user1",big);
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

		String json = jsonBuilder("id","name","raw","metameta","name","user1","relea/seNr");
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

		String json = jsonBuilder("id","name.txt","raw","metameta","name","user1","releaseNr");
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

		String json = jsonBuilder("id","name","raw","metameta","name","user1","releaseNr");
		AddFileToExperimentCommand c = new AddFileToExperimentCommand();
		c = gson.fromJson(json, AddFileToExperimentCommand.class);
		String compare = gson.toJson(c);

		assertEquals(compare, json);

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param strings to insert into JSON object.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String expID, String fileN, String type, String metaD, String ath, String up, String grV) {

	    JsonObject j = new JsonObject();
		j.addProperty("experimentID", expID);
		j.addProperty("fileName", fileN);
		j.addProperty("type", type);
		j.addProperty("metaData", metaD);
		j.addProperty("author", ath);
		j.addProperty("uploader", up);
		j.addProperty("grVersion", grV);

		return j.toString();
	}

}
