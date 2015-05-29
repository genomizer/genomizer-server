package command.file;

import authentication.Authenticate;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import response.*;
import server.Debug;

import java.io.File;
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

	@Override
	public Response execute() {
		Response response;
		int fileType;
		switch (type.toLowerCase()) {
			case "raw":
				fileType = FileTuple.RAW;
				break;
			case "profile":
				fileType = FileTuple.PROFILE;
				break;
			case "region":
				fileType = FileTuple.REGION;
				break;
			default:
				fileType = FileTuple.OTHER;
		}

		try (DatabaseAccessor db = initDB()) {
			FileTuple ft = db.addNewInProgressFile(experimentID, fileType,
					fileName, null, metaData, author, uploader, false,
					grVersion, checkSumMD5);
			response = new UrlUploadResponse(ft.getUploadURL());

		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Adding file '" + fileName +
					"'");
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Adding file '" + fileName + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}
}
