package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import process.ProcessException;
import process.RawToProfileConverter;
import response.HttpStatusCode;
import response.Response;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static command.Command.initDB;

/**
 * Class handles a ratio processing command. The command can include multiple file packages to run one at a time.
 */

public class RatioProcessCommand extends ProcessCommand {

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for(RatioProcessFile file: files) {
            Command.validateName(file.getPreChipFile(), MaxLength.FILE_FILENAME, "PreChipFile");
            Command.validateName(file.getPostChipFile(), MaxLength.FILE_FILENAME, "PostChipFile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            if(!file.getMean().equals("single")&&!file.getMean().equals("double")){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Incorrect mean, should be 'single' or 'double'.");
            }
            if(file.getReadsCutoff()<0){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "ReadsCutOff should not be less than 0.");
            }
        }
    }

    @Override
    public void doProcess(Map.Entry<String, String> filePath) {
        for(RatioProcessFile file: files) {
            file.ProcessFile(filePath);
        }
    }

    public ArrayList<RatioProcessFile> getFiles() {return files;}

    @Override
    public String toString() {
        return "RatioProcessCommand{" +
                "files=" + files +
                '}';
    }

    @Expose
    private ArrayList<RatioProcessFile> files;

    public class RatioProcessFile {

        /**
         * Class is used to start a single ratio processing with correct parameters.
         */

        @Expose
        private String preChipFile;

        @Expose
        private String postChipFile;

        @Expose
        private String outfile;

        @Expose
        private String mean;

        @Expose
        private int readsCutoff;

        @Expose
        private String chromosome;

        public String getPreChipFile() {return preChipFile;}

        public String getPostChipFile() {return postChipFile;}

        public String getOutfile() {return outfile;}

        public String getMean() {return mean;}

        public int getReadsCutoff() {return readsCutoff;}

        public String getChromosome() {return chromosome;}


        @Override
        public String toString() {
            return "RatioProcessFile{" +
                    "preChipFile='" + preChipFile + '\'' +
                    ", postChipFile='" + postChipFile + '\'' +
                    ", outfile='" + outfile + '\'' +
                    ", mean='" + mean + '\'' +
                    ", readsCutoff=" + readsCutoff + '\'' +
                    ", chromosome=" + chromosome +
                    '}';
        }

        /**
         * Call upon a ratio processing with correct parameters.
         *
         * @param filePaths
         */
        public void ProcessFile(Map.Entry<String, String> filePaths) {
            throw new UnsupportedOperationException("Error when processing. Ratio processing not yet implemented!");
        }
    }
}
