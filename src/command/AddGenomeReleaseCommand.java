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
	public boolean validate() throws ValidateException {

		if(files == null || files.size() == 0) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify release files.");
		}
		if(genomeVersion == null || genomeVersion.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a genome release version.");
		}
		for(int i = 0; i < files.size(); i++) {
			int sizeCheck = files.get(i).length();
			if(sizeCheck > MaxSize.GENOME_FILEPATH || sizeCheck < 1) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "File name has to be between 1 and "
						+ database.constants.MaxSize.GENOME_FILEPATH + " characters long.");
			}
		}
		if(specie == null || specie.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a specie.");
		}
		if(specie.length() > MaxSize.GENOME_SPECIES) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specie name has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES + " characters long.");
		}

		if(genomeVersion.length() > MaxSize.GENOME_VERSION || genomeVersion.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Genome version has to be between 1 and "
					+ database.constants.MaxSize.GENOME_VERSION + " characters long.");
		}

		if(genomeVersion.indexOf('/') != -1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Wrong format on request.");
		}
		if(!hasOnlyValidCharacters(genomeVersion)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in genome version name. Valid characters are: a-z, A-Z, 0-9");
		}
		if(!hasOnlyValidCharacters(specie)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in specie name. Valid characters are: a-z, A-Z, 0-9");
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
