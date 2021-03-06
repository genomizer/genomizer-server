package command.test.file;

import static org.junit.Assert.*;

import command.file.PostFileCommand;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.ValidateException;

/**
 * Class used to test that the PostFileCommand class
 * works properly. Execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PostFileCommandTest {

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

		PostFileCommand c = new PostFileCommand();

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

		String json = jsonBuilder(null,"name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("","name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder(big,"name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("i/d","name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id",null,"raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder("id",big,"raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","nam/e","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name",null,"metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder("id","name",big,"metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","ra/w","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","raw",null,"name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","raw","","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder("id","name","raw",big,"name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","raw","metameta",null,"releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","raw","metameta","","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder("id","name","raw","metameta",big,"releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","raw","metameta","na/me","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","profile","metameta","name",null);
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","profile","metameta","name","");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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
		String json = jsonBuilder("id","name","profile","metameta","name",big);
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name","region","metameta","name","relea/seNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
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

		String json = jsonBuilder("id","name.txt","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that converting to and from JSON works
	 * properly.
	 */
	@Test
	public void testConvertJSON() {

		String json = jsonBuilder("id","name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		String compare = gson.toJson(c);

		assertEquals(compare, json);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingFieldsRights() throws ValidateException {

		String json = jsonBuilder("id","name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.USER);

		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when the user doesn't have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNotHavingRights() throws ValidateException {

		String json = jsonBuilder("id","name","raw","metameta","name","releaseNr");
		PostFileCommand c = gson.fromJson(json, PostFileCommand.class);
		c.setFields("uri", null, "uuid", UserType.GUEST);

		c.validate();
		fail();
	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param expID and the others strings are used for inserting into JSON object.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String expID, String fileN, String type, String metaD, String ath, String grV) {

	    JsonObject j = new JsonObject();
		j.addProperty("experimentID", expID);
		j.addProperty("fileName", fileN);
		j.addProperty("type", type);
		j.addProperty("metaData", metaD);
		j.addProperty("author", ath);
		j.addProperty("grVersion", grV);

		return j.toString();
	}

}
