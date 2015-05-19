package command.test;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;

import command.ValidateException;
import command.experiment.PutExperimentCommand;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;

/**
 * Class used to check that PutExperimentCommand class
 * works properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class PutExperimentCommandTest {

	private Gson gson = null;
	private String json = "{\"name\":\"experimentId\"," +
			"\"annotations\":[" +
				"{\"name\":\"pubmedId\",\"value\":\"abc123\"}," +
				"{\"name\":\"type\",\"value\":\"raw\"}," +
				"{\"name\":\"specie\",\"value\":\"human\"}," +
				"{\"name\":\"genome release\",\"value\":\"v.123\"}," +
				"{\"name\":\"cell line\",\"value\":\"yes\"}," +
				"{\"name\":\"development stage\",\"value\":\"larva\"}," +
				"{\"name\":\"sex\",\"value\":\"male\"}," +
				"{\"name\":\"tissue\",\"value\":\"eye\"}" +
			"]}";

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

		PutExperimentCommand c = new PutExperimentCommand();

		assertNotNull(c);
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameEmptyString() throws ValidateException {

		String faultyJson = "{\"name\": \"\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
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
		String faultyJson = "{\"name\": \"" + big + "\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
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

		String faultyJson = "{\"name\": \"hell/o\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * annotation name is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationNameEmptyString() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * annotation name length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			big = big + "A";
		}
		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"" + big + "\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
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
	public void testValidateAnnotationNameInvalidCharacters() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"not/correct\", \"value\": \"abc123\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * the value is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueEmptyString() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * the value length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.EXPID + 1; i++) {
			big = big + "A";
		}
		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"" + big + "\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * the value contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateAnnotationValueInvalidCharacters() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
				"{\"name\": \"pubmedId\", \"value\": \"not/correct\"}," +
				"{ \"name\": \"type\", \"value\": \"raw\"}," +
				"{ \"name\": \"specie\", \"value\": \"human\"}," +
				"{ \"name\": \"genome release\", \"value\": \"v.123\"}," +
				"{ \"name\": \"cell line\", \"value\": \"yes\"}," +
				"{ \"name\": \"development stage\", \"value\": \"larva\"}," +
				"{ \"name\": \"sex\", \"value\": \"male\"}," +
				"{ \"name\": \"tissue\", \"value\": \"eye\"} " +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * there is no annotations.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNoAnnotations() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": []}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * there is no annotations.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateEmptyAnnotation() throws ValidateException {

		String faultyJson = "{\"name\": \"experimentId\"," +
				"\"annotations\": [" +
					"{\"name\": \"pubmedId\", \"value\": \"not/correct\"}," +
					"{}" +
				"]}";
		PutExperimentCommand c = gson.fromJson(faultyJson, PutExperimentCommand.class);
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

		PutExperimentCommand c = gson.fromJson(json, PutExperimentCommand.class);
		c.setFields("/uri/expId", "uuid", UserType.USER);

		c.validate();
	}

	/**
	 * Test used to check that conversion to and from JSON works properly.
	 */
	@Test
	public void testConvertToAndFromJSON() {

		PutExperimentCommand c = gson.fromJson(json, PutExperimentCommand.class);
		String compare = gson.toJson(c);

		assertEquals(json, compare);
	}


	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws command.ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		Command c = gson.fromJson(json,PutExperimentCommand.class);
		c.setFields("/exp/uri", "", "uuid", UserType.USER);
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

		Command c = gson.fromJson(json,PutExperimentCommand.class);
		c.setFields("/exp/uri", "", "uuid", UserType.GUEST);
		c.validate();
		fail();
	}

}
