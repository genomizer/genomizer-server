package command.genomerelease;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import response.DeleteGenomeReleaseResponse;
import response.ErrorResponse;
import response.Response;
import response.HttpStatusCode;

/**
 * Class used to delete a genome release.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteGenomeReleaseCommand extends Command {
	private String genomeVersion;
	private String species;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);
		String[] splitFields = uri.split("/");
		species = splitFields[2];
		genomeVersion = splitFields[3];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");
		validateName(species, MaxLength.GENOME_SPECIES, "Genome species");
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
									DeleteGenomeReleaseResponse(HttpStatusCode.OK);
						} else {
							return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
									"Could not delete genome release");
						}
					}
				}
			}
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Version " +
					genomeVersion + " or species " + species +
					" does not exist.");
		} catch (SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if(db != null && db.isConnected()) {
				db.close();
			}
		}
	}
}
