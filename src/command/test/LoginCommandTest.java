package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.LoginCommand;

/**
 * Class used to test the LoginCommand class.
 *
 * @author tfy09jnn
 * @version 1.1
 */
public class LoginCommandTest {

	//Builder used with almost all tests.
	public Gson gson = null;

	/**
	 * Setup method to initiate gson builder.
	 */
	@Before
	public void setUp() {

		//Create the builder.
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

	    //Create input
	    String json = "{\"username\":\"uname\",\"password\":\"pw\"}";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		String json2 = gson.toJson(lcmd);

		assertEquals(json, json2);

	}

	/**
	 * Used to test the validate method if password
	 * is not long enough.
	 */
	@Test
	public void testValidateToSmallPasswordLength() {

	    //Create input
	    String json = "{\"username\":\"uname\",\"password\":\"\"}";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertFalse(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * password is just big enough.
	 */
	@Test
	public void testValidateWorkingPasswordLength() {

	    //Create input
	    String json = "{\"username\":\"uname\",\"password\":\"pass\"}";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertTrue(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * user name is to short in length.
	 */
	@Test
	public void testValidateToSmallUsernameLength() {

	    //Create input
	    String json = "{\"username\":\"\",\"password\":\"password\"}";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertFalse(lcmd.validate());

	}

	/**
	 * Test used to test the validate method if the
	 * user name is just big enough.
	 */
	@Test
	public void testValidateWorkingUsernameLength() {

	    //Create input
	    String json = "{\"username\":\"M\",\"password\":\"pass\"}";

		//Create command with json.
		final Command lcmd = gson.fromJson(json, LoginCommand.class);

		assertTrue(lcmd.validate());

	}

}
