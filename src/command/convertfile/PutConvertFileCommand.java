package command.convertfile;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import conversion.ConversionHandler;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.MinimalResponse;
import response.Response;
import response.HttpStatusCode;

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
        //TODO convert with filepath and currentformat + toformat. and stuff
        return new MinimalResponse(HttpStatusCode.NO_CONTENT);
    }
}
