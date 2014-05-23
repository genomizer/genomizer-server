package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.MaxSize;

import response.AddGenomeReleaseResponse;
import response.ErrorResponse;
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

	@Expose
	private String version = null;

	@Expose
	private String species = null;

	@Expose
	private ArrayList<String> files = new ArrayList<String>();

	/**
	 * Method used to validate the command.
	 */
	@Override
	public boolean validate() {

		if(files == null || species == null || version == null) {
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
		if(species.length() > MaxSize.GENOME_SPECIES || species.length() < 1) {
			return false;
		}

		if(version.length() > MaxSize.GENOME_VERSION || version.length() < 1) {
			return false;
		}

		if(version.indexOf('/') != -1) {
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
		ArrayList<String> uploadURLs = new ArrayList<String>();

		try {
			db = initDB();
			for(String fileName: files) {
				 uploadURLs.add(db.addGenomeRelease(version, species, fileName));
			}
			return new AddGenomeReleaseResponse(StatusCode.CREATED, uploadURLs);
		} catch (SQLException | IOException e) {
				rsp = new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			db.close();
		}

		return rsp;

	}

}
