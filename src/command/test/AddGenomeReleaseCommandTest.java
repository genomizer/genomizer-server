package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.AddGenomeReleaseCommand;
import command.Command;

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

		String json = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"GV 1.0\"}";
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

		String json = "{\"fileName\":null,\"specie\":\"human\",\"genomeVersion\":\"GV 1.0\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"\",\"specie\":\"human\",\"genomeVersion\":\"GV 1.0\"}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if specie is null or empty string.
	 */
	@Test
	public void testValidateSpecieNull() {

		String json = "{\"fileName\":\"abc123\",\"specie\":null,\"genomeVersion\":\"GV 1.0\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"abc123\",\"specie\":\"\",\"genomeVersion\":\"GV 1.0\"}";
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

		String json = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":null}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"\"}";
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

		String big_filename = "Start";
		for(int i = 0; i < 200; i++) {
			big_filename = big_filename + i;
		}

		String json = "{\"fileName\":\"" + big_filename +
				"\",\"specie\":\"human\",\"genomeVersion\":\"123\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"\",\"specie\":\"human\",\"genomeVersion\":\"123\"}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if lengths
	 * are not following MaxSize specifications for specie.
	 */
	@Test
	public void testValidateSpecieLength() {

		String big_specie = "Start";
		for(int i = 0; i < 200; i++) {
			big_specie = big_specie + i;
		}

		String json = "{\"fileName\":\"abc123\",\"specie\":\"" + big_specie +
				"\",\"genomeVersion\":\"123\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"abc123\",\"specie\":\"\",\"genomeVersion\":\"123\"}";
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

		String big_gv = "Start";
		for(int i = 0; i < 200; i++) {
			big_gv = big_gv + i;
		}

		String json = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"" + big_gv +
				"\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		String json2 = "{\"fileName\":\"abc123\",\"specie\":\"human\",\"genomeVersion\":\"\"}";
		final Command cmd2 = gson.fromJson(json2, AddGenomeReleaseCommand.class);

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that should validate true since everything is properly formatted.
	 */
	@Test
	public void testValidateProperlyFormatted() {

		String json = "{\"fileName\":\"abc123\",\"specie\":\"Hello\",\"genomeVersion\":\"GV 1.0\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);

		assertTrue(cmd.validate());

	}

}

