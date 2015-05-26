package command.test;

import static org.junit.Assert.*;

import command.ValidateException;
import command.admin.PostAdminUserCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import database.constants.MaxLength;

/**
 * Class used to test that PostUserCommand class works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
//TODO Activate tests when the validate method is implemented again
@Ignore
public class PostAdminUserCommandTest {

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
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that conversion to and from
	 * JSON works properly.
	 */
	@Test
	public void testConvertJSON() {

		json = createJSON("a","b","c","d","e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		String compare = gson.toJson(cmd);
		assertEquals(compare, json);

	}

	/**
	 * Test used to check that username validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidUsernameMinLength() throws ValidateException {

		json = createJSON("","b","c","d","e");
		PostAdminUserCommand cmd2 = gson.fromJson(json, PostAdminUserCommand.class);
		cmd2.setFields("uri", null, "uuid", UserType.ADMIN);

		cmd2.validate();
	}

	/**
	 * Test used to check that username validation works
	 * properly.
	 */
	@Test (expected = ValidateException.class)
	public void testValidateInvalidUsernameMaxLength() throws ValidateException {

		String username = "";
		for(int i = 0; i < MaxLength.USERNAME+1; i++) {
			username = username + "a";
		}
		json = createJSON(username, "b", "c", "d", "e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);

		cmd.validate();
	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidPasswordMaxLength() throws ValidateException {

		String password = "";
		for(int i = 0; i < MaxLength.PASSWORD+1; i++) {
			password = password + "a";
		}
		json = createJSON("a", password, "c", "d", "e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);

		cmd.validate();
	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidPasswordMinLength() throws ValidateException {

		json = createJSON("a","","c","d","e");
		PostAdminUserCommand cmd2 = gson.fromJson(json, PostAdminUserCommand.class);
		cmd2.setFields("uri", null, "uuid", UserType.ADMIN);

		cmd2.validate();
	}

	/**
	 * Test used to check privileges min length validation
	 */
	@Test
	public void testValidatePrivilegesLength() throws ValidateException {
		json = createJSON("a","b","","d","e");
		PostAdminUserCommand cmd2 = gson.fromJson(json, PostAdminUserCommand.class);
		cmd2.setFields("uri", null, "uuid", UserType.ADMIN);
		cmd2.validate();
	}
	/**
	 * Test used to check privileges Max length validation
	 */
	@Test (expected = ValidateException.class)
	public void testValidateInvalidPrivilegesMaxLength() throws ValidateException {
		String privilege = "";
		for(int i = 0; i < MaxLength.ROLE+1; i++) {
			privilege = privilege + "a";
		}
		json = createJSON("a","b",privilege,"d","e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);
		cmd.validate();
	}

	/**
	 * Test used to check that username validation works
	 * properly.
	 */
	@Test (expected = ValidateException.class)
	public void testValidateInvalidUsernameCharacters() throws ValidateException {

		json = createJSON("��!?,:;/[]{}", "b", "c", "d", "e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);

		cmd.validate();
	}

	/**
	 * Test used to check that password validation works
	 * properly.
	 */
	@Test(expected = ValidateException.class)
	public void testValidateInvalidPasswordCharacters() throws ValidateException {

		json = createJSON("a","��!?,:;/[]{}","c","d","e");
		PostAdminUserCommand cmd2 = gson.fromJson(json, PostAdminUserCommand.class);
		cmd2.setFields("uri", null, "uuid", UserType.ADMIN);
		cmd2.validate();
	}

	/**
	 * Test used to check a properly formatted creation.
	 */
	@Test (expected = ValidateException.class)
	public void testValidationProperlyFormatted() throws ValidateException {
		json = createJSON("a","b","c","d","e");
		PostAdminUserCommand cmd = gson.fromJson(json, PostAdminUserCommand.class);
		cmd.setFields("uri", null, "uuid", UserType.ADMIN);

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
		PostAdminUserCommand c = gson.fromJson(json, PostAdminUserCommand.class);
		c.setFields("uri", null, "uuid", UserType.ADMIN);

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
		PostAdminUserCommand c = gson.fromJson(json, PostAdminUserCommand.class);
		c.setFields("uri", null, "uuid", UserType.USER);

		c.validate();
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
