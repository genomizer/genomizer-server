package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DatabaseAccessor;
import database.Genome;
import response.ErrorResponse;
import response.GetGenomeReleaseRespons;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

/**
 * A command which is used to get all the genome versions
 * currently stored in the database. It takes no account of species.
 *
 * @author
 * @version 1.0
 */

public class GetGenomeReleaseCommand extends Command{

	/**
	 * Empty constructor, used to get an object of GetGenomeReleaseCommand
	 */
	public GetGenomeReleaseCommand() {

	}

	/**
	 * Validation is always true, this command is always sent to the database
	 * because the command can't be corrupt.
	 *
	 * @return true.
	 */
	@Override
	public boolean validate() {

		return true;

	}

	/**
	 * Connects to the database, retrieves all the genomeReleases
	 * and creates a response depending on the database return value.
	 *
	 * @return Object of the response class depending on result.
	 */
	@Override
	public Response execute() {
		DatabaseAccessor db=null;
		try {

			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			ArrayList<Genome> genomeReleases=db.getAllGenomReleases();

			return new GetGenomeReleaseRespons(StatusCode.OK, genomeReleases);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new ErrorResponse(StatusCode.BAD_REQUEST, "Something went wrong");
	}

}
