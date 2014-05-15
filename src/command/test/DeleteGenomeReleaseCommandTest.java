package command.test;

import static org.junit.Assert.*;
import org.junit.Test;

import command.Command;
import command.DeleteGenomeReleaseCommand;

//TODO: Add more tests on validation.

/**
 * Class used to test the DeleteGenomeReleaseCommand class.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommandTest {

	/**
	 * Test used to check that creation works and
	 * is not null.
	 */
	@Test
	public void testCreateNotNull() {

		DeleteGenomeReleaseCommand cmd = new DeleteGenomeReleaseCommand("Specie", "GenomeRelease");
		assertNotNull(cmd);

	}

	/**
	 * Test used to check that the validate method returns false
	 * if genomeVersion is null.
	 */
	@Test
	public void testValidateGenomeVersionNull() {

		final Command cmd = new DeleteGenomeReleaseCommand("Human", "null");
		assertFalse(cmd.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if specie is null.
	 */
	@Test
	public void testValidateSpecieNull() {

		final Command cmd = new DeleteGenomeReleaseCommand("null", "GRelease");
		assertFalse(cmd.validate());

	}

}

