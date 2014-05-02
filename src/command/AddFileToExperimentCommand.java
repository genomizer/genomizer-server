package command;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import response.ErrorResponse;
import response.Response;

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

		Response rsp;
		ArrayList<String> fileInfo = new ArrayList<String>();
		fileInfo.add(fileName);
		fileInfo.add(size);
		fileInfo.add(type);
//
//		 Method from database group, needs more info
//		 String filepath = uploadFile(experimentID, fileInfo);
//		 if(filepath != null) {
//			 rsp = new AddFileToExperimentResponse(200, filepath);
//		 } else {
//			 rsp = new ErrorResponse(404);
//		 }
//		 return rsp;
//		return null;

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(405);
	}


}
