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
 * Class used to test the LoginCommand class.
 *
 * @author tfy09jnn
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
	 * Used to test the validate method if password
	 * is not long enough.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateToSmallPasswordLength() throws ValidateException {

	    String json = "{\"username\":\"uname\",\"password\":\"\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertFalse(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * password is just big enough.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateWorkingPasswordLength() throws ValidateException {

	    String json = "{\"username\":\"uname\",\"password\":\"pass\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertTrue(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * user name is to short in length.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateToSmallUsernameLength() throws ValidateException {

	    String json = "{\"username\":\"\",\"password\":\"password\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertFalse(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * user name is just big enough.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateWorkingUsernameLength() throws ValidateException {

	    String json = "{\"username\":\"M\",\"password\":\"pass\"}";
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertTrue(lcmd.validate());

	}

}
