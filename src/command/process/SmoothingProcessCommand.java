package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;

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
        }
    }
}