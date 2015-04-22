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
		if(experimentID == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify " +
					"an experiment name.");
		}
		if(fileName == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a " +
					"file name.");
		}
		if(type == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a " +
					"type.");
		}
		if(metaData == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify metadata.");
		}
		if(uploader == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify an uploader name.");
		}
		if(grVersion == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify a genome release.");
		}
		if(author == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST,
					"Specify author.");
		}
		if(experimentID.length() > MaxSize.EXPID || experimentID.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Experiment " +
					"name: " + experimentID + " has to be between 1 and "
					+ database.constants.MaxSize.EXPID + " characters long.");
		}
		if(fileName.length() > MaxSize.FILE_FILENAME || fileName.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File name: " +
					fileName + " has to be between 1 and "
					+ database.constants.MaxSize.FILE_FILENAME + " characters" +
					" long.");
		}
		if(type.length() > MaxSize.FILE_FILETYPE  || type.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File type: " +
					type + " has to be between 1 and "
					+ database.constants.MaxSize.FILE_FILETYPE + " characters" +
					" long.");
		}
		if(uploader == null){
			throw new ValidateException(StatusCode.BAD_REQUEST,"Specify " +
					"metadata");
		}
		if(uploader.length() > MaxSize.FILE_UPLOADER || uploader.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Uploader " +
					"name: " + uploader + " has to be between 1 and "
					+ database.constants.MaxSize.FILE_UPLOADER + " characters" +
					" long.");
		}
		if(!hasOnlyValidCharacters(uploader)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in uploader name. Valid characters are: " +
					validCharacters);
		}
		if(grVersion.length() > MaxSize.FILE_GRVERSION ||
				grVersion.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Genome " +
					"version: " + grVersion + " has to be between 1 and "
					+ database.constants.MaxSize.FILE_GRVERSION +
					" characters long.");
		}
		if(metaData == null){
			throw new ValidateException(StatusCode.BAD_REQUEST,"Specify " +
					"metadata");
		}
		if(metaData.length() > MaxSize.FILE_METADATA ||
				metaData.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Metadata has" +
					" to be between 1 and "
					+ database.constants.MaxSize.FILE_GRVERSION +
					" characters long.");
		}
		if(metaData.contains("/")) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Metadata can" +
					" not contain slash.");
		}
		if(author == null){
			throw new ValidateException(StatusCode.BAD_REQUEST,"Specify " +
					"author");
		}
		if(author.length() > MaxSize.FILE_AUTHOR || author.length() < 1) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Author has" +
					" to be between 1 and "
					+ database.constants.MaxSize.FILE_AUTHOR + " characters" +
					" long.");
		}
		if(!hasOnlyValidCharacters(author)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid" +
					" characters in author name. Valid characters are: " +
					validCharacters);
		}
		if(!hasOnlyValidCharacters(experimentID)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid" +
					" characters in experiment name. Valid characters are: " +
					validCharacters);
		}
		if(!hasOnlyValidCharacters(type)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in file type name. Valid characters are: " +
					validCharacters);
		}

		if(!hasOnlyValidCharacters(grVersion)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in genome version name. Valid characters are: "
					+ validCharacters);
		}

		if(fileName.contains("/")) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File name " +
					"can not contain slash.");
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
