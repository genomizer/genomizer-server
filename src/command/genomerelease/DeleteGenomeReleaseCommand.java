package command.genomerelease;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {

		super.setFields(uri, query, username, userType);
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
									MinimalResponse(HttpStatusCode.OK);
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
			Debug.log("Error when deleting genome release " + genomeVersion + " for species " + species +
							". Database error: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Error when deleting genome release "
					+ genomeVersion + " for species "+ species + ". Database error.");
		} finally {
			if(db != null) {
				db.close();
			}
		}
	}
}
