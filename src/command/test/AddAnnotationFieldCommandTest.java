package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddAnnotationFieldCommand;
import command.Command;
import command.ValidateException;
import database.constants.MaxSize;

/**
 * Test class used to check that the AddAnnotationFieldCommand
 * class works properly. Execute method is tested elsewhere.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class AddAnnotationFieldCommandTest {

	public Gson gson = null;

	/**
	 * Setup method to initiate GSON builder.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test creating object and that it's not null.
	 */
	@Test
	public void testCreateAddAnnotationFieldCommand() {

		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameNull() throws ValidateException {

		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		String json = "{\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
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

		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		String json = "{\"name\":\"\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
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
	public void testValidateNameLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxSize.ANNOTATION_LABEL + 1; i++) {
			big = big + "A";
		}
	    String json = "{\"name\":\"" + big +
	    		"\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * characters are invalid.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testNameHasInvalidCharacters() throws ValidateException {

		String invalid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 едц";
		String json = "{\"name\":\"" + invalid +
				"\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * the ArrayList with types are empty.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeIsEmpty() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[],\"default\":\"human\",\"forced\":false}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * the ArrayList with types has invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeHasInvalidCharacters() throws ValidateException {

		String invalidType = "ra/t";
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"" + invalidType +
				"\",\"human\"],\"default\":\"human\",\"forced\":false}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that ValidateException is thrown if forced is not passed
	 * at all in JSON.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateForcedNotPassed() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\"}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that default variable can be null and
	 * that ValidateException is not thrown.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateDefaultNotPassedWorking() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"forced\":true}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when default is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultEmptyString() throws ValidateException {

	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"\",\"forced\":true}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if default
	 * contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human!!\",\"forced\":false}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * Defaults length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.ANNOTATION_DEFAULTVALUE + 1; i++) {
			big = big + "a";
		}
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"" + big +
				"\",\"forced\":false}";
		AddAnnotationFieldCommand c = new AddAnnotationFieldCommand();
		c = gson.fromJson(json, AddAnnotationFieldCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that creating a freetext annotation works
	 * properly.
	 */
	@Test
	public void testAddAnnotationFieldCommandFreetextJSON() {

	    String json = "{\"name\":\"species\",\"type\":[\"freetext\"],\"default\":null,\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    String jsonCompare = "{\"name\":\"species\",\"type\":[\"freetext\"],\"forced\":true}";
	    String json2 = gson.toJson(aafc);

	    assertEquals(json2, jsonCompare);

	}

	/**
	 * Test AddAnnotationField command creation of object with
	 * JSON string and then check that they are equal.
	 */
	@Test
	public void testAddAnnotationFieldCommandJSON() {

	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
		String json2 = gson.toJson(aafc);

		assertEquals(json2, json);

	}

	/**
	 * Test to check that validation code does not throw a ValidateException
	 * if the JSON is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidationProperJSON() throws ValidateException {

	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command c = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    c.validate();

	    assertTrue(true);

	}

}
