package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.HttpStatusCode;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by dv13jen on 2015-05-28.
 */
public class StepProcessCommand extends ProcessCommand {
    @Override
    public void doProcess(Map.Entry<String, String> filePath) {
        for (StepProcessFile file : files) {
            file.ProcessFile(filePath);
        }
    }

    @Override
    public void validate() throws ValidateException {
        for(StepProcessFile file: files) {
            Command.validateName(file.getInfile(), MaxLength.FILE_FILENAME, "Infile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            if(file.getStepSize()<0) {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Error validating StepProcessCommand. " +
                        "StepSize can not be less than 0");
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

    public class StepProcessFile {

        /**
         * Class is used to start a single step processing with correct parameters.
         */

        @Expose
        private String infile;

        @Expose
        private String outfile;

        @Expose
        private int stepSize;


        public String getInfile() {return infile;}

        public String getOutfile() {return outfile;}

        public int getStepSize() {return stepSize;}

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
         * @param filePaths
         */
        public void ProcessFile(Map.Entry<String, String> filePaths) {
            throw new UnsupportedOperationException("Error when processing. Step processing not implemented.");
        }

    }
}
