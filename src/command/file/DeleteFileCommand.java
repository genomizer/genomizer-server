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
					return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
							"The file " + fileID + " does not exist and can " +
									"not be deleted");
				}
			} catch (NumberFormatException e) {
				if (db.deleteFile(fileID) > 0) {
					return new MinimalResponse(HttpStatusCode.OK);
				} else {
					return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
							"The file " + fileID + " does not exist and can " +
									"not be deleted");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ErrorResponse(HttpStatusCode.SERVICE_UNAVAILABLE,
					e.getMessage());
		} catch (IOException e) {
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}
}
