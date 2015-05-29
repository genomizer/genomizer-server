package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.HttpStatusCode;
import java.util.ArrayList;
import java.util.Map;



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
            if(file.getChromosomes()==null)
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Chromosomes may not be null.");
            if(!file.getMean().equals("single")&&!file.getMean().equals("double")) {
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Incorrect mean, should be 'single' or 'double'.");
            }
            if(file.getReadsCutoff()==null){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "ReadsCutOff should not be null.");
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
        private Integer readsCutoff;

        @Expose
        private String chromosomes;

        public String getPreChipFile() {return preChipFile;}

        public String getPostChipFile() {return postChipFile;}

        public String getOutfile() {return outfile;}

        public String getMean() {return mean;}

        public Integer getReadsCutoff() {return readsCutoff;}

        public String getChromosomes() {return chromosomes;}


        @Override
        public String toString() {
            return "RatioProcessFile{" +
                    "preChipFile='" + preChipFile + '\'' +
                    ", postChipFile='" + postChipFile + '\'' +
                    ", outfile='" + outfile + '\'' +
                    ", mean='" + mean + '\'' +
                    ", readsCutoff=" + readsCutoff + '\'' +
                    ", chromosomes=" + chromosomes +
                    '}';
        }

        /**
         * Call upon a ratio processing with correct parameters.
         *
         * @param filePaths
         */
        public void ProcessFile(Map.Entry<String, String> filePaths) {

            String preChipPath = filePaths.getValue() + getPreChipFile();
            String postChipPath = filePaths.getValue() + getPostChipFile();


            throw new UnsupportedOperationException("Error when processing. Ratio processing not yet implemented!");
        }
    }
}
