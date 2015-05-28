package command.test.annotation;

import static org.junit.Assert.*;

import command.annotation.PostAnnotationFieldCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;

/**
 * Test class used to check that the PostAnnotationFieldCommand class works
 * properly. The execute method is not tested as it requires a connection to a
 * external database.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationFieldCommandTest {
	private Gson gson;

	/**
	 * Setup method to initiate the GSON object.
	 */
	@Before
	public void setUp() {
	    GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();
	}

	/**
	 * Test used to check that ValidateException is thrown when the name field
	 * is null.
	 * @throws ValidateException if the name field is null.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameNull() throws ValidateException {
		String json = "{\"type\":[\"fly\",\"rat\",\"human\"],\"default\":" +
				"\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when name is an empty
	 * string.
	 * @throws ValidateException if the name field is  an empty string.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameEmptyString() throws ValidateException {
		String json = "{\"name\":\"\",\"type\":[\"fly\",\"rat\",\"human\"]," +
				"\"default\":\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown when length of the
	 * name field is too long.
	 * @throws ValidateException when the length of the name field is too long.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameLength() throws ValidateException {
		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			big = big + "A";
		}

	    String json = "{\"name\":\"" + big + "\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if characters are
	 * invalid.
	 * @throws ValidateException if invalid characters are used
	 */
	@Test(expected = ValidateException.class)
	public void testNameHasInvalidCharacters() throws ValidateException {
		String invalid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxy" +
				"z0123456789 ���";
		String json = "{\"name\":\"" + invalid + "\",\"type\":[\"fly\"," +
				"\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if array list with
	 * types is empty.
	 * @throws ValidateException if the array list with types is empty.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeIsEmpty() throws ValidateException {
		String json = "{\"name\":\"species\",\"type\":[],\"default\":" +
				"\"human\",\"forced\":false}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if the array list
	 * with types contains invalid characters.
	 * @throws ValidateException if the array list with types contains invalid
	 * characters.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateTypeHasInvalidCharacters() throws ValidateException {
		String invalidType = "ra/t";
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"" + invalidType +
				"\",\"human\"],\"default\":\"human\",\"forced\":false}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test that checks that ValidateException is thrown if forced is not passed
	 * at all in JSON.
	 * @throws ValidateException if forced is not passed at all in JSON.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateForcedNotPassed() throws ValidateException {
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"human\"}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that default field can be null and that
	 * ValidateException is not thrown.
	 * @throws ValidateException should never be thrown.
	 */
	@Test
	public void testValidateDefaultNotPassedWorking() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"forced\":true}";
		PostAnnotationFieldCommand c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.setFields("uri", null, null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is thrown when the default
	 * field is an empty string.
	 * @throws ValidateException when the default field is an empty string.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultEmptyString() throws ValidateException {
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if the default field
	 * contains invalid characters.
	 * @throws ValidateException if the default field contains invalid
	 * characters.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultInvalidCharacters() throws ValidateException {
		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"human!!\",\"forced\":false}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown if the length of the
	 * default field is too long.
	 * @throws ValidateException if the length of the default field is too long.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateDefaultLengthNotToLong() throws ValidateException {
		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_DEFAULTVALUE + 1; i++) {
			big = big + "a";
		}

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"" +
				",\"human\"],\"default\":\"" + big + "\",\"forced\":false}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.validate();
		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test that checks that creating a free text annotation works properly.
	 */
	@Test
	public void testAddAnnotationFieldCommandFreetextJSON() {
	    String json = "{\"name\":\"species\",\"type\":[\"freetext\"]," +
				"\"default\":null,\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
	    String jsonCompare = "{\"name\":\"species\",\"type\":[\"freetext\"]," +
				"\"forced\":true}";
	    assertEquals(gson.toJson(c), jsonCompare);
	}

	/**
	 * Test AddAnnotationField command creation of object with JSON string and
	 * then check that they are equal.
	 */
	@Test
	public void testAddAnnotationFieldCommandJSON() {
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		String json2 = gson.toJson(c);
		assertEquals(json2, json);
	}

	/**
	 * Test to check that validation code does not throw a ValidateException if
	 * the JSON is properly formatted.
	 * @throws ValidateException should never be thrown.
	 */
	@Test
	public void testValidationProperJSON() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\"," +
				"\"human\"],\"default\":\"human\",\"forced\":true}";
		Command c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.setFields("uri", null, null, UserType.ADMIN);
		c.validate();
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		PostAnnotationFieldCommand c = gson.fromJson(json, PostAnnotationFieldCommand.class);
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

		String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		PostAnnotationFieldCommand c = gson.fromJson(json, PostAnnotationFieldCommand.class);
		c.setFields("uri", null, "userName", UserType.GUEST);

		c.validate();
		fail();
	}
}
