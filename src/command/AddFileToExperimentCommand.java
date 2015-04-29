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

	//TODO: Find out what this does.
	private boolean isPrivate = false;

	@Override
	public void validate() throws ValidateException {
		validateName(experimentID, MaxLength.EXPID, "Experiment name");
		validateExists(type, MaxLength.FILE_FILETYPE, "File type");
		validateName(author, MaxLength.FILE_AUTHOR, "Author");
		validateName(uploader, MaxLength.FILE_UPLOADER, "Uploader");
		validateName(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateName(fileName, MaxLength.FILE_FILENAME, "Filename");
		if(metaData != null){
			validateExists(metaData, MaxLength.FILE_METADATA, "Metadata");
		}
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
					metaData, author, uploader, isPrivate, grVersion);
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
