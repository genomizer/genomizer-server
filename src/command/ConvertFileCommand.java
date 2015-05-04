package command;

import com.google.gson.annotations.Expose;
import conversion.ConversionHandler;
import database.constants.MaxLength;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

/**
 *
 *
 * Created by dv13thg on 2015-05-04.
 */
public class ConvertFileCommand extends Command{

    @Expose
    private String fileid;

    @Expose
    private String toformat;


    @Override
    public void validate() throws ValidateException {
        validateName(fileid, MaxLength.FILE_FILENAME,"file id");
        validateName(toformat,MaxLength.FILE_FILETYPE,"to format");
    }

    @Override
    public Response execute() {

        ConversionHandler convHandler = new ConversionHandler();
        //TODO convert with filepath and currentformat + toformat.
        return new MinimalResponse(StatusCode.NO_CONTENT);

    }
}
