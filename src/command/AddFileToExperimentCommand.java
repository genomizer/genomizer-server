package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
import database.constants.MaxSize;
import database.containers.FileTuple;

import response.AddFileToExperimentResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command of the type Addfile.
 *
 * @author hugokallstrom
 * @version 1.0
 */
public class AddFileToExperimentCommand extends Command {

	@Expose
	private String experimentID;

	@Expose
	private String fileName;

	@Expose
 	private String type;

	@Expose
	private String metaData;

	@Expose
	private String author;

	@Expose
	private String uploader;

	@Expose
	private boolean isPrivate;

	@Expose
	private String grVersion;

	/**
	 * Validates the request by checking
	 * the attributes. No attribute can be null
	 * and type needs to be either "raw", "profile",
	 * or "region".
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(experimentID == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify an experiment name.");
		}
		if(fileName == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a file name.");
		}
		if(type == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a type.");
		}
		if(uploader == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify an uploader name.");
		}
		if(grVersion == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a genome release.");
		}
		if(experimentID.length() > MaxSize.EXPID || experimentID.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment name has to be between 1 and "
					+ database.constants.MaxSize.EXPID + " characters long.");
		}
		if(fileName.length() > MaxSize.FILE_FILENAME || fileName.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File name has to be between 1 and "
					+ database.constants.MaxSize.FILE_FILENAME + " characters long.");
		}
		if(type.length() > MaxSize.FILE_FILETYPE  || type.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File type has to be between 1 and "
					+ database.constants.MaxSize.FILE_FILETYPE + " characters long.");
		}
		if(uploader.length() > MaxSize.FILE_UPLOADER || uploader.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Uploader name has to be between 1 and "
					+ database.constants.MaxSize.FILE_UPLOADER + " characters long.");
		}
		if(grVersion.length() > MaxSize.FILE_GRVERSION || grVersion.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Genome version has to be between 1 and "
					+ database.constants.MaxSize.FILE_GRVERSION + " characters long.");
		}
		if(!hasOnlyValidCharacters(experimentID)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in experiment name. Valid characters are: a-z, A-Z, 0-9");
		}
		if(!hasOnlyValidCharacters(type)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in file type name. Valid characters are: a-z, A-Z, 0-9");
		}
		if(!hasOnlyValidCharacters(uploader)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in uploader name. Valid characters are: a-z, A-Z, 0-9");
		}
		return true;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	/**
	 * Adds all attributes to an arraylist and
	 * pass that and the experimentID to the database.
	 * A filepath is returned and sent to the client as
	 * a URL.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor db = null;
		int filetype;
		if(type.equalsIgnoreCase("raw")) {
			filetype = FileTuple.RAW;
		} else if(type.equalsIgnoreCase("profile")) {
			filetype = FileTuple.PROFILE;
		} else if(type.equalsIgnoreCase("region")) {
			filetype = FileTuple.REGION;
		} else {
			filetype = FileTuple.OTHER;
		}
		try {
			db = initDB();
			FileTuple ft = db.addNewFile(experimentID, filetype, fileName, "", metaData, author, uploader, isPrivate, grVersion);
			return new AddFileToExperimentResponse(StatusCode.OK, ft.getUploadURL());
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE, e.getMessage());
		} finally{
			db.close();
		}
	}
}
