package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.RenameAnnotationValueCommand;
import command.ValidateException;

/**
 * Class used to check that RenameAnnotationValueCommand class
 * works properly. Execute is not tested here.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RenameAnnotationValueCommandTest {

	private Gson gson = null;

	/**
	 * Setup method to initiate GSON builder and
	 * initiate a Annotation class with JSON.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameMissing() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder(null, "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * name is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameEmptyString() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
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
	public void testValidateNameLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.ANNOTATION_LABEL + 1; i++) {
			big = big + "a";
		}
		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder(big, "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
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

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jho/n", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * OldValue is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldValueMissing() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", null,"cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * OldValue is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldValueEmptyString() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * OldValue length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldValueLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.ANNOTATION_VALUE + 1; i++) {
			big = big + "a";
		}
		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", big,"cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * OldValue contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldValueInvalidCharacters() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "do/g","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}
	/**
	 * Test used to check that ValidateException is thrown when
	 * NewValue is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewValueMissing() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog", null);
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * NewValue is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewValueEmptyString() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * NewValue length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewValueLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.ANNOTATION_VALUE + 1; i++) {
			big = big + "a";
		}
		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog", big);
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * NewValue contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewValueInvalidCharacters() throws ValidateException {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","ca/t");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
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

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that conversion to and from JSON
	 * works properly.
	 */
	@Test
	public void testConvertJSON() {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		String compare = gson.toJson(c);

		assertEquals(j, compare);

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param privileges string.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String name, String oldName, String newName) {

	    JsonObject j = new JsonObject();
		j.addProperty("name", name);
		j.addProperty("oldValue", oldName);
		j.addProperty("newValue", newName);

		return j.toString();
	}

}
