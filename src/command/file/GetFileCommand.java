package command.file;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;

/**
 * retrieves a file linked to an experiment.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetFileCommand extends Command {
	//TODO Implement this class

	private String fileID;

	@Override
	public void setFields(String uri, String uuid, UserType userType) {

		super.setFields(uri, uuid, userType);
		fileID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(fileID, MaxLength.FILE_EXPID, "Filename");
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
		return 	new MinimalResponse(HttpStatusCode.METHOD_NOT_YET_IMPLEMENTED);
	}
}
