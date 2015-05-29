package command.file;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods.UserType;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Command used to retrieve a file.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class GetFileCommand extends Command {
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
		validateName(fileID, MaxLength.FILE_EXPID, "Filename");
	}

	@Override
	public Response execute() {
		Response response;
		try (DatabaseAccessor db = initDB()) {
			return new SingleFileResponse(db.getFileTuple(Integer.
					parseInt(fileID)));
		} catch (NumberFormatException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Retrieval of file with file id '" + fileID +
							"' unsuccessful, the file id may not contain any " +
							"characters except numbers.");
		} catch (SQLException e) {
			response = new DatabaseErrorResponse("Retrieval of file with " +
					"file id '" + fileID + "'");
			Debug.log("Reason: " + e.getMessage());
		} catch (IOException e) {
			response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Retrieval of file with file id '" + fileID +
							"' unsuccessful. " + e.getMessage());
		}

		return response;
	}
}
