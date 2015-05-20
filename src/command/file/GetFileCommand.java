package command.file;

import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.subClasses.UserMethods.UserType;
import response.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * retrieves a file linked to an experiment.
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
	public void setFields(String uri, String query, String username, UserType userType) {

		super.setFields(uri, query, username, userType);
		fileID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(fileID, MaxLength.FILE_EXPID, "Filename");
	}

	@Override
	public Response execute() {

		DatabaseAccessor db;
		FileTuple fileTuple;

		try {
			db = initDB();
			fileTuple = db.getFileTuple(fileID);
		} catch (SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		if(fileTuple == null){
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,"Could not find file");
		}

		return new SingleFileResponse(fileTuple);
	}
}
