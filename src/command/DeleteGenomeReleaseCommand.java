package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import database.DatabaseAccessor;
import response.DeleteGenomeReleaseResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to delete a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommand extends Command {

	@SerializedName("genomeversion")
	@Expose
	private String genomeVersion = null;

	@Expose
	private String specie = null;

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

		//Add implementation code.
		try {

			db = initDB();
			boolean result = db.removeGenomeRelease(genomeVersion, specie);

			if(result) {

				rsp = new DeleteGenomeReleaseResponse(StatusCode.OK);

			} else {

				rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

			}

		} catch (SQLException e) {

			rsp = new MinimalResponse(StatusCode.BAD_REQUEST);

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

	}

}
