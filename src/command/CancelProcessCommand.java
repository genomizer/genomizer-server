package command;

import com.google.gson.annotations.Expose;
import database.subClasses.UserMethods;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.util.UUID;

/**
 * Created by c10mjn on 2015-05-11.
 */
public class CancelProcessCommand extends Command {
    @Expose
    private String PID = null;

    private final int pidLength = 36;

    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

    @Override
    public void validate() throws ValidateException {
        
        hasRights(UserRights.getRights(this.getClass()));
        validateExists(this.PID, this.pidLength, "Process ID");

    }

    @Override
    public Response execute() {
        ProcessPool pp = Doorman.getProcessPool();
        UUID processID = UUID.fromString(this.PID);
        pp.cancelProcess(processID);
        return new MinimalResponse(HttpStatusCode.OK);


    }

    @Override
    public void setFields(String uri, String username, UserMethods.UserType userType) {
        this.userType = userType;
    }
}

