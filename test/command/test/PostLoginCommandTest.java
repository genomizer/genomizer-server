package command.test;

import static org.junit.Assert.*;

import command.connection.PostLoginCommand;
import database.constants.MaxLength;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;
import command.ValidateException;

/**
 * Class used to test that the PostLoginCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PostLoginCommandTest {

	public Gson gson = null;

	/**
	 * Setup method to initiate gson builder.
	 */
	@Before
	public void setUp() {

	    final GsonBuilder builder = new GsonBuilder();
	    builder.excludeFieldsWithoutExposeAnnotation();
	    gson = builder.create();

	}

	/**
	 * Test login command creation.
	 */
	@Test
	public void testCreateLoginCommand() {

		PostLoginCommand lcmd = new PostLoginCommand();

		assertNotNull(lcmd);

	}

	/**
	 * Test login command creation of object with
	 * JSON string.
	 */
	@Test
	public void testLoginCommandJSON() {

	    String json = "{\"username\":\"uname\",\"password\":\"pw\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		String json2 = gson.toJson(lcmd);

		assertEquals(json, json2);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * password is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidatePasswordNull() throws ValidateException {

	    String json = "{\"username\":\"uname\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * username is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameNull() throws ValidateException {

		String json = "{\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Used to test that ValidateException is thrown if
	 * password length is empty string..
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidatePasswordEmptyString() throws ValidateException {

		String json = "{\"username\":\"uname\",\"password\":\"\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Used to test that ValidateException is thrown if
	 * username length is empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameEmptyString() throws ValidateException {

		String json = "{\"username\":\"\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when password length is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateWorkingPasswordLength() throws ValidateException {

	    String json = "{\"username\":\"uname\",\"password\":\"pass\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when username length is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateWorkingUsernameLength() throws ValidateException {

		String json = "{\"username\":\"M\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is
	 * when password length is greater than valid length.
	 *
	 * @throws ValidateException
	 */
	@Test (expected = ValidateException.class)
	public void testValidateInvalidPasswordLength() throws ValidateException {
		String pass = "";
		for(int i = 0; i < MaxLength.PASSWORD + 1;i++){
			pass += "i";
		}

		String json = "{\"username\":\"M\",\"password\":\""+pass+"\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when username length is greater than valid length.
	 *
	 * @throws ValidateException
	 */
	@Test (expected = ValidateException.class)
	public void testValidateInvalidUsernameLength() throws ValidateException {
		String user = "";
		for(int i = 0; i < MaxLength.USERNAME + 1;i++){
			user += "i";
		}

		String json = "{\"username\":\""+user+"\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when username is exactly one char
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateMinUsernameLength() throws ValidateException {

	    String json = "{\"username\":\"M\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		assertTrue(true);

	}



	/**
	 * Test used to check that ValidateException is thrown
	 * when invalid characters are used
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUsernameCharacters() throws ValidateException {

		String json = "{\"username\":\"��!?,:;[]{}\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
	}

	/**
	 * Test used to check that ValidateException is thrown
	 * when invalid characters are used
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidatePasswordCharacters() throws ValidateException {

		String json = "{\"username\":\"M\",\"password\":\"��!?,:;[]{}\"}";
		final Command lcmd = gson.fromJson(json, PostLoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
	}
}
