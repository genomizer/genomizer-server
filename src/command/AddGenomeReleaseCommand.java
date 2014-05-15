package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {

	//TODO: Check API on names on JSON stuff. Also check in factory.
	@Expose
	private String genomeVersion = null;

	@Expose
	private String species = null;

	@Expose
	private String fileName = null;

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		return false;

	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		Response rsp = null;
		DatabaseAccessor db = null;

		return rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);
		/*

		try {

			db = initDB();
			String savePath = db.addGenomeRelease(genomeVersion, species, fileName);

			//Do something with string.


		} catch (SQLException e) {
			//Takes care of the duplicate key.
			if(e.getErrorCode() == 0) {

				rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

			} else {

				rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

			}

		} catch (IOException e) {

			rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

		} finally {

			try {

				db.close();

			} catch (SQLException e) {

				rsp = new MinimalResponse(StatusCode.SERVICE_UNAVAILABLE);

			}

		}

		return rsp;
	*/
	}

}
