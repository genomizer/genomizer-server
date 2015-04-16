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
 * for a specific specie.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */
public class GetGenomeReleaseSpeciesCommand extends Command{

	private String specie;

	/**
	 * Creates a GetGenomeReleaseSpeciesCommand and saves the
	 * specie the genome versions should be retrieved for.
	 *
	 * @param restful - the restful header with specie information
	 */
	public GetGenomeReleaseSpeciesCommand(String restful) {

		specie = restful;

	}

	/**
	 * Method used to validate the information needed
	 * to execute the command.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(specie == null) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Specie was missing.");

		} else if(specie.length() < 1 || specie.length() > database.constants.MaxSize.GENOME_SPECIES) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Specie has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES + " characters long.");

		} else if(!hasOnlyValidCharacters(specie)) {

			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in specie name. Valid characters are: " + validCharacters);

		}

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
			ArrayList<Genome> genomeReleases=db.getAllGenomReleasesForSpecies(specie);
			return new GetGenomeReleaseResponse(StatusCode.OK, genomeReleases);
		} catch (SQLException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "DatabaseAccessor could not be created: " + e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, specie + " has no genome version released");
		}finally{
			db.close();
		}

	}

}
