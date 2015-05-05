package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.CreateUserCommand;
import database.constants.MaxLength;

/**
 * Class used to test that CreateUserCommand class works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
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
	@Test(expected =  ValidateException.class)
	public void testValidateUsernameLength() throws ValidateException{

		String username = "";
		for(int i = 0; i < MaxLength.USERNAME+1; i++) {
			username = username + "a";
		}
		json = createJSON(username,"b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("","b","c","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		cmd.validate();
		cmd2.validate();
	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidatePasswordLength() throws ValidateException {

		String password = "";
		for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
			password = password + "a";
		}
		json = createJSON("a",password,"c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("a","","c","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		cmd.validate();
		cmd2.validate();

	}

	/**
	 * Test used to check that privileges validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidatePrivilegesLength() throws ValidateException {

		String priv = "";
		for(int i = 0; i < MaxLength.ROLE+1; i++) {
			priv = priv + "a";
		}
		json = createJSON("a","b",priv,"d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		json = createJSON("a","b","","d","e");
		CreateUserCommand cmd2 = new CreateUserCommand();
		cmd2 = gson.fromJson(json, CreateUserCommand.class);

		cmd.validate();
		cmd2.validate();

	}

	/**
	 * Test used to check that username does not contains
	 * any slashes.
	 */
	@Test(expected=ValidateException.class)
	public void testNoSlashesUserName() throws ValidateException {

		json = createJSON("a/b/c","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		cmd.validate();

	}

	/**
	 * Test used to check a properly formatted creation.
	 */
	@Test
	public void testValidationProperlyFormatted() throws ValidateException {
		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = new CreateUserCommand();
		cmd = gson.fromJson(json, CreateUserCommand.class);

		cmd.validate();

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
