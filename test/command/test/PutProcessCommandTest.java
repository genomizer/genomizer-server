package command.test;

import static org.junit.Assert.*;

import command.process.*;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ValidateException;

import java.util.UUID;

/**
 * Class used to test that the PutProcessCommand class
 * works properly. The execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class PutProcessCommandTest {

	UUID PID = UUID.randomUUID();
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
	 * Test used to check that creation works and
	 * object is not null.
	 */
	@Test
	public void testCreationNotNull() {

		PutProcessCommand c = new PutProcessCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * UserName is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationUserNameNotNull() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1",  PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername(null);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * UserName is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationUserNameEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * UserName length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUserNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.USERNAME + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername(big);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * UserName contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUserNameInvalidCharacters() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hel/lo");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * process type is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationProcessTypeNotNull() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(null);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * process type is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationProcessTypeEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype("");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * process type does not exist.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateProcessTypeInvalidSelection() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype("I Dont Exist");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * experiment id is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationExperimentIdNotNull() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder(null,p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * experiment id is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationExperimentIdEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * experiment id length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExperimentIdLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder(big,p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * experiment id contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateExperimentIdInvalidCharacters() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experi/mentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * parameters are not the correct size for raw to profile.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationParametersRawToProfileSize() throws ValidateException {

		String[] p = {"a","b","c","d"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1",PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * MetaData is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationMetaDataEmpty() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,null,"gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * MetaData is empty string.
	 *
	 * TODO: This test is not working currently as metadata can be null.
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidationMetaDataEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
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
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,big,"gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * MetaData contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataInvalidCharacters() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metad/ata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * GenomeVersion is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationGenomeVersionNotNull() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata",null, PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * GenomeVersion is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationGenomeVersionEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * GenomeVersion length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.FILE_GRVERSION + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata",big, PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * GenomeVersion contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionInvalidCharacters() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","ge/n1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setUsername("hello");
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setFields("/hello", null, null, UserType.ADMIN);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that Converting to and from JSON works
	 * properly.
	 */
	@Test
	public void testConvertJSON() {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1", PID);
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setFields("/hello/hiho", null, null, UserType.ADMIN);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);
		String compare = gson.toJson(c);

		assertEquals(compare, json);

	}

	//TODO Test for CMD_CANCEL_PROCESS

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Ignore
	@Test
	public void testHavingRights() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1",
				UUID.randomUUID());
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setFields("/hello", null, "name", UserType.USER);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);

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
		PutProcessCommand c = gson.fromJson(json, PutProcessCommand.class);
		c.setFields("/hello/hiho", null, null, UserType.GUEST);
		c.setProcesstype(PutProcessCommand.CMD_RAW_TO_PROFILE);

		c.validate();
		fail();
	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param expId and the other strings are used for inserting into JSON object.
	 * @return JSON formatted string.
	 */
	private String jsonAndInfoBuilder(String expId, String[] param, String met, String genV, UUID PID) {

		String parameters = "";
		if(param != null) {
			for(int i = 0; i < param.length; i++) {
				parameters = parameters + "\"" + param[i] + "\"";
				if(i != param.length -1) {
					parameters = parameters + ",";
				}
			}
		}

		String json = null;

		if(expId == null) {
			json = "{\"parameters\":[" + parameters +"]," +"\"metadata\":\"" + met + "\",\"genomeVersion\":\"" + genV +"\"}";
		} else if (param == null) {
			json = "{\"expid\":\"" + expId + "\"," + "\"metadata\":\"" + met + "\",\"genomeVersion\":\"" + genV +"\"}";
		} else if(met == null) {
			json = "{\"expid\":\"" + expId + "\"," + "\"parameters\":[" + parameters +"],\"genomeVersion\":\"" + genV +"\"}";
		} else if(genV == null) {
			json = "{\"expid\":\"" + expId + "\"," + "\"parameters\":[" + parameters +"]," + "\"metadata\":\"" + met + "\"}";
		} else {
			json = "{\"expid\":\"" + expId + "\"," +
					"\"parameters\":[" + parameters +"]," +
					"\"metadata\":\"" + met + "\",\"genomeVersion\":\"" + genV +"\"}";
		}

		return json;

	}

}
