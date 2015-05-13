package command.genomerelease;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;

import database.subClasses.UserMethods;
import response.AddGenomeReleaseResponse;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import server.Debug;

/**
 * Class used to handle adding a genome release.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostGenomeReleaseCommand extends Command {
	@Expose
	private String genomeVersion = null;

	// TODO: rename to "species".
	@Expose
	private String specie = null;

	@Expose
	private ArrayList<String> files = new ArrayList<>();

	@Expose
	private ArrayList<String> checkSumsMD5 = new ArrayList<>();

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}


	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));
		validateName(specie, MaxLength.GENOME_SPECIES, "Species");
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");

		for(String fileName : files) {
			validateName(fileName, MaxLength.GENOME_FILEPATH, "File name");
		}

		for (String checkSumMD5 : checkSumsMD5) {
			validateMD5(checkSumMD5);
		}
	}


	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		ArrayList<String> uploadURLs = new ArrayList<>();
		try {
			db = initDB();
			for(int i = 0; i < files.size(); ++i) {
				String fileName = files.get(i);
				String checkSumMD5 = null;
				if (i < checkSumsMD5.size()) {
					checkSumMD5 = checkSumsMD5.get(i);
				}
				 uploadURLs.add(db.addGenomeRelease(genomeVersion, specie,
						 fileName, checkSumMD5));
			}
			return new AddGenomeReleaseResponse(HttpStatusCode.OK, uploadURLs);
		} catch (SQLException | IOException e) {
			Debug.log("Error when adding genome release "+genomeVersion+". Temporary error with database: "
					+ e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
					"Error when adding genome release "+genomeVersion+" due to temporary database error.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

}
