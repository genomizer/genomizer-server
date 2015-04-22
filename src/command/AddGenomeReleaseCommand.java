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
 * @author Business Logic 2015.
 * @version 1.1
 */
public class AddGenomeReleaseCommand extends Command {
	@Expose
	private String genomeVersion = null;

	@Expose
	private String specie = null;

	@Expose
	private ArrayList<String> files = new ArrayList<>();

	@Override
	public void validate() throws ValidateException {
		if(files == null || files.size() == 0) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify release files.");
		}
		if(genomeVersion == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify a genome release version.");
		}
		for(int i = 0; i < files.size(); i++) {
			int sizeCheck = files.get(i).length();
			if(sizeCheck > MaxSize.GENOME_FILEPATH || sizeCheck < 1) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "File " +
						"name has to be between 1 and " +
						database.constants.MaxSize.GENOME_FILEPATH +
						" characters long.");
			}
		}
		if(specie == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a " +
					"specie.");
		}
		if(specie.length() > MaxSize.GENOME_SPECIES || specie.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specie name " +
					"has to be between 1 and "
					+ database.constants.MaxSize.GENOME_SPECIES +
					" characters long.");
		}

		if(genomeVersion.length() > MaxSize.GENOME_VERSION ||
				genomeVersion.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Genome " +
					"version has to be between 1 and "
					+ database.constants.MaxSize.GENOME_VERSION +
					" characters long.");
		}

		if(genomeVersion.indexOf('/') != -1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Wrong " +
					"format on request.");
		}
		if(!hasOnlyValidCharacters(genomeVersion)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in genome version name. Valid characters are: "
					+ validCharacters);
		}
		if(!hasOnlyValidCharacters(specie)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in specie name. Valid characters are: " +
					validCharacters);
		}
	}


	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		ArrayList<String> uploadURLs = new ArrayList<String>();
		try {
			db = initDB();
			for(String fileName: files) {
				 uploadURLs.add(db.addGenomeRelease(genomeVersion, specie,
						 fileName));
			}
			return new AddGenomeReleaseResponse(StatusCode.CREATED, uploadURLs);
		} catch (SQLException | IOException e) {
				return new ErrorResponse(StatusCode.BAD_REQUEST,
						e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
