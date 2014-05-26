package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddGenomeReleaseCommand;
import command.Command;
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
	 * Test used to check that the validate method returns false
	 * if fileName is null or empty string.
	 */
	@Test
	public void testValidateFileNameNull() {

		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		String json2 = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);
		String json3 = "{\"version\":\"hx16\",\"species\":\"human\"}";
		final Command cmd3 = gson.fromJson(json3, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());
		assertFalse(cmd3.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if specie is null or empty string.
	 */
	@Test
	public void testValidateSpecieNull() {

		String json = "{\"version\":\"hx16\",\"species\":null,\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"version\":\"hx16\",\"species\":\"\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if genomeVersion is null.
	 */
	@Test
	public void testValidateGenomeVersionNull() {

		String json = "{\"version\":null,\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"version\":\"\",\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if lengths
	 * are not following MaxSize specifications for filename.
	 */
	@Test
	public void testValidateFileNameLength() {

		String big_filename = "";
		for(int i = 0; i < MaxSize.GENOME_FILEPATH+1; i++) {
			big_filename = big_filename + "A";
		}
		String json = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"" + big_filename +
				"\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
		String json2 = "{\"version\":\"hx16\",\"species\":\"human\",\"files\":[\"\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if lengths
	 * are not following MaxSize specifications for species.
	 */
	@Test
	public void testValidateSpecieLength() {

		String big_specie = "";
		for(int i = 0; i < MaxSize.GENOME_SPECIES + 1; i++) {
			big_specie = big_specie + "A";
		}

		String json = "{\"version\":\"hx16\",\"species\":\"" + big_specie +
				"\",\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"version\":\"hx16\",\"species\":\"\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if lengths
	 * are not following MaxSize specifications for genome version.
	 */
	@Test
	public void testValidateGenomeVersionLength() {

		String big_gv = "";
		for(int i = 0; i < MaxSize.GENOME_VERSION + 1; i++) {
			big_gv = big_gv + "A";
		}
		String json = "{\"version\":\"" + big_gv +
				"hx16\",\"species\":\"human\",\"files\":[\"nameOfFile1\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"version\":\"\",\"species\":\"human\",\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that should validate true since everything is properly formatted.
	 */
	@Test
	public void testValidateProperlyFormatted() {

		String json = "{\"genomeVersion\":\"hx16\",\"specie\":\"human\"," +
				"\"files\":[\"nameOfFile1\",\"nameOfFile2\",\"nameOfFile3\"]}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		assertTrue(cmd.validate());

	}

}
