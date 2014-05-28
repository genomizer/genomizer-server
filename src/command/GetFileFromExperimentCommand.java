package command;

import java.util.ArrayList;

import response.DownloadResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import database.DatabaseAccessor;
import com.google.gson.annotations.Expose;

/**
 * Class used to represent a command of the type GetFileFromExperimentCommand.
 *
 * @author tfy09jnn, hugokallstrom
 * @version 1.1
 */
public class GetFileFromExperimentCommand extends Command {

	private String fileID;
	private DatabaseAccessor db;


	/**
	 * Constructor. Takes the fileID as argument.
	 * @param fileID
	 */
	public GetFileFromExperimentCommand(String restful) {
		fileID = restful;
	}

	/**
	 * Used to validate the GetFileFromExperimentCommand
	 * class.
	 *
	 * @return boolean depending on result.
	 * @throws ValidateException
	 */
	@Override
	public boolean validate() throws ValidateException {

		if(fileID == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a file-id.");
		}
		if(fileID.length() < 1 || fileID.length() > database.constants.MaxSize.FILE_EXPID) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File-id has to be between 1 and "
					+ database.constants.MaxSize.FILE_EXPID + " characters long.");
		}
		if(!hasOnlyValidCharacters(fileID)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid characters in annotation value. Valid characters are: " + validCharacters);
		}
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
//
//		results = db.searchExperiment(fileID);
//
//
//		if(results == null) {
//			// File not found, send appropriate response (404)
//			rsp = new ErrorResponse(404);
//		} else {
//			int rowNr = results.getRowCount();
//			for (int i = 0; i < rowNr; i++) {
//				attributes = results.getRowValues(i);
//			}
//			System.out.println(attributes.toString());
//			rsp = new DownloadResponse(200, attributes);
//		}

		//Method not implemented, send appropriate response
		return 	new MinimalResponse(StatusCode.NO_CONTENT);
	}

}
