package command.genomerelease;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.GetGenomeReleaseResponse;
import response.HttpStatusCode;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A command which is used to get all the genome versions
 * for a specific species.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetGenomeReleaseSpeciesCommand extends Command {
	private String species;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	/**
	 * Overrides the original command in order to use the uri.
	 * @param uri Contains the experiment id to fetch.
	 * @param uuid the UUID for the user who made the request.
	 * @param userType the user type for the command caller.
	 */
	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
		species = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(species, MaxLength.GENOME_SPECIES, "Genome species");
	}

	/**
	 * Connects to the database, retrieves all the genomeReleases for a
	 * specific species from
	 * the db and creates a response depending on the return value from the
	 * database.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			ArrayList<Genome> genomeReleases =
					db.getAllGenomeReleasesForSpecies(species);
			return new GetGenomeReleaseResponse(HttpStatusCode.OK, genomeReleases);
		} catch (SQLException e) {
			Debug.log("Error when fetching all genome versions for species "+species+". Temporary error with database: "
					+ e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Error when fetching all genome versions for species "+species+". Temporary error with database.");
		} catch (IOException e) {
			Debug.log("Error when fetching all genome versions for species "+species+". The specie has no released " +
					"genome versions: " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, species +
					" has no genome version released");
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}

}
