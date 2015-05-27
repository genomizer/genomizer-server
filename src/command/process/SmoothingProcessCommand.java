package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.HttpStatusCode;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dv13jen on 2015-05-27.
 */
public class SmoothingProcessCommand extends ProcessCommand {


    @Override
    public void validate() throws ValidateException {
        for (SmoothingFile file : files) {
            Command.validateName(file.getInfile(), MaxLength.FILE_FILENAME, "Infile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            if(!file.getMeanOrMedian().equals("mean")&&!file.getMeanOrMedian().equals("median")){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST,
                        "Incorrect meanOrMedian, should be either 'mean' or 'median'.");
            }
            if(file.getMinSmooth()>=file.getWindowSize()){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: minSmooth: "+
                        file.getMinSmooth()+" must be smaller than windowSize: "+file.getWindowSize()+".");
            }
            if(file.getWindowSize()<0){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: windowSize can not be" +
                        "less than 0");
            }
            if(file.getMinSmooth()<0){
                throw new ValidateException(HttpStatusCode.BAD_REQUEST, "Incorrect input: minSmooth can not be" +
                        "less than 0");
            }
       }
    }

    /**
     * Run through the list of SmoothingFiles and run processing on each with filePath as parameter.
     *
     * @param filePath associated with expId
     */
    @Override
    public void doProcess(Map.Entry<String, String> filePath) {
        for (SmoothingFile file : files) {
            file.ProcessFile(filePath);
        }
    }

    public ArrayList<SmoothingFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "SmoothingCommand{" +
                "files=" + files +
                '}';
    }

    @Expose
    private ArrayList<SmoothingFile> files;

    public class SmoothingFile {

        /**
         * Class is used to start a smooth processing with correct parameters.
         */

        @Expose
        private String infile;

        @Expose
        private String outfile;

        @Expose
        private int windowSize;

        @Expose
        private String meanOrMedian;

        @Expose
        private int minSmooth;


        public String getInfile() {
            return infile;
        }

        public String getOutfile() {
            return outfile;
        }

        public int getWindowSize() {
            return windowSize;
        }

        public String getMeanOrMedian() {
            return meanOrMedian;
        }

        public int getMinSmooth() {
            return minSmooth;
        }

        @Override
        public String toString() {
            return "SmoothingFile{" +
                    "infile='" + infile + '\'' +
                    ", outfile='" + outfile + '\'' +
                    ", windowSize='" + windowSize + '\'' +
                    ", meanOrMedian='" + meanOrMedian + '\'' +
                    ", minSmooth=" + minSmooth +
                    '}';
        }

        /**
         * Call upon a single smooth processing with correct parameters.
         *
         * @param filePaths
         */
        public void ProcessFile(Map.Entry<String, String> filePaths) {
            throw new UnsupportedOperationException("Error when processing. Smoothing not implemented.");
        }
    }
}
