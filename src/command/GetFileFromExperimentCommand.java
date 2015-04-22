package command;

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
	private String fileID;

	/**
	 * Constructor. Takes the fileID as argument.
	 * @param fileID the file id.
	 */

	/**
	 * Constructs a new instance of GetFileExperimentCommand using the supplied
	 * file ID.
	 * @param fileID the file ID of the wanted file.
	 */
	public GetFileFromExperimentCommand(String fileID) {
		this.fileID = fileID;
	}

	@Override
	public void validate() throws ValidateException {
		if(fileID == null) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Specify a " +
					"file-id.");
		}
		if(fileID.length() < 1 || fileID.length() >
				database.constants.MaxSize.FILE_EXPID) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "File-id has " +
					"to be between 1 and "
					+ database.constants.MaxSize.FILE_EXPID + " characters " +
					"long.");
		}
		if(!hasOnlyValidCharacters(fileID)) {
			throw new ValidateException(StatusCode.BAD_REQUEST, "Invalid " +
					"characters in annotation value. Valid characters are: " +
					validCharacters);
		}
	}

	@Override
	public Response execute() {
		//TODO Replace this with useful code?
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
