package command.convertfile;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import conversion.ConversionHandler;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.subClasses.UserMethods;
import response.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * command that handles fileconversion.
 *
 * @author dv13thg
 * @version 1.0
 */
public class PutConvertFileCommand extends Command {

    @Expose
    private String fileid;

    @Expose
    private String toformat;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String username, UserMethods.UserType userType) {
        this.userType = userType;

		/*No fields from the URI is needed, neither is the UUID. Dummy
		implementation*/
    }

    /**
     * @see command.Command
     * @throws command.ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(fileid, MaxLength.FILE_FILENAME,"file id");
        validateName(toformat, MaxLength.FILE_FILETYPE, "to format");
    }

    /**
     * @see command.Command
     * @return response
     */
    @Override
    public Response execute() {
        ConversionHandler convHandler = new ConversionHandler();
        FileTuple filetuple;

        try {
            filetuple = convHandler.convertProfileData(toformat,Integer.parseInt(fileid));
        } catch (SQLException | IOException | NumberFormatException e) {
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
                    "convert file : " + e.getMessage());

        }
        if(filetuple == null){
            return new MinimalResponse(HttpStatusCode.NOT_FOUND);
        }
        return new SingleFileResponse(filetuple);
    }
}
