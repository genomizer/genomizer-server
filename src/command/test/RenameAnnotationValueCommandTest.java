package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import command.RenameAnnotationValueCommand;

/**
 * Class used to check that RenameAnnotationValueCommand class
 * works properly.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class RenameAnnotationValueCommandTest {

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
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		assertNotNull(c);

	}

	/**
	 * Test used to check that conversion to and from JSON
	 * works properly.
	 */
	@Test
	public void testConvertJSON() {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		String compare = gson.toJson(c);
		assertEquals(j, compare);

	}

	/**
	 * Test used to check that validate always returns true.
	 */
	@Test
	public void testValidateAlwaysTrue() {

		RenameAnnotationValueCommand c = new RenameAnnotationValueCommand();
		String j = jsonBuilder("Jhon", "dog","cat");
		c = gson.fromJson(j, RenameAnnotationValueCommand.class);
		assertTrue(c.validate());

	}

	/**
	 * Method used to build a JSON and return it as a string.
	 *
	 * @param privileges string.
	 * @return JSON formatted string.
	 */
	private String jsonBuilder(String name, String oldName, String newName) {

	    JsonObject j = new JsonObject();
		j.addProperty("name", name);
		j.addProperty("oldValue", oldName);
		j.addProperty("newValue", newName);

		return j.toString();
	}

}
