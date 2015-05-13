package command.file;

import java.io.IOException;
import java.sql.SQLException;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;
import server.Debug;

/**
 * Class used to represent a command that is used to
 * delete a file from an experiment.
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

	/**
	 * Overrides the original command in order to use the uri.
	 * @param uri Contains the experiment id to fetch.
	 * @param uuid the UUID for the user who made the request.
	 * @param userType the user type for the command caller.
	 */
	@Override
	public void setFields(String uri, String uuid, UserType userType) {
		super.setFields(uri, uuid, userType);
		fileID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(fileID, MaxLength.EXPID, "Experiment name");
	}

	@Override
	public Response execute() {
		DatabaseAccessor db = null;
		try {
			db = initDB();
			try {
				if(db.deleteFile(Integer.parseInt(fileID))==1) {
					return new MinimalResponse(HttpStatusCode.OK);
				} else {
					return new ErrorResponse(HttpStatusCode.NOT_FOUND,
							"The file " + fileID + " does not exist and can " +
									"not be deleted");
				}
			} catch (NumberFormatException e) {
				if (db.deleteFile(fileID) > 0) {
					return new MinimalResponse(HttpStatusCode.OK);
				} else {
					return new ErrorResponse(HttpStatusCode.NOT_FOUND,
							"The file " + fileID + " does not exist and can " +
									"not be deleted");
				}
			}
		} catch (SQLException | IOException e) {
			Debug.log("Deletion of file " + fileID + " didn't work, reason: " +
					e.getMessage());
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, "Deletion of file " + fileID +
					" didn't work because of temporary problems with database.");
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
