package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddGenomeReleaseCommand;
import command.Command;
import command.ValidateException;
import database.constants.MaxSize;

/**
 * Class used to test the AddGenomeRelease class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommandTest {

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
	 * Test used to check that the created command
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		AddGenomeReleaseCommand cmd = new AddGenomeReleaseCommand();
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that convertion to JSON works
	 * properly.
	 */
	@Test
	public void testAddGenomeReleaseJSON() {

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
	    String json2 = gson.toJson(cmd);

	    assertEquals(json2, json);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName is an empty string.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameEmptyString() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when FileName
	 * is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameNull() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when FileName
	 * is null with JSON format totaly missing.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameNullSecondTest() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"human\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * specie is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieEmptyString() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * specie is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieNull() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":null,\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * genome version is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionEmptyString() throws ValidateException {

		String json = "{\"version\":\"\",\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}
	/**
	 * Test used to check that ValidateException is thrown when
	 * genome version is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionNull() throws ValidateException {

		String json = "{\"version\":null,\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that ValidateException is thrown if
	 * FileName length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameLength() throws ValidateException {

		String big_filename = "";
		for(int i = 0; i < MaxSize.GENOME_FILEPATH + 1; i++) {
			big_filename = big_filename + "A";
		}
		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"" + big_filename +
				"\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that ValidateException is thrown if
	 * specie length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieLength() throws ValidateException {

		String big_specie = "";
		for(int i = 0; i < MaxSize.GENOME_SPECIES + 1; i++) {
			big_specie = big_specie + "A";
		}
		String json = "{\"version\":\"hx16\",\"species\":\"" + big_specie +
				"\",\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that ValidateException is thrown if
	 * genome version length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionLength() throws ValidateException {

		String big_gv = "";
		for(int i = 0; i < MaxSize.GENOME_VERSION + 1; i++) {
			big_gv = big_gv + "A";
		}
		String json = "{\"version\":\"" + big_gv +
				"hx16\",\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that should validate true since everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperlyFormatted() throws ValidateException {

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		cmd.validate();

		assertTrue(true);

	}

}
