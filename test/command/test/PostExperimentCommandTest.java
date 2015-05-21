package command.test;

import static org.junit.Assert.*;

import command.experiment.PostExperimentCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ValidateException;
import database.constants.MaxLength;

/**
 * Test class used to check that PostExperimentCommand class
 * works properly. Execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PostExperimentCommandTest {

	public Gson gson = null;

	/**
	 * Setup method containing the gson builder.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();

	}

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		PostExperimentCommand c = new PostExperimentCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is not passed.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameNotPassed() throws ValidateException {

		String json = "{\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameEmptyString() throws ValidateException {

		String json = "{\"name\":\"\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"" + big +
				"\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"hell/o\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * annotation name is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameAnnotationMissingName() throws ValidateException {

		String json = "{\"name\":\"valid\",\"annotations\":[{\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * annotation name is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameAnnotationEmptyString() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * annotation name contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameAnnotationInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubme/dId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidationException is thrown when
	 * value is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueMissing() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidationException is thrown when
	 * value is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueEmptyString() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidationException is thrown when
	 * value length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_VALUE + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"" + big +
				"\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidationException is thrown when
	 * value contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc/123\"},{\"name\":\"type\",\"val!ue\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidationException is not thrown when
	 * everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.setFields("uri", null, "userName", UserType.USER);

		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that conversion to and from JSON works properly.
	 */
	@Test
	public void testConvertToAndFromJSON() {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		String compare = gson.toJson(c);
		c.setFields("uri", null, "userName", UserType.ADMIN);

		assertEquals(json, compare);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.setFields("uri", null, "userName", UserType.USER);

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

		String json = "{\"name\":\"experimentId\",\"annotations\":[{\"name\":\"pubmedId\",\"value\":\"abc123\"},{\"name\":\"type\",\"value\":\"raw\"}]}";
		PostExperimentCommand c = gson.fromJson(json, PostExperimentCommand.class);
		c.setFields("uri", null, "userName", UserType.GUEST);

		c.validate();
		fail();
	}


}









