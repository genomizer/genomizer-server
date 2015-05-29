package command.file;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.*;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Class used to handle updating files in experiments.
 *
 * @author Business Logic 2015.
 * @version 1.1
 */
public class PutFileCommand extends Command {
	@Expose
	private String fileName = null;

	@Expose
	private String type = null;

	@Expose
	private String metaData = null;

	@Expose
	private String author = null;

	@Expose
	private String grVersion = null;

	private String fileID;

	@Override
	public void setFields(String uri, HashMap<String, String> query,
						  String username, UserMethods.UserType userType) {
		super.setFields(uri, query, username, userType);
		fileID = uri.split("/")[2];
	}

	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(type, MaxLength.FILE_FILETYPE, "File type");
		validateName(author, MaxLength.FILE_AUTHOR, "Author");
		validateName(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateName(fileName, MaxLength.FILE_FILENAME, "Filename");
		validateExists(metaData, MaxLength.FILE_METADATA, "Metadata");
		if(!isInteger(fileID) | !isInteger(type))
			throw new ValidateException(HttpStatusCode.BAD_REQUEST,"FileID is not an int");

	}

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public Response execute() {
		try (DatabaseAccessor db = initDB()){
			db.changeFileName(Integer.parseInt(fileID),fileName);
			db.changeFileType(Integer.parseInt(fileID),Integer.parseInt(type));
			db.changeFileMetaData(Integer.parseInt(fileID),metaData);
			db.changeFileAuthor(Integer.parseInt(fileID),author);
			db.changeFileGrVersion(Integer.parseInt(fileID),grVersion);
		} catch (SQLException e) {
			Debug.log("PutfileCommand SQLException :  " + e.getMessage());
			return new DatabaseErrorResponse("Could not alter file");
		} catch (IOException e) {
			Debug.log("PutfileCommand IOException :  " + e.getMessage());
			return new ErrorResponse(HttpStatusCode.BAD_REQUEST,
					"Could not alter file");
		}
		return 	new MinimalResponse(HttpStatusCode.NOT_IMPLEMENTED);
	}

	public boolean isInteger(String s){
		for(Character c : s.toCharArray()){
			if(!Character.isDigit(c))
				return false;
		}
		return true;
	}
}
