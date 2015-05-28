package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import process.Ratio;
import process.Step;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Class is used to handle step processing. The command can include multiple file packages to run one at a time.
 */
public class StepProcessCommand extends ProcessCommand {

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for(StepProcessFile file: files) {
            Command.validateName(file.getInfile(), MaxLength.FILE_FILENAME, "Infile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            if(file.getStepSize()==null) {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Error validating StepProcessCommand. " +
                                                                        "StepSize can not be null.");
            }
            if(file.getStepSize()<1) {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Error validating StepProcessCommand. " +
                        "Step size must be a positive integer");
            }
        }

    }

    public ArrayList<StepProcessFile> getFiles() {return files;}

    @Override
    public String toString() {
        return "StepProcessingCommand{" +
                "files=" + files +
                '}';
    }
    @Expose
    private ArrayList<StepProcessFile> files;

    @Override
    protected Collection<Callable<Response>> getCallables() {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (StepProcessFile file: files) {
            callables.add(file.getCallable());
        }
        return callables;
    }

    public class StepProcessFile {

        /**
         * Class is used to start a single step processing with correct parameters.
         */

        @Expose
        private String infile;

        @Expose
        private String outfile;

        @Expose
        private Integer stepSize;


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

//        /**
//         * Call upon a single step processing with correct parameters.
//         * @param filePaths
//         */
//        public void processFile(Map.Entry<String, String> filePaths) {
//            throw new UnsupportedOperationException("Error when processing. Step processing not implemented.");
//        }

        public Callable<Response> getCallable() {
            return new Callable<Response>() {
                @SuppressWarnings("TryWithIdenticalCatches")
                @Override
                public Response call() throws Exception {
                    try {
                        Step.runStep(infile, outfile, stepSize);
                        return new ProcessResponse(HttpStatusCode.OK);
                    } catch (ValidateException ve) {
                        ve.printStackTrace();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return new ProcessResponse(HttpStatusCode.INTERNAL_SERVER_ERROR);
                }
            };
        }
    }
}
