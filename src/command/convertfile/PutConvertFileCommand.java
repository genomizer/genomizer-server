package command.convertfile;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import conversion.ConversionHandler;
import database.constants.MaxLength;
import database.containers.FileTuple;
import response.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Command used to convert a file.
 *
 * @author Business Logic 2015
 * @version 1.1
 */
public class PutConvertFileCommand extends Command {
    @Expose
    private String fileid = null;
    @Expose
    private String toformat = null;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
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

    @Override
    public Response execute() {
        ConversionHandler convHandler = new ConversionHandler();
        Response response;

        try {
            FileTuple filetuple = convHandler.convertProfileData(toformat,
                    Integer.parseInt(fileid));
            if (filetuple != null)
                response = new SingleFileResponse(filetuple);
            else
                response = new MinimalResponse(HttpStatusCode.NOT_FOUND);
        } catch (NumberFormatException e) {
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "File conversion unsuccessful, the file id may not " +
                            "contain any characters except numbers.");
        } catch (SQLException e) {
            response = new ErrorResponse(HttpStatusCode.INTERNAL_SERVER_ERROR,
                    "File conversion unsuccessful due to temporary database " +
                            "problems");
        } catch (IOException e) {
            response = new ErrorResponse(HttpStatusCode.BAD_REQUEST, "File " +
                    "conversion unsuccessful. " + e.getMessage());
        }

        return response;
    }
}
