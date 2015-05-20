package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.UserRights;
import command.ValidateException;
import database.constants.MaxLength;
import database.subClasses.UserMethods;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;

import java.util.ArrayList;

/**
 * Command to handle a list of processes to execute sequentially on an experiment.
 * Created by dv13jen on 2015-05-19.
 */
public class PutProcessCommands extends Command{

    @Expose
    private String expId = null;

    @Expose
    private ArrayList<ProcessCommands> processCommands = new ArrayList<>();

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
    }

    @Override
    public void validate() throws ValidateException {
        hasRights(UserRights.getRights(this.getClass()));
        validateName(expId, MaxLength.EXPID, "Experiment ID");

        if(processCommands == null || processCommands.size() < 1) {
            throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Specify " +
                    "processes for the experiment.");
        }

        for(int i =0;i<processCommands.size();i++){
            validateName(processCommands.get(i).getInfile(),
                    MaxLength.FILE_EXPID, "Infile");
            validateName(processCommands.get(i).getOutfile(),
                    MaxLength.FILE_EXPID, "Outfile");
            validateName(processCommands.get(i).getGenomeVersion(),
                    MaxLength.GENOME_VERSION, "Genome version");
        }
    }

    public PutProcessCommands(){

        for(ProcessCommands pC : processCommands){
            if(pC.getType().equals("bowtie")){
                Process p = new RawToProfileProcess(pC.getType(), pC.getInfile(), pC.getOutfile(), pC.getParams(),
                        pC.getKeepSam(), pC.getGenomeVersion());
                processes.add(p);
            }
        }
    }

    @Override
    public Response execute() {

        for (Process pC: processes) {
            try{
                pC.runProcess();
            } catch(UnsupportedOperationException e){
                //return new ErrorResponse(HttpStatusCode.BAD_REQUEST, e.getMessage());
            }
        }
        //return new ProcessResponse(HttpStatusCode.OK, "Processing of experiment: "+expId+" has completed.");
        return null;
    }
}
