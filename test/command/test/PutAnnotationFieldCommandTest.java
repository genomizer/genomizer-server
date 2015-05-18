package command.test;

import static org.junit.Assert.*;

import command.annotation.PutAnnotationFieldCommand;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.ValidateException;

/**
 * Class used to test that PutAnnotationFieldCommand class
 * works properly. Execute method is not tested here.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PutAnnotationFieldCommandTest {

	private Gson gson;

	/**
	 * Setup method used to create the GSONbuilder.
	 */
	@Before
	public void setup() {

		final GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();

	}

	/**
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("One","Two"), PutAnnotationFieldCommand.class);

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * oldName is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldNameNotNull() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder(null,"Two"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * oldName is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldNameEmptyString() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("","Two"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * oldName contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldNameInvalidCharacters() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("O/*ne","Two"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * OldName length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateOldNameLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			big = big + "a";
		}
		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder(big,"Two"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * newName is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewNameNotNull() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("One",null), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * newName is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewNameEmptyString() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("One",""), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * newName contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewNameInvalidCharacters() throws ValidateException {

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("One","Tw/*o"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * newName length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNewNameLengthNotToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			big = big + "a";
		}
		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("One",big), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
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

		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("Species","Mouse"), PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.ADMIN);
		c.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that converting between JSON and object
	 * works properly.
	 */
	@Test
	public void testConvertJSON() {

		String json = jsonBuilder("Species","Mouse");
		PutAnnotationFieldCommand c = gson.fromJson(jsonBuilder("Species","Mouse"), PutAnnotationFieldCommand.class);
		String compare = gson.toJson(c);

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

		String json = jsonBuilder("Species","Mouse");
		PutAnnotationFieldCommand c = gson.fromJson(json, PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.USER);

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

		String json = jsonBuilder("Species","Mouse");
		PutAnnotationFieldCommand c = gson.fromJson(json, PutAnnotationFieldCommand.class);
		c.setFields("hello", "", null, UserType.GUEST);

		c.validate();
		fail();
	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param oldName to insert into JSON body.
	 * @param newName to insert into JSON body.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String oldName, String newName) {

	    JsonObject j = new JsonObject();
		j.addProperty("oldName", oldName);
		j.addProperty("newName", newName);

		return j.toString();

	}

}
