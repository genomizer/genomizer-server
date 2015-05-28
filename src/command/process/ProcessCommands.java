package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.constants.MaxLength;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * The class handles processing a list of processing commands. The list of commands each have a list of files to
 * process.
 */
public class ProcessCommands extends Command {

    @Expose
    protected String expId;

    @Expose
    protected ArrayList<ProcessCommand> processCommands;

    private String rawFilesDir;
    private String profileFilesDir;

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
        try {
            Map.Entry<String, String> filePaths = fetchFilePathsFromDB(expId);
            rawFilesDir = filePaths.getKey();
            profileFilesDir = filePaths.getValue();

            startProcessing();

            return new ProcessResponse(HttpStatusCode.OK, "Processing of experiment " + expId + " has begun.");

        } catch (IOException | SQLException e) {
            return new ProcessResponse(HttpStatusCode.NOT_FOUND, e.getMessage());
        }
    }

    private void startProcessing() {
        new Thread() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void run() {
                for (ProcessCommand processCommand: processCommands) {
                    try {
                        processCommand.doProcess(rawFilesDir, profileFilesDir);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * Set filePaths using expId by connecting to the database.
     * @param expId the experiment Id of the processing.
     * @return a response if something went wrong, otherwise null
     */
    public Map.Entry<String, String> fetchFilePathsFromDB(String expId)
            throws IOException, SQLException {
        return initDB().processRawToProfile(expId);
    }

//    public String[] getFilePaths() {
//        return new String[] {filePaths.getKey(), filePaths.getValue()};
//    }
//
//    public void setTimestamp(long currentTimeMillis) {
//        this.timestamp = currentTimeMillis;
//    }
//
//    public long getTimestamp(){
//        return this.timestamp;
//    }

    public ArrayList<ProcessCommand> getProcessCommands() {
        return processCommands;
    }

    public String getExpId() {
        return expId;
    }

//    public UUID getPID() {
//        return PID;
//    }
//
//    public void setPID(UUID PID) {
//        this.PID = PID;
//    }
//
//    public String getUsername() {
//        return Authenticate.getUsernameByID(uuid);
//    }
}

