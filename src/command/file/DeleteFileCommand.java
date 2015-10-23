package command.file;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to delete a file.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class DeleteFileCommand extends Command {
	private String fileID;

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public void setFields(String uri, HashMap<String, String> query, 
                          String uuid, UserType userType) {

		super.setFields(uri, query, uuid, userType);
		fileID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(fileID, MaxLength.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			FileTuple ft = db.getFileTuple(Integer.parseInt(fileID));
			if (isNumeric(fileID)) {
				if (db.deleteFile(Integer.parseInt(fileID)) == 1){
					response = new MinimalResponse(HttpStatusCode.OK);
				} else {
					response = new ErrorResponse(HttpStatusCode.NOT_FOUND,
							"Deletion of file unsuccessful, file id '" +
									fileID + "' does not exist");
				}
			} else {
				if (db.deleteFile(fileID) == 1){
					response = new MinimalResponse(HttpStatusCode.OK);
				} else {
					response = new ErrorResponse(HttpStatusCode.NOT_FOUND,
							"Deletion of file unsuccessful, file path '" +
									fileID + "' does not exist");
				}
			}
			if(response.getClass().equals(MinimalResponse.class)){
				File file = new File(ft.path);
				file.delete();
			}
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Deletion of file '" + fileID +
					"'");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Deletion of file '" + fileID + "' unsuccessful. " +
							e.getMessage());
		}

		return response;
	}

	private boolean isNumeric(String numeral) {
		try {
			Integer.parseInt(numeral);
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}
}
