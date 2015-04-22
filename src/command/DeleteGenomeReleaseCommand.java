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
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteGenomeReleaseCommand extends Command {
	private String genomeVersion = null;
	private String species = null;

	/**
	 * Constructs a new instance of DeleteExperimentCommand using the supplied
	 * restful string.
	 * @param restful string to set.
	 */

	/**
	 * Constructs a new instance of DeleteGenomeReleaseCommand using the
	 * supplied species name and genome version.
	 *
	 * @param species the name of the species.
	 * @param genomeVersion the genome version.
	 */
	public DeleteGenomeReleaseCommand(String species, String genomeVersion) {
		this.genomeVersion = genomeVersion;
		this.species = species;
	}

	@Override
	public void validate() throws ValidateException {
		validateString(genomeVersion, MaxSize.GENOME_VERSION, "Genome version");
		validateString(species, MaxSize.GENOME_SPECIES, "Genome specie");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<Genome> genomeReleases =
					db.getAllGenomeReleasesForSpecies(species);
			if(genomeReleases != null) {
				for(Genome g : genomeReleases) {
					if (g != null && g.genomeVersion.equals(this.genomeVersion)
							&& g.species.equals(this.species)) {
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
					genomeVersion + " or specie " + species +
					" does not exist.");
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if(db != null && db.isConnected()) {
				db.close();
			}
		}
	}
}
