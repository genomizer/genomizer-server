package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


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
	/**
	 * Creates a GetGenomeReleaseSpeciesCommand and saves the
	 * species the genome versions should be retrieved for.
	 *
	 * @param restful - the restful header with species information
	 */

	/**
	 * Constructs a new instance of GetGenomeReleaseSpeciesCommand using the
	 * supplied restful string.
	 * @param restful header to set.
	 */
	public GetGenomeReleaseSpeciesCommand(String restful) {
		species = restful;
	}

	@Override
	public void validate() throws ValidateException {

		if(species == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Species was " +
					"missing.");

		} else if(species.length() < 1 || species.length() >
				database.constants.MaxSize.GENOME_SPECIES) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Species has " +
					"to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES +
					" characters long.");

		} else if(!hasOnlyValidCharacters(species)) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in species name. Valid characters are: " +
					validCharacters);

		}
	}

	/**
	 * Connects to the database, retrieves all the genomeReleases for a
	 * specific species from
	 * the db and creates a response depending on the return value from the
	 * database.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db=null;
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
		}finally{
			if (db != null) {
				db.close();
			}
		}

	}

}
