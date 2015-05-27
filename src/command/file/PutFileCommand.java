package command.file;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import response.*;

import java.io.IOException;
import java.sql.SQLException;

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


	@Override
	public void validate() throws ValidateException {
		hasRights(UserRights.getRights(this.getClass()));
		validateName(type, MaxLength.FILE_FILETYPE, "File type");
		validateName(author, MaxLength.FILE_AUTHOR, "Author");
		validateName(grVersion, MaxLength.FILE_GRVERSION, "Genome release");
		validateName(fileName, MaxLength.FILE_FILENAME, "Filename");
		validateExists(metaData, MaxLength.FILE_METADATA, "Metadata");
	}

	@Override
	public int getExpectedNumberOfURIFields() {
		return 2;
	}

	@Override
	public Response execute() {
		try (DatabaseAccessor db = initDB()){
			//TODO ADD db function here...

		} catch (SQLException | IOException e) {
			return new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		return 	new MinimalResponse(HttpStatusCode.NOT_IMPLEMENTED);
	}
}
