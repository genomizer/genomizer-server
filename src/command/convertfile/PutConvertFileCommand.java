package command.convertfile;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import conversion.ConversionHandler;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.subClasses.UserMethods;
import response.*;

import java.io.IOException;
import java.sql.SQLException;

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
    public void setFields(String uri, String uuid, UserMethods.UserType userType) {
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
            filetuple = convHandler.convertProfileData(toformat,Integer.getInteger(fileid));
        } catch (SQLException | IOException e) {
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not " +
                    "convert file : " + e.getMessage());

        }

        if(filetuple == null){
            return new MinimalResponse(HttpStatusCode.NOT_FOUND);
        }
        FileInformation fileInformation = new FileInformation(filetuple);

        return new

    }
}
