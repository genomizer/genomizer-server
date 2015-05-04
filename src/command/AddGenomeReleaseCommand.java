package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxLength;

import database.subClasses.UserMethods.UserType;
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
	public void setFields(String uri, String uuid, UserType userType) {
		this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {

		hasRights(UserRights.getRights(this.getClass()));
		validateName(specie, MaxLength.GENOME_SPECIES, "Specie");
		validateName(genomeVersion, MaxLength.GENOME_VERSION, "Genome version");

		for(int i = 0; i < files.size(); i++) {
			validateName(files.get(i), MaxLength.GENOME_FILEPATH, "File name");
		}
	}


	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		ArrayList<String> uploadURLs = new ArrayList<>();
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
