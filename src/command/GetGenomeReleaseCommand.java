package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import database.DatabaseAccessor;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.GetGenomeReleaseResponse;
import response.Response;
import response.StatusCode;

/**
 * A command which is used to get all the genome versions
 * currently stored in the database. It takes no account of species.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class GetGenomeReleaseCommand extends Command {

	public GetGenomeReleaseCommand(UserType userType){
		this.userType = userType;
	}

	@Override
	public void validate() throws ValidateException {
		/*Validation will always succeed, the command can not be corrupt.*/
		hasRights(UserRights.getRights(this.getClass()));
	}

	/**
	 * Connects to the database, retrieves all the genomeReleases and creates
	 * a response depending on the database return value.
	 * @return an appropriate Response.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db=null;
		try {
			db = initDB();
			try{
				ArrayList<Genome> genomeReleases =
						(ArrayList<Genome>)db.getAllGenomReleases();
				return new GetGenomeReleaseResponse(StatusCode.OK,
						genomeReleases);
			}catch(SQLException e){
				return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
						"Could not fetch all genome releases: " +
								e.getMessage());
			}
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					"SQLException - Could not create connection to database: " +
							e.getMessage());
		} finally {
			if (db != null)
				db.close();
		}
	}
}
