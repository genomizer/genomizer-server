package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import database.subClasses.UserMethods.UserType;
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
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that conversion to and from
	 * JSON works properly.
	 */
	@Test
	public void testConvertJSON() {

		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		String compare = gson.toJson(cmd);
		assertEquals(compare, json);

	}

	/**
	 * Test used to check that username validation works
	 * properly.
	 */
	@Test
	public void testValidateUsernameLength() throws ValidateException {

		String username = "";
		for(int i = 0; i < MaxLength.USERNAME+1; i++) {
			username = username + "a";
		}
		json = createJSON(username, "b", "c", "d", "e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);

		json = createJSON("","b","c","d","e");
		CreateUserCommand cmd2 = gson.fromJson(json, CreateUserCommand.class);
		cmd2.setFields("uri", "uuid", UserType.ADMIN);

		cmd.validate();
		cmd2.validate();
	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test
	public void testValidatePasswordLength() throws ValidateException {

		String password = "";
		for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
			password = password + "a";
		}
		json = createJSON("a", password,"c","d","e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);

		json = createJSON("a","","c","d","e");
		CreateUserCommand cmd2 = gson.fromJson(json, CreateUserCommand.class);
		cmd2.setFields("uri", "uuid", UserType.ADMIN);

		cmd.validate();
		cmd2.validate();

	}

	/**
	 * Test used to check that privileges validation works
	 * properly.
	 */
	@Test
	public void testValidatePrivilegesLength() throws ValidateException {

		String privilege = "";
		for(int i = 0; i < MaxLength.ROLE+1; i++) {
			privilege = privilege + "a";
		}
		json = createJSON("a","b",privilege,"d","e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);

		json = createJSON("a","b","","d","e");
		CreateUserCommand cmd2 = gson.fromJson(json, CreateUserCommand.class);
		cmd2.setFields("uri", "uuid", UserType.ADMIN);

		cmd.validate();
		cmd2.validate();

	}

	/**
	 * Test used to check that username does not contains
	 * any slashes.
	 */
	@Test
	public void testNoSlashesUserName() throws ValidateException {

		json = createJSON("a/b/c","b","c","d","e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);

		cmd.validate();

	}

	/**
	 * Test used to check a properly formatted creation.
	 */
	@Test
	public void testValidationProperlyFormatted() throws ValidateException {
		json = createJSON("a","b","c","d","e");
		CreateUserCommand cmd = gson.fromJson(json, CreateUserCommand.class);
		cmd.setFields("uri", "uuid", UserType.ADMIN);

		cmd.validate();

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		json = createJSON("a","b","c","d","e");
		CreateUserCommand c = gson.fromJson(json, CreateUserCommand.class);
		c.setFields("uri", "uuid", UserType.ADMIN);

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

		json = createJSON("a","b","c","d","e");
		CreateUserCommand c = gson.fromJson(json, CreateUserCommand.class);
		c.setFields("uri", "uuid", UserType.USER);

		c.validate();
		fail();
	}

	/**
	 * Used to create JSON objects.
	 *
	 * @param username the username.
	 * @param password the password.
	 * @param privilege the privileges.
	 * @param name the name.
	 * @param email the email.
	 * @return String with the JSON.
	 */
	private String createJSON(String username, String password,
							  String privilege, String name, String email) {

	    JsonObject j = new JsonObject();
		j.addProperty("username", username);
		j.addProperty("password", password);
		j.addProperty("privileges", privilege);
		j.addProperty("name", name);
		j.addProperty("email", email);

		return j.toString();

	}

}
