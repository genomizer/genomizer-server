package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.MaxSize;

import response.AddGenomeReleaseResponse;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

//TODO: Better error response messages.

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

	@Expose
	private int fileNumber = 0;

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		//TODO: Add validation on the integer.

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

		if(genomeVersion.indexOf('/') != -1) {
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
			//TODO: Call proper database method.
			//String filePath = db.addGenomeRelease(genomeVersion, specie, fileName);
			//ArrayList<String> filePaths = db.addGenomeRelease(genomeVersion, species, filename);
			//rsp = new AddGenomeReleaseResponse(StatusCode.CREATED, filePath);
			rsp = new MinimalResponse(StatusCode.NO_CONTENT);

		} catch (SQLException e) {

			if(e.getErrorCode() == 0) {

				rsp = new ErrorResponse(StatusCode.BAD_REQUEST, "Duplicate values.");

			} else {

				rsp = new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, "Database error");

			}

		} catch (IOException e) {

			rsp = new ErrorResponse(StatusCode.BAD_REQUEST, "IOEXCEPTION");

		} finally {

			if(db.isConnected()) {
				db.close();
			}

		}

		return rsp;

	}

}
