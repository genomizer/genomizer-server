package command.genomerelease;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Command used to delete a genome release.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteGenomeReleaseCommand extends Command {
	private String genomeVersion;
	private String species;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 3;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserType userType) {
		super.setFields(uri, query, username, userType);
		String[] splitFields = uri.split("/");
		species = splitFields[2];
		genomeVersion = splitFields[3];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");
		validateName(species, MaxLength.GENOME_SPECIES, "Genome species");
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			if (db.removeGenomeRelease(genomeVersion))
				response = new MinimalResponse(HttpStatusCode.OK);
			else
				response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
						"Deletion of genome release '" + genomeVersion +
								"' unsuccessful, genome release does not " +
								"exist");
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Deletion of genome " +
					"release '" + genomeVersion + "'");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of genome release '" + genomeVersion +
							"' unsuccessful. " + e.getMessage());
		}

		return response;
	}
}
