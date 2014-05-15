package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.DeleteGenomeReleaseCommand;

/**
 * Class used to test the DeleteGenomeReleaseCommand class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommandTest {

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
	 * Test used to check that creation works and
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		DeleteGenomeReleaseCommand cmd = new DeleteGenomeReleaseCommand();
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that convertion to JSON works
	 * properly.
	 */
	@Test
	public void testDeleteGenomeReleaseJSON() {

		String json = "{\"genomeversion\":\"GV 1.0\",\"specie\":\"human\"}";
		final Command cmd = gson.fromJson(json, DeleteGenomeReleaseCommand.class);
	    String json2 = gson.toJson(cmd);

	    assertEquals(json2, json);

	}

	/**
	 * Method used to test the validate method.
	 */
	@Test
	public void testValidate() {

		fail("Not yet implemented");

	}

}
