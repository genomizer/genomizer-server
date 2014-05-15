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

		String json = "{\"genomeversion\":\"GV 1.0\",\"species\":\"human\",\"filename\":\"abc123\"}";
		final Command cmd = gson.fromJson(json, AddGenomeReleaseCommand.class);
	    String json2 = gson.toJson(cmd);

	    assertEquals(json2, json);

	}

	/**
	 * Test used to check that the validate method.
	 */
	@Test
	public void testValidate() {

		fail("Not yet implemented");

	}

}
