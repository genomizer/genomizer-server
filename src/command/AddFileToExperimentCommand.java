package command;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.annotations.Expose;

import database.DatabaseAccessor;
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
	public boolean validate() {

		if(experimentID == null || fileName == null || type == null || uploader==null) {
			return false;
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
