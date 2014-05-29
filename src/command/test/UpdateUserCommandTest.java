package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.UpdateUserCommand;

/**
 * Class used to test that UpdateUserCommand works
 * properly.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class UpdateUserCommandTest {

	private Gson gson = null;

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
	 * Test used to check that creation works and object
	 * created is not null.
	 */
	@Test
	public void testCreationNotNull() {

		UpdateUserCommand c = new UpdateUserCommand();
		String j = jsonBuilder("a","b","c","d","e","f");
		c = gson.fromJson(j, UpdateUserCommand.class);
		assertNotNull(c);

	}

	/**
	 * Test used to check that conversion to and from
	 * JSON works properly.
	 */
	@Test
	public void testConvertJSON() {

		UpdateUserCommand c = new UpdateUserCommand();
		String j = jsonBuilder("a","b","c","d","e","f");
		c = gson.fromJson(j, UpdateUserCommand.class);
		String compare = gson.toJson(c);
		assertEquals(j, compare);

	}

	/**
	 * Test used to check that validation returns false if
	 * privileges is null.
	 */
	@Test
	public void testValidateInputNotNull() {

		UpdateUserCommand c = new UpdateUserCommand();
		String j = jsonBuilder(null,"b","c","d","e","f");
		c = gson.fromJson(j, UpdateUserCommand.class);
		UpdateUserCommand c2 = new UpdateUserCommand();
		String j2 = jsonBuilder("a",null,"c","d","e","f");
		c2 = gson.fromJson(j2, UpdateUserCommand.class);
		UpdateUserCommand c3 = new UpdateUserCommand();
		String j3 = jsonBuilder("a","b",null,"d","e","f");
		c3 = gson.fromJson(j3, UpdateUserCommand.class);
		UpdateUserCommand c4 = new UpdateUserCommand();
		String j4 = jsonBuilder("a","b","c",null,"e","f");
		c4 = gson.fromJson(j4, UpdateUserCommand.class);

		assertFalse(c.validate());
		assertFalse(c2.validate());
		assertFalse(c3.validate());
		assertFalse(c4.validate());

	}

	/**
	 * Test used to check that validation works properly if
	 * everything is properly formatted.
	 */
	@Test
	public void testValidateProperlyFormatted() {

		UpdateUserCommand c = new UpdateUserCommand();
		String j = jsonBuilder("a","b","c","d","e","f");
		c = gson.fromJson(j, UpdateUserCommand.class);
		assertTrue(c.validate());

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param Old username.
	 * @param Old password.
	 * @param New username.
	 * @param New password.
	 * @param New name.
	 * @param New email.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String ou, String op, String nu, String np,
			String nn, String ne) {

	    JsonObject j = new JsonObject();
		j.addProperty("old_username", ou);
		j.addProperty("old_password", op);
		j.addProperty("new_username", nu);
		j.addProperty("new_password", np);
		j.addProperty("new_name", nn);
		j.addProperty("new_email", ne);

		return j.toString();

	}

}
