package command.genomerelease;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.containers.Genome;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Command used to get all of the genome releases.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetGenomeReleaseCommand extends Command {
	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			response = new GenomeReleaseListResponse((ArrayList<Genome>)
					db.getAllGenomeReleases());
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Retrieval of genome " +
					"releases");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Retrieval of genome releases unsuccessful. " +
							e.getMessage());
		}

		return response;
	}
}
