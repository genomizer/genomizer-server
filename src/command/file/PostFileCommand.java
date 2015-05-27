package command.file;

import authentication.Authenticate;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;
import response.UrlUploadResponse;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Adds a file to an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PostFileCommand extends Command {
	@Expose
	private String experimentID = null;

	@Expose
	private String fileName = null;

	@Expose
	private String type = null;

	@Expose
	private String metaData = null;

	@Expose
	private String author = null;

	private String uploader;

	@Expose
	private String grVersion = null;

	@Expose
	private String checkSumMD5 = null;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 1;
	}

	@Override
	public void validate() throws ValidateException {

		uploader = Authenticate.getUsernameByID(uuid);

		hasRights(UserRights.getRights(this.getClass()));
		validateName(experimentID, MaxLength.EXPID, "Experiment name");
		validateName(type, MaxLength.FILE_FILETYPE, "File type");
		validateName(author, MaxLength.FILE_AUTHOR, "Author");
		validateName(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateName(fileName, MaxLength.FILE_FILENAME, "Filename");
		validateExists(metaData, MaxLength.FILE_METADATA, "Metadata");
		validateMD5(this.checkSumMD5);
	}

	/**
	 * Adds all attributes to an ArrayList and passes that and the experimentID
	 * to the database. A file path is returned and sent to the client as an
	 * URL.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		int fileType;
		if(type.equalsIgnoreCase("raw")) {
			fileType = FileTuple.RAW;
		} else if(type.equalsIgnoreCase("profile")) {
			fileType = FileTuple.PROFILE;
		} else if(type.equalsIgnoreCase("region")) {
			fileType = FileTuple.REGION;
		} else {
			fileType = FileTuple.OTHER;
		}
		try {
			db = initDB();
			FileTuple ft = db.addNewInProgressFile(experimentID, fileType, fileName, null,
					metaData, author, uploader, false, grVersion, checkSumMD5);
			return new UrlUploadResponse(ft.getUploadURL());
		} catch (SQLException | IOException e) {
			Debug.log("Adding of file " + fileName + " to experiment "+experimentID+" didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Adding of file " + fileName +
					" to experiment "+experimentID+" didn't work because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
