package command.genomerelease;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Command used to add a genome release.
 *
 * @author Busines Logic 2015
 * @version 1.1
 */
public class PostGenomeReleaseCommand extends Command {
	@Expose
	private String genomeVersion = null;
	@Expose
	private String specie = null;
	@Expose
	private ArrayList<String> files = null;
	@Expose
	private ArrayList<String> checkSumsMD5 = null;

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
		Response response;
		try (DatabaseAccessor db = initDB()) {
			ArrayList<String> uploadURLs = new ArrayList<>();

			for (int i = 0; i < files.size(); i++) {
				String fileName = files.get(i);
				String checkSumMD5 = null;
				if (i < checkSumsMD5.size())
					checkSumMD5 = checkSumsMD5.get(i);
				uploadURLs.add(db.addInProgressGenomeRelease(genomeVersion,
						specie, fileName, checkSumMD5));
			}

			response = new FilePathListResponse(uploadURLs);
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Adding genome release '" +
					genomeVersion + "' for species '" + specie + "'");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Adding genome release '" + genomeVersion +
							"' for species '" + specie + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}

}
