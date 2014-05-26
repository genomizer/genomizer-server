package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.UpdateUserPrivilegesCommand;

/**
 * Class used to test that UpdateUserPrivilegesCommand works
 * properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class UpdateUserPrivilegesCommandTest {

	private Gson gson = null;

	/**
	 * Setup method to initiate GSON builder and
	 * initiate a Annotation class with JSON.
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

		UpdateUserPrivilegesCommand c = new UpdateUserPrivilegesCommand();
		String j = jsonBuilder("privileges");
		c = gson.fromJson(j, UpdateUserPrivilegesCommand.class);
		assertNotNull(c);
	}

	/**
	 * Test used to check that conversion to and from
	 * JSON works properly.
	 */
	@Test
	public void testConvertJSON() {

		UpdateUserPrivilegesCommand c = new UpdateUserPrivilegesCommand();
		String j = jsonBuilder("privileges");
		c = gson.fromJson(j, UpdateUserPrivilegesCommand.class);
		String compare = gson.toJson(c);
		assertEquals(j, compare);

	}

	/**
	 * Test used to check that validation returns false if
	 * privileges is null.
	 */
	@Test
	public void testValidatePrivilegesNotNull() {

		UpdateUserPrivilegesCommand c = new UpdateUserPrivilegesCommand();
		String j = jsonBuilder(null);
		c = gson.fromJson(j, UpdateUserPrivilegesCommand.class);
		assertFalse(c.validate());

	}

	/**
	 * Test used to check that validation works properly if
	 * everything is properly formatted.
	 */
	@Test
	public void testValidateProperlyFormatted() {

		UpdateUserPrivilegesCommand c = new UpdateUserPrivilegesCommand();
		String j = jsonBuilder("properly");
		c = gson.fromJson(j, UpdateUserPrivilegesCommand.class);
		assertTrue(c.validate());

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param privileges string.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String priv) {

	    JsonObject j = new JsonObject();
		j.addProperty("new_privileges", priv);

		return j.toString();
	}

}
