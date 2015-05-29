package command.process;

import com.google.gson.annotations.Expose;
import command.*;
import database.constants.MaxLength;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * The class handles processing a list of processing commands. The list of
 * commands each have a list of files to
 * process.
 */
public class ProcessCommands extends Command {

    @Expose
    protected String expId;

    @Expose
    protected ArrayList<ProcessCommand> processCommands;

    private String rawFilesDir;
    private String profileFilesDir;
    private ProcessPool pool = Doorman.getProcessPool();

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
     * Validate that the user has correct rights to use the command and that
     * the json information is in
     * correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(expId, MaxLength.EXPID, "Experiment ID");

        if (processCommands == null || processCommands.size() < 1) {
            throw new ValidateException(
                    HttpStatusCode.BAD_REQUEST,
                    "Specify processes for the experiment.");
        }

        for (ProcessCommand processCommand : processCommands) {
            processCommand.validate();
        }

        validateCommandOrder();
    }

    // Validate that the list of ProcessCommands is in the correct order.
    private void validateCommandOrder() throws ValidateException {
        ArrayList<ProcessCommand> before = new ArrayList<>();

        HashSet<Class<? extends  ProcessCommand>> rawToProfAllowedBeforeSet
                = new HashSet<>();

        HashSet<Class<? extends  ProcessCommand>> ratioAllowedBeforeSet
                = new HashSet<>();
        ratioAllowedBeforeSet.add(RawToProfProcessCommand.class);
        ratioAllowedBeforeSet.add(SmoothingProcessCommand.class);
        ratioAllowedBeforeSet.add(StepProcessCommand.class);

        HashSet<Class<? extends  ProcessCommand>> smoothingAllowedBeforeSet
                = new HashSet<>();
        smoothingAllowedBeforeSet.add(RawToProfProcessCommand.class);
        smoothingAllowedBeforeSet.add(RatioProcessCommand.class);

        HashSet<Class<? extends  ProcessCommand>> stepAllowedBeforeSet
                = new HashSet<>();
        stepAllowedBeforeSet.add(RawToProfProcessCommand.class);
        stepAllowedBeforeSet.add(RatioProcessCommand.class);
        stepAllowedBeforeSet.add(SmoothingProcessCommand.class);

        for (ProcessCommand processCommand : processCommands) {
            if (processCommand.getClass().equals(RawToProfProcessCommand.class)) {
                validateCommandsAllowedBefore(before,
                        rawToProfAllowedBeforeSet, processCommand);
            }
            else if (processCommand.getClass().equals(RatioProcessCommand.class)) {
                validateCommandsAllowedBefore(before,
                        ratioAllowedBeforeSet, processCommand);
            }
            else if (processCommand.getClass().equals(SmoothingProcessCommand.class)) {
                validateCommandsAllowedBefore(before,
                        smoothingAllowedBeforeSet, processCommand);

            }
            else if (processCommand.getClass().equals(StepProcessCommand.class)) {
                validateCommandsAllowedBefore(before,
                        stepAllowedBeforeSet, processCommand);
            }
            else {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Unknown process command in process commands list!");
            }

            before.add(processCommand);
        }
    }

    // Helper used by validateCommandOrder().
    private void validateCommandsAllowedBefore(List<ProcessCommand> before,
                                              Set<Class<? extends ProcessCommand>> allowed,
                                              ProcessCommand current)
            throws ValidateException
    {
        for (ProcessCommand processCommand : before) {
            if (!allowed.contains(processCommand.getClass())) {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Wrong command order: " + processCommand.getClass()
                                + "not allowed before " + current.getClass());
            }
        }
    }

    /**
     * Run processing on all commands.
     * @return a response of the total processing. A problem during any part
     * of the process of even a single file
     * will result in a ErrorResponse.
     */
    @Override
    public Response execute() {
        for (ProcessCommand processCommand : processCommands) {
            processCommand.setExpID(expId);
        }

        startProcessingThread();

        return new ProcessResponse(
                HttpStatusCode.OK,
                "Processing of experiment " + expId + " has begun.");

    }

    private void startProcessingThread() {
        new Thread() {
            @Override
            public void run() {
                doProcesses();
            }
        }.start();
    }

    public void doProcesses() {
        try {
            rawFilesDir = fetchRawFilesDirFromDB(expId);
            profileFilesDir = fetchProfileFilesDirFromDB(expId);
            for (ProcessCommand processCommand : processCommands) {
                processCommand
                        .doProcess(pool, rawFilesDir, profileFilesDir);
            }
        } catch (ExecutionException | InterruptedException | SQLException |
                IOException e) {
            e.printStackTrace();
        }
    }

    private String fetchProfileFilesDirFromDB(String expId)
            throws IOException, SQLException {
        return initDB().getFilePathGenerator().getProfileFolderPath(expId);
    }

    private String fetchRawFilesDirFromDB(String expId)
            throws IOException, SQLException {
        return initDB().getFilePathGenerator().getProfileFolderPath(expId);
    }

    public void setPool(ProcessPool pool) {
        this.pool = pool;
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

