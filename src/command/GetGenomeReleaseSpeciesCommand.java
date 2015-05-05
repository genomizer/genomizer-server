package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.GetGenomeReleaseResponse;
import response.HttpStatusCode;
import response.Response;
import database.DatabaseAccessor;
import database.containers.Genome;
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
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;
		species = uri.split("/")[1];
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
			return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE,
					"DatabaseAccessor could not be created: " + e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE, species +
					" has no genome version released");
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}

}
