package command;

import java.util.ArrayList;

import response.DownloadResponse;
import response.ErrorResponse;
import response.Response;
import response.StatusCode;

import com.google.gson.annotations.Expose;

/**
 * Class used to represent a command of the type GetFileFromExperimentCommand.
 *
 * @author tfy09jnn, hugokallstrom
 * @version 1.1
 */
public class GetFileFromExperimentCommand extends Command {

	private String fileID;

	/**
	 * Constructor. Takes the fileID as argument.
	 * @param fileID
	 */
	public GetFileFromExperimentCommand(String restful) {
		fileID = restful;

	}

	/**
	 * Used to validate the correctness of the
	 * class when built.
	 */
	@Override
	public boolean validate() {
		return true;
	}

	/**
	 * Check to see if the requested file exists and
	 * get its attributes from the database. Return
	 * the attributes and an URL to the client.
	 */
	@Override
	public Response execute() {

//		Response rsp = rsp;
//		ArrayList<String> attributes = new ArrayList<String>();

//		if(!fileExists(fileID)) {
//			attributes = getFileAttributes(fileID);
//			rsp = new DownloadResponse(200, attributes);
//		} else {
//			// File not found, send appropriate response (404)
//			rsp = new ErrorResponse(404);
//		}

		//Method not implemented, send appropriate response
		return 	new ErrorResponse(StatusCode.NO_CONTENT);
	}

}
