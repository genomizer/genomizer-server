package command.test;

import static org.junit.Assert.*;

import database.constants.MaxLength;
import org.junit.Test;
import command.GetGenomeReleaseSpeciesCommand;
import command.ValidateException;

/**
 * Test used to check that the GetGenomeReleaseSpeciesCommand
 * class works properly. The execute method is tested with other methods.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetGenomeReleaseSpeciesTest {

	/**
	 * Test used to check that creation works and object
	 * is not null.
	 */
	@Test
	public void testCreationNotNull() {

		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand("testing");

		assertNotNull(c);

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species is an empty string.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesEmptyString() throws ValidateException {

		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand("");
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species is null.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesNotNull() throws ValidateException {

		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand(null);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is thrown when
	 * species length is to long.
	 *
	 * @throws ValidateException
	 */
	@Test(expected = ValidateException.class)
	public void testValidateSpeciesLength() throws ValidateException {

		String big = "";
		for(int i = 0; i < MaxLength.GENOME_SPECIES + 1; i++) {
			big = big + "a";
		}
		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand(big);
		c.validate();

		fail("Expected ValidateException to be thrown.");

	}

	/**
	 * Test used to check that ValidateException is not thrown when
	 * everything is properly formatted.
	 *
	 * @throws ValidateException
	 */
	@Test
	public void testVaidateproperlyFormatted() throws ValidateException {

		GetGenomeReleaseSpeciesCommand c = new GetGenomeReleaseSpeciesCommand("properly");
		c.validate();

		assertTrue(true);

	}

}
