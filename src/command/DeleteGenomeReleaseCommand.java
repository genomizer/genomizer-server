package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
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
	private String genomeVersion;
	private String species;

	/**
	 * Constructs a new instance of DeleteGenomeReleaseCommand using the
	 * supplied species name and genome version.
	 *
	 * @param species the name of the species.
	 * @param genomeVersion the genome version.
	 */
	public DeleteGenomeReleaseCommand(String species, String genomeVersion, UserType userType) {
		this.genomeVersion = genomeVersion;
		this.species = species;
		this.userType = userType;
	}

	@Override
	public void validate() throws ValidateException {
		validateString(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");
		validateString(species, MaxLength.GENOME_SPECIES, "Genome specie");
		hasRights(UserType.USER);
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
