package command;

import com.google.gson.annotations.Expose;
import conversion.ConversionHandler;
import database.constants.MaxLength;
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
public class ConvertFileCommand extends Command{

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
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        validateName(toformat,MaxLength.FILE_FILETYPE,"to format");
        validateExists(fileid,MaxLength.FILE_FILENAME,"FileId");
    }

    /**
     * @see command.Command
     * @return response
     */
    @Override
    public Response execute() {
        ConversionHandler convHandler = new ConversionHandler();
        String fileUrl;
        try {
            fileUrl = convHandler.convertProfileData(toformat,Integer.getInteger(fileid));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST,"database error" + e.getMessage());
        }

        if(fileUrl == null || fileUrl.isEmpty()){
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST,"Could not convert file");
        }
        return new UrlUploadResponse(HttpStatusCode.OK,fileUrl);
    }
}
