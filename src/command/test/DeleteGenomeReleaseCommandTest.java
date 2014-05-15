package command.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import command.Command;
import command.CommandHandler;
import command.DeleteGenomeReleaseCommand;

//TODO: Add more tests on validation.

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

		DeleteGenomeReleaseCommand cmd = new DeleteGenomeReleaseCommand("/123/345");
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that the validate method returns false
	 * if genomeVersion is null.
	 */
	@Test
	public void testValidateGenomeVersionNull() {

		String json = "{\"genomeVersion\":null,\"specie\":\"human\"}";
		final Command cmd = gson.fromJson(json, DeleteGenomeReleaseCommand.class);

		assertFalse(cmd.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if specie is null.
	 */
	@Test
	public void testValidateSpecieNull() {

		String json = "{\"genomeVersion\":\"GV 1.0\",\"specie\":null}";
		final Command cmd = gson.fromJson(json, DeleteGenomeReleaseCommand.class);

		assertFalse(cmd.validate());

	}

}

