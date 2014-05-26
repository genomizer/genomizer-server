package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddAnnotationFieldCommand;
import command.Command;
import database.MaxSize;

/**
 * Test class used to check that the AddAnnotationFieldCommand
 * class is working properly on a unit level.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationFieldCommandTest {

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
	 * Test creating object and that it's not null.
	 */
	@Test
	public void testCreateAddAnnotationFieldCommand() {

		AddAnnotationFieldCommand aafc = new AddAnnotationFieldCommand();
		assertNotNull(aafc);

	}

	@Test
	public void testHasOnlyValidCharacters(){
		AddAnnotationFieldCommand aafc = new AddAnnotationFieldCommand();

		String valid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
		String invalid = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 åäö";
		String invalid2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 !#¤%&/()";

		assertTrue(aafc.hasOnlyValidCharacters(valid));
		assertFalse(aafc.hasOnlyValidCharacters(invalid));
		assertFalse(aafc.hasOnlyValidCharacters(invalid2));
	}

	/**
	 * Test that checks that creating a freetext annotation works
	 * properly.
	 */
	@Test
	public void testAddAnnotationFieldCommandFreetextJSON() {

	    String json = "{\"name\":\"species\",\"type\":[\"freetext\"],\"default\":null,\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    String jsonCompare = "{\"name\":\"species\",\"type\":[\"freetext\"],\"forced\":true}";
	    String json2 = gson.toJson(aafc);

	    assertEquals(json2, jsonCompare);

	}

	/**
	 * Test AddAnnotationField command creation of object with
	 * JSON string and then check that they are equal.
	 */
	@Test
	public void testAddAnnotationFieldCommandJSON() {

	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
		final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
		String json2 = gson.toJson(aafc);
		assertEquals(json2, json);

	}

	/**
	 * Test for the validation of validation of missing objects
	 * that were made when JSON was serialized.
	 */
	@Test
	public void testValidationNullValues() {

	    String json = "{\"name\":\"\",\"type\":[\"fly\",\"rat\",\"human\"],\"forced\":true}";
	    String json2 = "{\"type\":[\"fly\",\"rat\",\"human\"],\"forced\":true}";
	    String json3 = "{\"name\":\"species\",\"type\":[],\"forced\":true}";
	    String json4 = "{\"name\":\"species\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    final Command aafc2 = gson.fromJson(json2, AddAnnotationFieldCommand.class);
	    final Command aafc3 = gson.fromJson(json3, AddAnnotationFieldCommand.class);
	    final Command aafc4 = gson.fromJson(json4, AddAnnotationFieldCommand.class);
	    assertFalse(aafc.validate());
	    assertFalse(aafc2.validate());
	    assertFalse(aafc3.validate());
	    assertFalse(aafc4.validate());

	}

	/**
	 * Test to check if name validation works properly.
	 */
	@Test
	public void testValidateNameIsToLong() {

		String toLong = "";
		for(int i = 0; i < MaxSize.ANNOTATION_LABEL + 1; i++) {
			toLong = toLong + "A";
		}
	    String json = "{\"name\":\"" + toLong +
	    		"\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
		assertFalse(aafc.validate());

	}

	/**
	 * Test to check if there are no types to select from.
	 */
	@Test
	public void testValidateNoTypes() {

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

	    String json = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
		assertTrue(aafc.validate());

	}

	@Test
	public void testInvalidCharacters(){
		String json = "{\"name\":\"spec ies\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc = gson.fromJson(json, AddAnnotationFieldCommand.class);
	    assertTrue(aafc.validate());

	    String json2 = "{\"name\":\"speciesö\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human\",\"forced\":true}";
	    final Command aafc2 = gson.fromJson(json2, AddAnnotationFieldCommand.class);
	    assertFalse(aafc2.validate());

	    String json3 = "{\"name\":\"species\",\"type\":[\"fly\",\"rat\",\"human\"],\"default\":\"human!!!!\",\"forced\":true}";
	    final Command aafc3 = gson.fromJson(json3, AddAnnotationFieldCommand.class);
	    assertFalse(aafc3.validate());




	}

}
