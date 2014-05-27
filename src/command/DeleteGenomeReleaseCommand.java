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
 * @author tfy09jnn, Hugo Källström
 * @version 1.1
 */
public class DeleteGenomeReleaseCommand extends Command {

	private String genomeVersion = null;

	private String specie = null;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param restful string to split.
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

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome version was missing.");

		} else if (genomeVersion.equals("null") || specie.equals("null")) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome version and/or specie was missing.");

		} else if(genomeVersion.length() > MaxSize.GENOME_VERSION || genomeVersion.length() < 1) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The genome version must be between 1 and "+database.constants.MaxSize.GENOME_VERSION+" characters long.");

		} else if(specie.length() > MaxSize.GENOME_SPECIES || specie.length() < 1) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "The species must be between 1 and "+database.constants.MaxSize.GENOME_SPECIES+" characters long.");

		} else if(!hasOnlyValidCharacters(genomeVersion)) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in genome version. Valid characters are: a-z, A-Z, 0-9");

		} else if(!hasOnlyValidCharacters(specie)) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in specie name. Valid characters are: a-z, A-Z, 0-9");

		}

		return true;

	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<Genome> genomeReleases=db.getAllGenomReleasesForSpecies(specie);
			if(genomeReleases != null) {
				for(Genome g : genomeReleases) {
					if (g != null && g.genomeVersion.equals(this.genomeVersion) && g.species.equals(this.specie)) {
						boolean result = db.removeGenomeRelease(genomeVersion);
						if(result) {
							return new DeleteGenomeReleaseResponse(StatusCode.OK);
						} else {
							return new ErrorResponse(StatusCode.BAD_REQUEST, "Removeing did not work.");
						}
					}
				}
			}
			return new ErrorResponse(StatusCode.BAD_REQUEST, "Version " +  genomeVersion + " or specie " + specie + " does not exist.");

		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if(db.isConnected()) {
				db.close();
			}
		}
	}

}
