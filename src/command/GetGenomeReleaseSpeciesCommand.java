package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


import database.constants.MaxLength;
import response.ErrorResponse;
import response.GetGenomeReleaseResponse;
import response.Response;
import response.StatusCode;
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
	public void setFields(String uri, String uuid) {
		species = uri.split("/")[1];
	}

	@Override
	public void validate() throws ValidateException {
		validateString(species, MaxLength.GENOME_SPECIES, "Genome species");
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
			return new GetGenomeReleaseResponse(StatusCode.OK, genomeReleases);
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					"DatabaseAccessor could not be created: " + e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, species +
					" has no genome version released");
		} finally {
			if (db != null) {
				db.close();
			}
		}

	}

}
