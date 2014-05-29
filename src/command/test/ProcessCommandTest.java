package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import command.ProcessCommand;
import command.ValidateException;

/*
 		String json = "{\"expid\":\"Exp1\"," +
				"\"parameters\":[\"-a -m 1 --best -p 10 -v 2 -q -S\", \"\",\"y\",\"n\",\"10 1 5 0 0\",\"y 10\",\"single 4 0\",\"150 1 7 0 0\"]," +
				"\"metadata\":\"astringofmetadata\",\"genomeVersion\":\"hg38\"}";

 */

/**
 * Class used to test that the ProcessCommand class
 * works properly. The execute method is not tested here.
 *
 * @author tfy09jnn
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

	@Test
	public void testValidationUserNameNotNull() {
		
	}
	
	@Test
	public void testValidationUserNameEmptyString() {
		
	}
	
	@Test
	public void testValidateUserNameLengthToLong() {
		
	}
	
	@Test
	public void testValidateUserNameInvalidCharacters() {
		
	}
	
	
	
	/**
	 * Test used to check that ValidateException is not thrown
	 * when everything is properly formatted.
	 * 
	 * @throws ValidateException 
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String[] p = {"a","b","c","d"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("raw");
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that Converting to and from JSON works
	 * properly.
	 */
	@Test
	public void testConvertJSON() {
		
		String[] p = {"a","b","c","d"};
		String json = jsonAndInfoBuilder("experimentID",p,"metadata","gen1");
		c = gson.fromJson(json, ProcessCommand.class);
		c.setUsername("hello");
		c.setProcessType("raw");
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
		for(int i = 0; i < param.length; i++) {
			parameters = parameters + "\"" + param[i] + "\"";
			if(i != param.length -1) {
				parameters = parameters + ",";
			}
		}
		String json = "{\"expid\":\"" + expId + "\"," +
				"\"parameters\":[" + parameters +"]," +
				"\"metadata\":\"" + met + "\",\"genomeVersion\":\"" + genV +"\"}";

		return json;
	}

}
