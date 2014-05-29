package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ProcessCommand;
import command.ValidateException;

/**
 * Class used to test that the ProcessCommand class
 * works properly. The execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class ProcessCommandTest {

	private Gson gson;
	private ProcessCommand c = null;

	/**
	 * Setup method used to create the gson builder.
	 */
	@Before
	public void setUp() {

		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
		c = new ProcessCommand();

	}

	/**
	 * Test used to check that creation works and
	 * object is not null.
	 */
	@Test
	public void testCreationNotNull() {

		ProcessCommand c = new ProcessCommand();

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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername(null);
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("");
		c.setProcessType("rawtoprofile");
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
		for(int i = 0; i < database.constants.MaxSize.USERNAME + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername(big);
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hel/lo");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType(null);
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("I Dont Exist");
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
		String json = jsonAndInfoBuilder(null,p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		for(int i = 0; i < database.constants.MaxSize.FILE_EXPID + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder(big,p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experi/mentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,null,"gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * MetaData is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidationMetaDataEmptyString() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		for(int i = 0; i < database.constants.MaxSize.FILE_METADATA + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,big,"gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * MetaData contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateMetaDataInvalidCharacters() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metad/ata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata",null);
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		for(int i = 0; i < database.constants.MaxSize.FILE_GRVERSION + 1; i++) {
			big = big + "a";
		}
		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata",big);
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","ge/n1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String[] p = {"a","b","c","d","e","f","g","h"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
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
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("rawtoprofile");
		String compare = gson.toJson(c);

		assertEquals(compare, json);

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param strings to insert into JSON object.
	 * @return JSON formatted string.
	 */
	private String jsonAndInfoBuilder(String expId, String[] param, String met, String genV) {

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
