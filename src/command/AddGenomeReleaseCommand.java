package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.MaxSize;

import response.AddGenomeReleaseResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

//TODO: Add validation code on lengths etc. Better error response messages.

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {

	@Expose
	private String fileName = null;

	@Expose
	private String specie = null;

	@Expose
	private String genomeVersion = null;

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		if(fileName == null || specie == null || genomeVersion == null) {
			return false;
		}

		if(fileName.length() > MaxSize.GENOME_FILEPATH || fileName.length() < 1) {
			return false;
		}

		if(specie.length() > MaxSize.GENOME_SPECIES || specie.length() < 1) {
			return false;
		}

		if(genomeVersion.length() > MaxSize.GENOME_VERSION || genomeVersion.length() < 1) {
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
			String filePath = db.addGenomeRelease(genomeVersion, specie, fileName);
			rsp = new AddGenomeReleaseResponse(StatusCode.CREATED, filePath);

		} catch (SQLException e) {

			if(e.getErrorCode() == 0) {

				rsp = new ErrorResponse(StatusCode.BAD_REQUEST, "Duplicate values.");

			} else {

				rsp = new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database error");

			}

		} catch (IOException e) {

			rsp = new ErrorResponse(StatusCode.BAD_REQUEST, "IOEXCEPTION");

		} finally {

			try {

				db.close();

			} catch (SQLException e) {

				rsp = new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Error cloeseing the database.");

			}

		}

		return rsp;

	}

}
