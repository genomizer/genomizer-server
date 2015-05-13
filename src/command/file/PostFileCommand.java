package command.file;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;

import database.subClasses.UserMethods.UserType;
import response.AddFileToExperimentResponse;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.Response;

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

	@Expose
	private String uploader;

	@Expose
	private String grVersion = null;

	@Expose
	private String checkSumMD5 = null;


	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(experimentID, MaxLength.EXPID, "Experiment name");
		validateName(type, MaxLength.FILE_FILETYPE, "File type");
		validateName(author, MaxLength.FILE_AUTHOR, "Author");
		validateName(uploader, MaxLength.FILE_UPLOADER, "Uploader");
		validateName(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateName(fileName, MaxLength.FILE_FILENAME, "Filename");
		validateExists(metaData, MaxLength.FILE_METADATA, "Metadata");
		validateMD5(this.checkSumMD5);
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
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
			return new AddFileToExperimentResponse(HttpStatusCode.OK,
					ft.getUploadURL());
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
