package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseAccessor;
import database.constants.MaxSize;
import database.containers.Genome;
import response.DeleteGenomeReleaseResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to delete a genome release.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class DeleteGenomeReleaseCommand extends Command {

	private String genomeVersion = null;

	private String specie = null;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param specie name of specie
	 * @param genomeVersion version
	 */
	public DeleteGenomeReleaseCommand(String specie, String genomeVersion) {

		this.genomeVersion = genomeVersion;
		this.specie = specie;

	}

	/**
	 * Method used to validate the DeleteGenomeReleaseCommand.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if (genomeVersion == null || specie == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome " +
					"version was missing.");

		} else if (genomeVersion.equals("null") || specie.equals("null")) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome " +
					"version and/or specie was missing.");

		} else if(genomeVersion.length() > MaxSize.GENOME_VERSION ||
				genomeVersion.length() < 1) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome " +
					"version must be between 1 and "+
					database.constants.MaxSize.GENOME_VERSION +
					" characters long.");

		} else if(specie.length() > MaxSize.GENOME_SPECIES ||
				specie.length() < 1) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The species " +
					"must be between 1 and " +
					database.constants.MaxSize.GENOME_SPECIES +
					" characters long.");

		} else if(!hasOnlyValidCharacters(genomeVersion)) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in genome version. Valid characters are: " +
					validCharacters);

		} else if(!hasOnlyValidCharacters(specie)) {

			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Invalid characters in specie name. Valid characters are: "
							+ validCharacters);

		}

		return true;

	}

	/**
	 * method used to execute the command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<Genome> genomeReleases =
					db.getAllGenomReleasesForSpecies(specie);
			if(genomeReleases != null) {
				for(Genome g : genomeReleases) {
					if (g != null && g.genomeVersion.equals(this.genomeVersion)
							&& g.species.equals(this.specie)) {
						boolean result = db.removeGenomeRelease(genomeVersion);
						if(result) {
							return new
									DeleteGenomeReleaseResponse(StatusCode.OK);
						} else {
							return new ErrorResponse(StatusCode.BAD_REQUEST,
									"Could not delete genomrelease");
						}
					}
				}
			}
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Version " +
					genomeVersion + " or specie " + specie +
					" does not exist.");

		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if(db.isConnected()) {
				db.close();
			}
		}
	}

}
