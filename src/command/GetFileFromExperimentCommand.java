package command;

import database.constants.MaxLength;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 * Class used to represent a command of the type GetFileFromExperimentCommand.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetFileFromExperimentCommand extends Command {
	//TODO Implement this class

	private String fileID;

	@Override
	public void setFields(String uri, String username) {
		fileID = uri.split("/")[1];
	}

	@Override
	public void validate() throws ValidateException {
		validateString(fileID, MaxLength.FILE_EXPID, "Filename");
	}

	@Override
	public Response execute() {
//		Response rsp = rsp;
//		ArrayList<String> attributes = new ArrayList<String>();
//
//		results = db.searchExperiment(fileID);
//
//
//		if(results == null) {
//			 File not found, send appropriate response (404)
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
