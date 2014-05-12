package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddAnnotationFieldCommand;
import command.Command;

/**
 * Test class used to test the AddAnnotationFieldCommand
 * class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldCommandTest {

	//Builder used with almost all tests.
	public Gson gson = null;

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
	 * Test creating and not null.
	 */
	@Test
	public void testCreateAddAnnotationFieldCommand() {

		AddAnnotationFieldCommand aafc = new AddAnnotationFieldCommand();
		assertNotNull(aafc);

	}

	/**
	 * Test that checks that creating a freetext annotation works
	 * properly.
	 */
	@Test
	public void testAddAnnotationFieldCommandFreetextJSON() {

	    //Create JSON input, serialize, de-serialize and then check for equality.
	    String json = "{\"name\":\"species\",\"type\":[\"freetext\"],\"default\":null,\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    String json2 = gson.toJson(aafc);

	    assertEquals(json2, json);

	}

	/**
	 * Test AddAnnotationField command creation of object with
	 * JSON string and then check that they are equal.
	 */
	@Test
	public void testAddAnnotationFieldCommandJSON() {

	    //Create JSON input, serialize, de-serialize and then check for equality.
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
		String json2 = gson.toJson(aafc);

		assertEquals(json2, json);

	}

	//TODO: Change assert when validate is working properly.
	/**
	 * Test for the validation of validation of missing objects
	 * that were made when JSON was serialized.
	 */
	@Test
	public void testValidationNullValues() {
		/* In this test, default is removed from the
		 * JSON string before serialization.
		 */

		/*
		//Create input string with null values.
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);

		assertFalse(aafc.validate());
		*/
		fail("Not yet implemented.");

	}

	/**
	 * Test to check if name validation works properly.
	 */
	@Test
	public void testValidateNameIsToLong() {

		//Create input string with null values.
	    String json = "{\"name\":\"This is a long name. Really long. Probably to long.\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);

		assertFalse(aafc.validate());

	}

	/**
	 * Test to check if there are no types to select from.
	 */
	@Test
	public void testValidateNoTypes() {

		//Create input string with no values in type.
		String json = "{\"name\":\"species\",\"type\":[],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);

		assertFalse(aafc.validate());

	}

	/**
	 * Test to check that validation code validates correct when
	 * JSON string is properly formatted.
	 */
	@Test
	public void testValidationProperJSON() {

		//Create properly formatted JSON string.
	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);

		assertTrue(aafc.validate());

	}

}
