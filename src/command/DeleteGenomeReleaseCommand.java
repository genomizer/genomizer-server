package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;
import response.DeleteGenomeReleaseResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

//TODO: Add more validation code.

/**
 * Class used to delete a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class DeleteGenomeReleaseCommand extends Command {

	private String genomeVersion = null;

	private String specie = null;

	/**
	 * Constructor used to initiate the class.
	 *
	 * @param restful string to split.
	 */
	public DeleteGenomeReleaseCommand(String specie, String genomeVersion) {

		this.genomeVersion = genomeVersion;
		this.specie = specie;

	}

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		if( (genomeVersion.equals("null")) || (specie.equals("null")) ) {
			return false;
		}

		return true;

	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		Response rsp = null;
		DatabaseAccessor db = null;

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
