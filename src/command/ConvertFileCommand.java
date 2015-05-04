package command;

import com.google.gson.annotations.Expose;
import conversion.ConversionHandler;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

import javax.net.ssl.SSLEngineResult;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 *
 * Created by dv13thg on 2015-05-04.
 */
public class ConvertFileCommand extends Command{

    @Expose
    private String fileid;

    @Expose
    private String currentformat;

    @Expose
    private String toformat;


    @Override
    public void validate() throws ValidateException {
        validateName(fileid, MaxLength.FILE_FILENAME,"file id");
        validateName(currentformat,MaxLength.FILE_FILETYPE, "current format");
        validateName(toformat,MaxLength.FILE_FILETYPE,"to format");
    }

    @Override
    public Response execute() {
        DatabaseAccessor db = null;
        FileTuple file;
        try {
            db = initDB();
            file = db.getFileTuple(fileid);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            return new ErrorResponse(StatusCode.SERVICE_UNAVAILABLE,
                    e.getMessage());
        }

        if(file != null){
            ConversionHandler convHandler = new ConversionHandler();
            //TODO convert with filepath and currentformat + toformat.
            return new MinimalResponse(StatusCode.NO_CONTENT);
        } else {
            return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not find file");
        }

    }
}
