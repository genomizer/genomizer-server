package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;

import response.AddFileToExperimentResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command of the type AddFile.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class AddFileToExperimentCommand extends Command {
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

	@Override
	public void setFields(String uri, String username) {

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
	}

	@Override
	public void validate() throws ValidateException {
		validateString(experimentID, MaxLength.EXPID, "Experiment name");
		validateString(type, MaxLength.FILE_FILETYPE, "File type");
		validateString(author, MaxLength.FILE_AUTHOR, "Author");
		validateString(uploader, MaxLength.FILE_UPLOADER, "Uploader");
		validateString(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateString(fileName, MaxLength.FILE_FILENAME, "Filename");
		validateString(metaData, MaxLength.FILE_METADATA, "Metadata");
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
			FileTuple ft = db.addNewFile(experimentID, fileType, fileName, null,
					metaData, author, uploader, false, grVersion);
			return new AddFileToExperimentResponse(StatusCode.OK,
					ft.getUploadURL());
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
