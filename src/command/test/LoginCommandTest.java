package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.Command;
import command.LoginCommand;
import command.ValidateException;

/**
 * Class used to test that the LoginCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class LoginCommandTest {

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

		LoginCommand lcmd = new LoginCommand();

		assertNotNull(lcmd);

	}

	/**
	 * Test login command creation of object with
	 * JSON string.
	 */
	@Test
	public void testLoginCommandJSON() {

	    String json = "{\"username\":\"uname\",\"password\":\"pw\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		String json2 = gson.toJson(lcmd);

		assertEquals(json, json2);

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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");

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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		lcmd.validate();

		assertTrue(true);

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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");

	}
	
	/**
	 * Test used to check that ValidateException is thrown if
	 * username contains invalid characters.
	 * 
	 * @throws ValidateException 
	 */
	@Test(expected = ValidateException.class)
	public void testValidateUserNameInvalidCharacters() throws ValidateException {
		
	    String json = "{\"username\":\"Hell///o\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		lcmd.validate();

		fail("Expected ValidateException to be thrown.");
		
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
		final Command lcmd = gson.fromJson(json, LoginCommand.class);
		lcmd.validate();

		assertTrue(true);

	}

}
