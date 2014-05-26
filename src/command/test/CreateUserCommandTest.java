package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.CreateUserCommand;
import database.constants.MaxSize;

/**
 * Class used to test that CreateUserCommand class works
 * properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class CreateUserCommandTest {

	private Gson gson = null;
	private String json = null;

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
	 * Test used to check that creation is not null.
	 */
	@Test
	public void testCreationNotNull() {

		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that conversion to and from
	 * JSON works properly.
	 */
	@Test
	public void testConvertJSON() {

		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);
		String compare = gson.toJson(cmd);
		assertEquals(compare, json);

	}

	/**
	 * Test used to check that username validation works
	 * properly.
	 */
	@Test
	public void testValidateUsernameLength() {

		String username = "";
		for(int i = 0; i < MaxSize.USERNAME+1; i++) {
			username = username + "a";
		}
		json = createJSON(username,"b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("","b","c","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test
	public void testValidatePasswordLength() {

		String password = "";
		for(int i = 0; i < MaxSize.PASSWORD+1; i++) {
			password = password + "a";
		}
		json = createJSON("a",password,"c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("a","","c","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that privileges validation works
	 * properly.
	 */
	@Test
	public void testValidatePrivilegesLength() {

		String priv = "";
		for(int i = 0; i < MaxSize.ROLE+1; i++) {
			priv = priv + "a";
		}
		json = createJSON("a","b",priv,"d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("a","b","","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that username does not contains
	 * any slashes.
	 */
	@Test
	public void testNoSlashesUserName() {

		json = createJSON("a/b/c","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		assertFalse(cmd.validate());

	}

	/**
	 * Test used to check a properly formatted creation.
	 */
	@Test
	public void testValidationProperlyFormatted() {
		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		assertTrue(cmd.validate());

	}

	/**
	 * Used to create JSON objects.
	 *
	 * @param the username.
	 * @param the password.
	 * @param the privileges.
	 * @param the name.
	 * @param the email.
	 * @return String with the JSON.
	 */
	private String createJSON(String un, String pw, String p, String n, String e) {

	    JsonObject j = new JsonObject();
		j.addProperty("username", un);
		j.addProperty("password", pw);
		j.addProperty("privileges", p);
		j.addProperty("name", n);
		j.addProperty("email", e);

		return j.toString();

	}

}
