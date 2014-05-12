package command;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.*;

import database.DatabaseAccessor;
import database.FileTuple;

import response.AddFileToExperimentResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.DatabaseSettings;

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

		if(experimentID == null || fileName == null || type == null) {
			return false;
		}
		return true;
	}


	public void setUploader(String uploader) {
		this.uploader = uploader;
	}
	/**
	 * Adds all attributes an arraylist and
	 * pass that and the experimentID to the database.
	 * A filepath is returned and sent to the client as
	 * a URL.
	 */
	@Override
	public Response execute() {

		DatabaseAccessor accessor = null;
		String response_url = null;
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
			accessor = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			//response_url = accessor.addFile(type, fileName, "metadata", "Jonas Markström", "Jonas Markström", false, experimentID, "v.123");
			FileTuple ft = accessor.addNewFile(experimentID, filetype, fileName, "", metaData, author, uploader, false, grVersion);
			return new AddFileToExperimentResponse(StatusCode.OK, ft.getUploadURL());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MinimalResponse(StatusCode.BAD_REQUEST);


	}


}
