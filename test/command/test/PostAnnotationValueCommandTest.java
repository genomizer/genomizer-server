package command.test;

import static org.junit.Assert.*;

import command.annotation.PostAnnotationValueCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.ValidateException;
import database.constants.MaxLength;

/**
 * Test class used to check that the PostAnnotationValueCommand class works
 * properly. The execute method is not tested as it requires a connection to a
 * external database.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostAnnotationValueCommandTest {
	private Gson gson;

	/**
	 * Setup method to initiate the GSON object.
	 */
	@Before
	public void setUp() {
	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();
	}

	/**
	 * Test used to check that ValidateException is thrown if the name field is
	 * missing altogether.
	 * @throws ValidateException if the name field is missing altogether.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameNotNull() throws ValidateException {

		String json = "{\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if name
	 * length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_LABEL + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"" + big +
				"\",\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if name
	 * has invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"spec/ies\",\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueNotNull() throws ValidateException {

		String json = "{\"name\":\"species\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.ANNOTATION_VALUE + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"species\",\"value\":\"" + big +
				"\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"species\",\"value\":\"m*/ouse!\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown everything
	 * is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);
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

		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.USER);

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

		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		PostAnnotationValueCommand c = gson.fromJson(json, PostAnnotationValueCommand.class);
		c.setFields("uri", "uuid", UserType.GUEST);

		c.validate();
		fail();
	}

}
