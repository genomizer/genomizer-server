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
 * The class handles processing a list of processing commands. The list of commands each have a list of files to
 * process.
 */
public class ProcessCommands extends Command {

    @Expose
    private String expId = null;

    private Map.Entry<String,String> filePaths;

    private long timestamp;

    private UUID PID;

    @Expose
    private ArrayList<ProcessCommand> processCommands;

    @Override
    public String toString() {
        return "ProcessCommands_new{" +
               "expId='" + expId + '\'' +
               ", processCommands=" + processCommands +
               '}';
    }

    @Override
    public int getExpectedNumberOfURIFields() {
        return 2;
    }

    @Override
    public void setFields(String uri, HashMap<String, String> query,
                          String username, UserMethods.UserType userType) {
        super.setFields(uri, query, username, userType);
    }

    /**
     * Validate that the user has correct rights to use the command and that the json information is in
     * correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(expId, MaxLength.EXPID, "Experiment ID");

        if (processCommands == null || processCommands.size() < 1) {
            throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                    "Specify processes for the experiment.");
        }

        for (ProcessCommand processCommand : processCommands) {
            processCommand.validate();
        }

    }

    /**
     * Run processing on all commands.
     * @return a response of the total processing. A problem during any part of the process of even a single file
     * will result in a ErrorResponse.
     */
    @Override
    public Response execute() {
        setFilePaths(expId);

        for (ProcessCommand pC: processCommands) {
            try{
                pC.doProcess(filePaths);
            } catch(UnsupportedOperationException e){
                return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
            }
        }
        return new ProcessResponse(HttpStatusCode.OK, "Processing of experiment: "+expId+" has completed.");
    }

    /**
     * Set filePaths using expId by connecting to the database.
     * @param expId the experiment Id of the processing.
     * @return a response if something went wrong, otherwise null
     */
    public Response setFilePaths(String expId) {
        DatabaseAccessor db = null;

        try {
            db = initDB();
            filePaths = db.processRawToProfile(expId);
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
        return new String[] {filePaths.getKey(), filePaths.getValue()};
    }

    public void setTimestamp(long currentTimeMillis) {
        this.timestamp = currentTimeMillis;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public ArrayList<ProcessCommand> getProcessCommands() {
        return processCommands;
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

