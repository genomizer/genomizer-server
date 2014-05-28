package command.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import command.AddAnnotationValueCommand;
import command.ValidateException;
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

		AddAnnotationValueCommand c = new AddAnnotationValueCommand();

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown if name
	 * is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameNotNull() throws ValidateException {

		String json = "{\"value\":\"mouse\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if name
	 * length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxSize.ANNOTATION_LABEL + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"" + big +
				"\",\"value\":\"mouse\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if name
	 * has invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateNameInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"spec/ies\",\"value\":\"mouse\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * is missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueNotNull() throws ValidateException {

		String json = "{\"name\":\"species\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueLengthToLong() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxSize.ANNOTATION_VALUE + 1; i++) {
			big = big + "A";
		}
		String json = "{\"name\":\"species\",\"value\":\"" + big +
				"\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if value
	 * contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateValueInvalidCharacters() throws ValidateException {

		String json = "{\"name\":\"species\",\"value\":\"m*/ouse!\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown everything
	 * is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String json = "{\"name\":\"species\",\"value\":\"mouse\"}";
		AddAnnotationValueCommand c = new AddAnnotationValueCommand();
		c = gson.fromJson(json, AddAnnotationValueCommand.class);
		c.validate();

		assertTrue(true);

	}

}
