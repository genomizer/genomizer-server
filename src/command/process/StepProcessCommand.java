package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import process.Step;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;


/**
 * Class is used to handle step processing. The command can include multiple
 * file packages to run one at a time.
 */
public class StepProcessCommand extends ProcessCommand {

    @Expose
    protected ArrayList<StepProcessFile> files;

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (StepProcessFile file : files) {
            Command.validateName(
                    file.getInfile(),
                    MaxLength.FILE_FILENAME,
                    "Infile");
            Command.validateName(
                    file.getOutfile(),
                    MaxLength.FILE_FILENAME,
                    "Outfile");
            if (file.getStepSize() == null) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Error validating StepProcessCommand. " +
                        "StepSize can not be null.");
            }
            if (file.getStepSize() < 1) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Error validating StepProcessCommand. " +
                        "Step size must be a positive integer");
            }
        }

    }

    @Override
    protected Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir) {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (StepProcessFile file : files) {
            callables.add(file.getCallable(profileFilesDir));
        }
        return callables;
    }

    /**
     * Class is used to start a single step processing with correct parameters.
     */
    public class StepProcessFile {

        @Expose
        protected String infile;

        @Expose
        protected String outfile;

        @Expose
        protected Integer stepSize;

        public String getInfile() {return infile;}

        public String getOutfile() {return outfile;}

        public Integer getStepSize() {return stepSize;}

        @Override
        public String toString() {
            return "RawToProfProcessFile{" +
                   "infile='" + infile + '\'' +
                   ", outfile='" + outfile + '\'' +
                   ", stepSize='" + stepSize + '\'' +
                   '}';
        }

        public Callable<Response> getCallable(final String profileFilesDir) {
            return new Callable<Response>() {
                @Override
                public Response call() throws Exception {
                    try {
                        Step.runStep(
                                profileFilesDir + "/" + infile,
                                profileFilesDir + "/" + outfile,
                                stepSize);
                        return new ProcessResponse(HttpStatusCode.OK);
                    } catch (ValidateException | InterruptedException |
                            IOException e) {
                        e.printStackTrace();
                        Debug.log(
                                "Unable to perform stepping: " +
                                e.getMessage());
                        return new ProcessResponse(
                                HttpStatusCode.INTERNAL_SERVER_ERROR,
                                e.getMessage());
                    }
                }
            };
        }
    }

    public ArrayList<StepProcessFile> getFiles() {return files;}

    @Override
    public String toString() {
        return "StepProcessingCommand{" +
               "files=" + files +
               '}';
    }
}
