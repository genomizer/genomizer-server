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

public class ProcessCommands_new extends Command {

    @Expose
    private String expId = null;

    private Map.Entry<String,String> filepaths;

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

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(expId, MaxLength.EXPID, "Experiment ID");

        if (processCommands == null || processCommands.size() < 1) {
            throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
                    "processes for the experiment.");
        }

        for (ProcessCommand processCommand : processCommands) {
            processCommand.validate();
//                    MaxLength.FILE_EXPID, "Infile");
//            validateName(processCommands.get(i).getFiles().get(i).getOutfile(),
//                    MaxLength.FILE_EXPID, "Outfile");
//            validateName(processCommands.get(i).getFiles().get(i).getGenomeVersion(),
//                    MaxLength.GENOME_VERSION, "Genome version");
        }

    }

    @Override
    public Response execute() {
        setFilePaths(expId);

        for (ProcessCommand pC: processCommands) {
            try{
                pC.doProcess(filepaths);
            } catch(UnsupportedOperationException e){
                return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
            }
        }
        return new ProcessResponse(HttpStatusCode.OK, "Processing of experiment: "+expId+" has completed.");
    }

    public Response setFilePaths(String expId) {
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

