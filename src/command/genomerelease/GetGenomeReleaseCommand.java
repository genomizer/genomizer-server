package command.genomerelease;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.Genome;
import database.subClasses.UserMethods;
import response.ErrorResponse;
import response.GetGenomeReleaseResponse;
import response.HttpStatusCode;
import response.Response;
import server.Debug;

/**
 * A command which is used to get all the genome versions
 * currently stored in the database. It takes no account of species.
 *
 * @author Kommunikation/kontroll 2014.
 * @version 1.0
 */

public class GetGenomeReleaseCommand extends Command {
	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	/**
	 * Set the UserType. Uri and Uuid not used in this command.
	 * @param uri the URI from the http request.
	 * @param uuid the uuid from the http request.
	 * @param userType the userType
	 */
	@Override
	public void setFields(String uri, String uuid, UserMethods.UserType userType) {
		this.userType = userType;
		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
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
						(ArrayList<Genome>)db.getAllGenomeReleases();
				return new GetGenomeReleaseResponse(HttpStatusCode.OK,
						genomeReleases);
			}catch(SQLException e){
				Debug.log("Error when fetching all genome releases. Temporary error with database: "
						+ e.getMessage());
				return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
						"Could not fetch all genome releases due to temporary database error.");
			}
		} catch (SQLException | IOException e) {
			Debug.log("Error when fetching all genome releases. Temporary error with database: "
					+ e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Could not fetch all genome releases due to temporary database error.");
		} finally {
			if (db != null)
				db.close();
		}
	}
}
