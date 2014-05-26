package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


import response.ErrorResponse;
import response.GetGenomeReleaseRespons;
import response.Response;
import response.StatusCode;
import database.DatabaseAccessor;
import database.containers.Genome;
/**
 * A command which is used to get all the genome versions
 * for a specific specie.
 *
 */
public class GetGenomeReleaseSpeciesCommand extends Command{

	private String species;

	/**
	 * Creates a GetGenomeReleaseSpeciesCommand and saves the
	 * specie the genome versions should be retrieved for.
	 *
	 * @param restful - the restful header with specie information
	 */
	public GetGenomeReleaseSpeciesCommand(String restful) {
		species=restful;
	}

	/**
	 * Always true validation
	 */
	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Connects to the database, retrieves all the genomeReleases for a
	 * specific species from
	 * the db and creates a response depending on the return value from the database.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db=null;

		try {
			db = initDB();
			ArrayList<Genome> genomeReleases=db.getAllGenomReleasesForSpecies(species);
			return new GetGenomeReleaseRespons(StatusCode.OK, genomeReleases);
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "DatabaseAccessor could not be created: " + e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, species + " has no genome version released");
		}finally{
			db.close();
		}

	}

}
