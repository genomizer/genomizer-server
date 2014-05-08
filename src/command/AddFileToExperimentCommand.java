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
	private String size;

	@Expose
 	private String type;

	@Expose
	private String fileType;

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

		if(experimentID == null || fileName == null || size == null || type == null || type != "raw" || type != "profile" || type != "region") {
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

		/*ArrayList<String> fileInfo = new ArrayList<String>();
		fileInfo.add(fileName);
		fileInfo.add(size);
		fileInfo.add(type);*/

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
			response_url = accessor.addFileURL(type, fileName, metaData, author, uploader, false, experimentID, grVersion);
			return new AddFileToExperimentResponse(StatusCode.OK, response_url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MinimalResponse(StatusCode.BAD_REQUEST);


	}


}
