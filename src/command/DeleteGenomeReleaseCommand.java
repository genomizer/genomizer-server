package command;

import java.io.IOException;
import java.sql.SQLException;

import database.DatabaseAccessor;
import database.MaxSize;
import response.DeleteGenomeReleaseResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

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

		if(genomeVersion == null || specie == null) {
			return false;
		}

		if(genomeVersion.equals("null") || specie.equals("null")) {
			return false;
		}

		if(genomeVersion.length() > MaxSize.GENOME_VERSION || genomeVersion.length() < 1) {
			return false;
		}

		if(specie.length() > MaxSize.GENOME_SPECIES || specie.length() < 1) {
			return false;
		}

		return true;

	}

	/**
	 * method used to execute the actual command.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;

		try {
			db = initDB();
			boolean result = db.removeGenomeRelease(genomeVersion);
			if(result) {
				return new DeleteGenomeReleaseResponse(StatusCode.OK);
			} else {
				return new ErrorResponse(StatusCode.BAD_REQUEST, "Removeing did not work.");
			}
		} catch (SQLException | IOException e) {
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if(db.isConnected()) {
				db.close();
			}
		}

	}

}
