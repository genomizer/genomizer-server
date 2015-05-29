package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import process.Step;
import response.HttpStatusCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


/**
 * Class is used to handle step processing. The command can include multiple
 * file packages to run one at a time.
 */
public class StepProcessCommand extends ProcessCommand {
    @Override
    public void doProcess(Map.Entry<String, String> filePath)
            throws InterruptedException, ValidateException, IOException {
        for (StepProcessFile file : files) {
            file.ProcessFile(filePath);
        }
    }

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
                        "StepSize must be a positive integer");
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
    protected ArrayList<StepProcessFile> files;

    public class StepProcessFile {

        /**
         * Class is used to start a single step processing with correct
         * parameters.
         */

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

        /**
         * Call upon a single step processing with correct parameters.
         * @param filePaths The paths to the infile and outfile.
         */
        public void ProcessFile(Map.Entry<String, String> filePaths)
                throws InterruptedException, ValidateException, IOException {
            Step.runStep(
                    filePaths.getKey() + "/" + infile,
                    filePaths.getValue() + "/" + outfile,
                    stepSize);
        }
    }
}
