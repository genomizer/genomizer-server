package command.test;

import static org.junit.Assert.*;

import org.junit.Test;

import command.Command;
import command.DeleteGenomeReleaseCommand;


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
		final Command cmd2 = new DeleteGenomeReleaseCommand("Human", "");

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test used to check that the validate method returns false
	 * if specie is null.
	 */
	@Test
	public void testValidateSpecieNull() {

		final Command cmd = new DeleteGenomeReleaseCommand("null", "GRelease");
		final Command cmd2 = new DeleteGenomeReleaseCommand("", "GRelease");

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if wrong
	 * lengths on genome version.
	 */
	@Test
	public void testValidateGenomeVersionLength() {

		String big = "Start";
		for(int i = 0; i < 50; i++) {
			big = big + i;
		}

		final Command cmd = new DeleteGenomeReleaseCommand("Specie", big);
		final Command cmd2 = new DeleteGenomeReleaseCommand("Specie", "");

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that validate returns false if wrong
	 * lengths on specie.
	 */
	@Test
	public void testValidateSpecieLength() {

		String big = "Start";
		for(int i = 0; i < 50; i++) {
			big = big + i;
		}

		final Command cmd = new DeleteGenomeReleaseCommand(big, "GRelease");
		final Command cmd2 = new DeleteGenomeReleaseCommand("", "GRelease");

		assertFalse(cmd.validate());
		assertFalse(cmd2.validate());

	}

	/**
	 * Test that checks that if specie and genomeVersion is
	 * properly formatted, then the validate returns true.
	 */
	@Test
	public void testValidateProperFormatted() {

		final Command cmd = new DeleteGenomeReleaseCommand("Specie", "GRelease");

		assertTrue(cmd.validate());

	}

}
