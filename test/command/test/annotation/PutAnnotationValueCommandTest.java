package command.test.annotation;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.annotation.PutAnnotationValueCommand;
import command.ValidateException;

/**
 * Class used to check that RenameAnnotationValueCommand class
 * works properly. Execute is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PutAnnotationValueCommandTest {

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

		PutAnnotationValueCommand c;
		String j = jsonBuilder("John", "dog","cat");
		c = gson.fromJson(j, PutAnnotationValueCommand.class);

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

		String j = jsonBuilder(null, "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			big = big + "a";
		}
		String j = jsonBuilder(big, "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("Joh/n", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", null,"cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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
		for(int i = 0; i < MaxLength.ANNOTATION_VALUE + 1; i++) {
			big = big + "a";
		}
		String j = jsonBuilder("John", big,"cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "do/g","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "dog", null);
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "dog","");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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
		for(int i = 0; i < MaxLength.ANNOTATION_VALUE + 1; i++) {
			big = big + "a";
		}
		String j = jsonBuilder("John", "dog", big);
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "dog","ca/t");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
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

		String j = jsonBuilder("John", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.ADMIN);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		String json = jsonBuilder("John", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(json, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.USER);

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

		String json = jsonBuilder("John", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(json, PutAnnotationValueCommand.class);
		c.setFields("hello", null, null, UserType.GUEST);

		c.validate();
		fail();
	}

	/**
	 * Test used to check that conversion to and from JSON
	 * works properly.
	 */
	@Test
	public void testConvertJSON() {

		String j = jsonBuilder("Jhon", "dog","cat");
		PutAnnotationValueCommand c = gson.fromJson(j, PutAnnotationValueCommand.class);
		String compare = gson.toJson(c);

		assertEquals(j, compare);

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param name And the other strings to insert into the JSON.
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
