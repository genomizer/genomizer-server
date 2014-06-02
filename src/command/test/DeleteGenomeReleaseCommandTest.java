package command.test;

import static org.junit.Assert.*;

import org.junit.Test;
import command.Command;
import command.DeleteGenomeReleaseCommand;
import command.ValidateException;

/**
 * Class used to test that the DeleteGenomeReleaseCommand class
 * works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
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
	 * Test used to check that validate method throws ValidateException
	 * when genome version is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionNull() throws ValidateException {

		final Command cmd = new DeleteGenomeReleaseCommand("Human", "null");
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that validate method throws ValidateException
	 * when genome version is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionEmptyString() throws ValidateException {

		final Command cmd = new DeleteGenomeReleaseCommand("Human", "");
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that validate method throws ValidateException
	 * when specie is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieNull() throws ValidateException {

		final Command cmd = new DeleteGenomeReleaseCommand("null", "GRelease");
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test used to check that validate method throws ValidateException
	 * when genome version is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieEmptyString() throws ValidateException {

		final Command cmd = new DeleteGenomeReleaseCommand("", "GRelease");
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that ValidateException is thrown when the
	 * genome version length is to large.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateGenomeVersionLength() throws ValidateException {

		String big = "Start";
		for(int i = 0; i < database.constants.MaxSize.GENOME_VERSION + 1; i++) {
			big = big + i;
		}
		final Command cmd = new DeleteGenomeReleaseCommand("Specie", big);
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that ValidateException is thrown when the
	 * specie length is to large.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpecieLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < database.constants.MaxSize.GENOME_SPECIES + 1; i++) {
			big = big + "a";
		}
		final Command cmd = new DeleteGenomeReleaseCommand(big, "GRelease");
		cmd.validate();

		fail("Expected ValidateException.");

	}

	/**
	 * Test that checks that if specie and genomeVersion is
	 * properly formatted, then the validate returns true.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testValidateProperFormatted() throws ValidateException {

		final Command cmd = new DeleteGenomeReleaseCommand("Specie", "GRelease");
		cmd.validate();

		assertTrue(true);

	}

}
