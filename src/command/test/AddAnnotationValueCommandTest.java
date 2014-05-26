package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddAnnotationValueCommand;
import database.constants.MaxSize;

/**
 * Class used to unit-test AddAnnotationValueCommand.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddAnnotationValueCommandTest {

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
	 * Test that checks that creation is not null.
	 */
	@Test
	public void testCreateNotNull() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		assertNotNull(cmd);

	}

	/**
	 * Test that checks that validate returns false if
	 * value is null.
	 */
	@Test
	public void testValidateNameNull() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		AddAnnotationValueCommand cmd2 = new AddAnnotationValueCommand();
		String json = "{\"name\":\"\",\"value\":\"mouse\"}";
		String json2 = "{\"value\":\"mouse\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		cmd2 = gson.fromJson(json2, AddAnnotationValueCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if
	 * value is null.
	 */
	@Test
	public void testValidateValueNotNull() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		AddAnnotationValueCommand cmd2 = new AddAnnotationValueCommand();
		String json = "{\"name\":\"species\",\"value\":\"\"}";
		String json2 = "{\"value\":\"\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		cmd2 = gson.fromJson(json2, AddAnnotationValueCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that validation on name size returns
	 * false if name is to long or short.
	 */
	@Test
	public void testValidateNameSize() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		AddAnnotationValueCommand cmd2 = new AddAnnotationValueCommand();
		String toLong = "";
		for(int i = 0; i < MaxSize.ANNOTATION_LABEL + 1; i++) {
			toLong = toLong + "A";
		}
		String json = "{\"name\":\"" + toLong +
				"\",\"value\":\"\"}";
		String json2 = "{\"name\":\"\",\"value\":\"\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		cmd2 = gson.fromJson(json2, AddAnnotationValueCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that validation on value size returns
	 * false if value is to long or short.
	 */
	@Test
	public void testValidateValueSize() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		AddAnnotationValueCommand cmd2 = new AddAnnotationValueCommand();
		String toLong = "";
		for(int i = 0; i < MaxSize.ANNOTATION_VALUE + 1; i++) {
			toLong = toLong + "A";
		}
		String json = "{\"name\":\"species\",\"value\":\"" + toLong +
				"\"}";
		String json2 = "{\"name\":\"\",\"value\":\"\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		cmd2 = gson.fromJson(json2, AddAnnotationValueCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that validation returns false
	 * if name or value contains slashes.
	 */
	@Test
	public void testValidateContainsSlashes() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		AddAnnotationValueCommand cmd2 = new AddAnnotationValueCommand();
		String json = "{\"name\":\"spec/ies\",\"value\":\"legit\"}";
		String json2 = "{\"name\":\"species\",\"value\":\"notl/egit\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		cmd2 = gson.fromJson(json2, AddAnnotationValueCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that a properly formatted
	 * JSON passes the validation.
	 */
	@Test
	public void testValidateProperFormatted() {

		AddAnnotationValueCommand cmd = new AddAnnotationValueCommand();
		String json = "{\"name\":\"species\",\"value\":\"legit\"}";
		cmd = gson.fromJson(json, AddAnnotationValueCommand.class);
		assertTrue(cmd.validate());

	}


}
