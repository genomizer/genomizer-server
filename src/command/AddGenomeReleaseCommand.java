package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxSize;

import response.AddGenomeReleaseResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to handle adding a genome release.
 *
 * @author tfy09jnn
 * @version 1.0
 */
public class AddGenomeReleaseCommand extends Command {

	@Expose
	private String genomeVersion = null;

	@Expose
	private String specie = null;

	@Expose
	private ArrayList<String> files = new ArrayList<String>();

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		if(files == null || specie == null || genomeVersion == null) {
			return false;
		}
		if(files.size() == 0) {
			return false;
		}
		for(int i = 0; i < files.size(); i++) {
			int sizeCheck = files.get(i).length();
			if(sizeCheck > MaxSize.GENOME_FILEPATH || sizeCheck < 1) {
				return false;
			}
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

		DatabaseAccessor db = null;
		ArrayList<String> uploadURLs = new ArrayList<String>();

		try {
			db = initDB();
			for(String fileName: files) {
				 uploadURLs.add(db.addGenomeRelease(genomeVersion, specie, fileName));
			}
			return new AddGenomeReleaseResponse(StatusCode.CREATED, uploadURLs);
		} catch (SQLException | IOException e) {
				return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			db.close();
		}
	}

}
