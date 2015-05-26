package command.process;
import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 + * Command to handle a list of processes to execute sequentially on an experiment.
 + * Created by dv13jen on 2015-05-19.
 + */
public class PutProcessCommands extends Command{

    @Expose
    private String expId = null;

    private Map.Entry<String,String> filepaths;

    private long timestamp;

    private UUID PID;


    private ArrayList<Process> processes = new ArrayList<>();

    @Override
    public int getExpectedNumberOfURIFields() {
        return 2;
    }

    /**
     + * Overrides the original command in order to use the uri.
     + * @param uri Contains the experiment id to fetch.
     + * @param query the query of the command
     + * @param uuid the UUID for the user who made the request.
     + * @param userType the user type for the command caller.
     + */
    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String username, UserMethods.UserType userType) {
        super.setFields(uri, query, username, userType);
    }

    @Override
    public void validate() throws ValidateException {
    }



    @Override
    public Response execute() {

        setFilePaths();

        for (Process pC: processes) {
            try{
                pC.runProcess();
                } catch(UnsupportedOperationException e){
                return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
            }
        }
        return new ProcessResponse(HttpStatusCode.OK, "Processing of experiment: "+expId+" has completed.");

        }
    public Response setFilePaths() {
        DatabaseAccessor db = null;

        try {
            db = initDB();
            filepaths = db.processRawToProfile(expId);
        } catch (SQLException e) {
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    public String[] getFilePaths() {
        return new String[] {filepaths.getKey(), filepaths.getValue()};
    }
    public void setTimestamp(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
    }
    public long getTimestamp(){
        return this.timestamp;
    }


    public String getExpId() {
        return expId;
    }

    public UUID getPID() {
        return PID;
    }

    public void setPID(UUID PID) {
        this.PID = PID;
    }

    public String getUsername() {
        return userName;
    }
    }