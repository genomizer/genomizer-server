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
	private String size;

	@Expose
 	private String type;

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

	/**
	 * Adds all attributes an arraylist and
	 * pass that and the experimentID to the database.
	 * A filepath is returned and sent to the client as
	 * a URL.
	 */
	@Override
	public Response execute() {

		ArrayList<String> fileInfo = new ArrayList<String>();
		fileInfo.add(fileName);
		fileInfo.add(size);
		fileInfo.add(type);

		DatabaseAccessor accessor = null;
		String response_url = null;
		try {
			accessor = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
			response_url = accessor.addFile(type, fileName, null, "Jonas Markström", "Jonas Markström", false, experimentID, "1.0");
			return new AddFileToExperimentResponse(StatusCode.OK, response_url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ErrorResponse(StatusCode.NO_CONTENT);


	}


}
