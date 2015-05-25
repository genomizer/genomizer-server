package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.subClasses.UserMethods;
import response.HttpStatusCode;
import response.MinimalResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.util.HashMap;
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
    public void setFields(String uri, HashMap<String, String> query,
                          String uuid, UserMethods.UserType userType) {
        super.setFields(uri, query, uuid, userType);
        this.userType = userType;
    }
}

