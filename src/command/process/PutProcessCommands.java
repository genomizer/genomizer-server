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
import java.util.Map;
import java.util.UUID;

/**
 * Command to handle a list of processes to execute sequentially on an experiment.
 * Created by dv13jen on 2015-05-19.
 */
public class PutProcessCommands extends Command{

    private String username;
    private Map.Entry<String,String> filepaths;

    @Expose
    private String expId = null;

    @Expose
    private UUID PID;

    @Expose
    private ArrayList<FileToProcess> files = new ArrayList<>();

    private ArrayList<Process> processes = new ArrayList<>();


    @Override
    public int getExpectedNumberOfURIFields() {
        return 1;
    }

    /**
     * Overrides the original command in order to use the uri.
     * @param uri Contains the experiment id to fetch.
     * @param query the query of the command
     * @param uuid the UUID for the user who made the request.
     * @param userType the user type for the command caller.
     */
    @Override
    public void setFields(String uri, String query, String uuid, UserMethods.UserType userType) {
        super.setFields(uri, query, uuid, userType);
        username = uuid;
    }

    public UUID getPID() {
        return PID;
    }

    public void setPID(UUID PID) {
        this.PID = PID;
    }

    public String getUsername() {return username;}

    public String getExpId() {return expId;}

    public void setFilePaths() throws SQLException, IOException {
        DatabaseAccessor db;

        db = initDB();
        filepaths = db.processRawToProfile(expId);

        if(db.isConnected()){
            db.close();

        }
    }
    public String[] getFilePaths() {
        return new String[] {filepaths.getKey(), filepaths.getValue()};
    }

    /**
     * Handle the validation of the user input. Only handles bowtie processes atm
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(expId, MaxLength.EXPID, "Experiment ID");

        if(files == null || files.size() < 1) {
            throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
                    "files for the experiment.");
        }

        for (FileToProcess file : files) {
                validateName(file.getInfile(),
                        MaxLength.FILE_EXPID, "Infile");
                validateName(file.getOutfile(),
                        MaxLength.FILE_EXPID, "Outfile");
                validateName(file.getGenomeVersion(),
                        MaxLength.GENOME_VERSION, "Genome version");

        }
    }

    public PutProcessCommands(){

        for(FileToProcess file : files){

        }
    }

    @Override
    public Response execute() {

        for (Process pC: processes) {
            try{
                pC.runProcess();
            } catch(UnsupportedOperationException e){
                return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
            }
        }
        return new ProcessResponse(HttpStatusCode.CREATED, "Processing of experiment: "+expId+" has completed.");
    }
}
