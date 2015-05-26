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

public class PutConvertFileCommand extends Command {
    @Expose
    private String fileid = null;
    @Expose
    private String toformat = null;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

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
