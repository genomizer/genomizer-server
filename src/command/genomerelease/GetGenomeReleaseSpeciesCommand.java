package command.genomerelease;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to get all of the genome releases for a specific species.
 */
public class GetGenomeReleaseSpeciesCommand extends Command {
	private String species;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String uuid, UserType userType) {
		super.setFields(uri, query, uuid, userType);
		species = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(species, MaxLength.GENOME_SPECIES, "Genome species");
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			response = new GenomeReleaseListResponse(db.
					getAllGenomeReleasesForSpecies(species));
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Retrieval of genome " +
					"releases for species '" + species + "'");
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Retrieval of genome releases for species '" + species +
							"' unsuccessful. " + e.getMessage());
		}

		return response;
	}
}
