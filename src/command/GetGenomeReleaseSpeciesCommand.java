package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


import response.ErrorResponse;
import response.GetGenomeReleaseRespons;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;
import database.DatabaseAccessor;
import database.Genome;
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
	 *
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db=null;

		try {
			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			if(db.getChoices("Species").contains(species)){
				ArrayList<Genome> genomeReleases=db.getAllGenomReleasesForSpecies(species);
				return new GetGenomeReleaseRespons(StatusCode.OK, genomeReleases);

			}else{
				return new ErrorResponse(StatusCode.BAD_REQUEST, "The species you are asking for has no genome version released");
			}

			//ArrayList<Genome> genomeReleases=db.getAllGenomReleasesForSpecies(species);
			//return new GetGenomeReleaseRespons(StatusCode.OK, genomeReleases);

		} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
