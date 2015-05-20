package command.test;

import static org.junit.Assert.*;

import command.genomerelease.PostGenomeReleaseCommand;
import database.subClasses.UserMethods.UserType;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.ValidateException;
import database.constants.MaxLength;

/**
 * Class used to test the AddGenomeRelease class.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class PostGenomeReleaseCommandTest {

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

		PostGenomeReleaseCommand cmd = new PostGenomeReleaseCommand();
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that convertion to JSON works
	 * properly.
	 */
	@Test
	public void testAddGenomeReleaseJSON() {

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"],\"checkSumsMD5\":[]}";
		final Command cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
	    String json2 = gson.toJson(cmd);

	    assertEquals(json, json2);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * FileName is an empty string.
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateFileNameEmptyString() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"\",\"nameOfFile2\",\"nameOfFile3\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesEmptyString() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"\",\"files\":[\"nameOfFile1\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * species contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesInvalidCharacters() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":\"not/valid\",\"files\":[\"nameOfFile1\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown if
	 * genome version contains invalid characters.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionInvalidCharacters() throws ValidateException {

		String json = "{\"version\":\"hx/16\",\"species\":\"valid\",\"files\":[\"nameOfFile1\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesNull() throws ValidateException {

		String json = "{\"version\":\"hx16\",\"species\":null,\"files\":[\"nameOfFile1\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		for(int i = 0; i < MaxLength.GENOME_FILEPATH + 1; i++) {
			big_filename = big_filename + "A";
		}
		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"" + big_filename +
				"\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test that checks that ValidateException is thrown if
	 * species length is to big.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesLength() throws ValidateException {

		String big_species = "";
		for(int i = 0; i < MaxLength.GENOME_SPECIES + 1; i++) {
			big_species = big_species + "A";
		}
		String json = "{\"version\":\"hx16\",\"species\":\"" + big_species +
				"\",\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		for(int i = 0; i < MaxLength.GENOME_VERSION + 1; i++) {
			big_gv = big_gv + "A";
		}
		String json = "{\"version\":\"" + big_gv +
				"hx16\",\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
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
		PostGenomeReleaseCommand cmd = gson.fromJson(json, PostGenomeReleaseCommand.class);
		cmd.setFields("hello", null, null, UserType.ADMIN);
		cmd.validate();

		assertTrue(true);

	}

	/**
	 * Test used to check that ValidateException is not thrown
	 * when the user have the required rights.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testHavingRights() throws ValidateException {

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		PostGenomeReleaseCommand c = gson.fromJson(json, PostGenomeReleaseCommand.class);
		c.setFields("hello", null, null, UserType.USER);

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

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		PostGenomeReleaseCommand c = gson.fromJson(json, PostGenomeReleaseCommand.class);
		c.setFields("hello", null, null, UserType.GUEST);

		c.validate();
		fail();
	}

}
