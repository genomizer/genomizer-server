package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxLength;

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
	public void setFields(String uri, String uuid) {

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {

		validateString(specie, MaxLength.GENOME_SPECIES, "Specie");
		validateString(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");

		for(int i = 0; i < files.size(); i++) {
			int sizeCheck = files.get(i).length();
			if(sizeCheck > MaxLength.GENOME_FILEPATH || sizeCheck < 1) {
				throw new ValidateException(StatusCode.BAD_REQUEST, "File " +
						"name has to be between 1 and " +
						MaxLength.GENOME_FILEPATH +
						" characters long.");
			}
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
