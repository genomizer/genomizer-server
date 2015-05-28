package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import response.HttpStatusCode;
import process.Smooth;
import response.ProcessResponse;
import response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Class is used to handle smooth processing. The command can include
 * multiple file packages to run one at a time.
 */
public class SmoothingProcessCommand extends ProcessCommand {

    @Expose
    protected ArrayList<SmoothingFile> files;

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (SmoothingFile file : files) {
            Command.validateName(
                    file.getInfile(),
                    MaxLength.FILE_FILENAME,
                    "Infile");
            Command.validateName(
                    file.getOutfile(),
                    MaxLength.FILE_FILENAME,
                    "Outfile");
            if (!file.getMeanOrMedian().equals("mean") &&
                !file.getMeanOrMedian().equals("median")) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Incorrect meanOrMedian, should be either 'mean' or " +
                        "'median'.");
            }
            if (file.getMinSmooth() >= file.getWindowSize()) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Incorrect input: minSmooth: " +
                        file.getMinSmooth() +
                        " must be smaller than windowSize: " +
                        file.getWindowSize() + ".");
            }
            if (file.getWindowSize() < 0) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Incorrect input: windowSize can not be" +
                        "less than 0");
            }
            if (file.getMinSmooth() < 0) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Incorrect input: minSmooth can not be" +
                        "less than 0");
            }
        }
    }

    @Override
    protected Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir) {

        Collection<Callable<Response>> callables = new ArrayList<>();
        for (SmoothingFile file : files) {
            callables.add(file.getCallable(profileFilesDir));
        }
        return callables;
    }

    public class SmoothingFile {

        /**
         * Class is used to start a smooth processing with correct parameters.
         */

        @Expose
        protected String infile;

        @Expose
        protected String outfile;

        @Expose
        protected Integer windowSize;

        @Expose
        protected String meanOrMedian;

        @Expose
        protected Integer minSmooth;


        public String getInfile() {
            return infile;
        }

        public String getOutfile() {
            return outfile;
        }

        public Integer getWindowSize() {
            return windowSize;
        }

        public String getMeanOrMedian() {
            return meanOrMedian;
        }

        public Integer getMinSmooth() {
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
         * @param profileFilesDir The path to the profile files of the
         *                        experiment.
         */
        public void processFile(String profileFilesDir)
                throws ValidateException, InterruptedException, IOException {

            String infileWithPath = profileFilesDir + "/" + infile;
            String outfileWithPath = profileFilesDir + "/" + outfile;
            Integer meanOrMedian;

            switch (getMeanOrMedian()) {
                case "mean":
                    meanOrMedian = 0;
                    break;
                case "median":
                    meanOrMedian = 1;
                    break;
                default:
                    throw new ValidateException(
                            0,
                            "Error during smoothing processing. Incorrect " +
                            "mean/median. " +
                            "Should be either 'mean' or 'median'.");
            }

            Smooth.runSmoothing(
                    infileWithPath,
                    getWindowSize(),
                    meanOrMedian,
                    getMinSmooth(),
                    0,
                    0,
                    outfileWithPath);
        }

        public Callable<Response> getCallable(final String profileFilesDir) {
            return new Callable<Response>() {
                @SuppressWarnings("TryWithIdenticalCatches")
                @Override
                public Response call() throws Exception {
                    try {
                        processFile(profileFilesDir);
                        return new ProcessResponse(HttpStatusCode.OK);
                    } catch (ValidateException ve) {
                        ve.printStackTrace();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return new ProcessResponse(HttpStatusCode
                            .INTERNAL_SERVER_ERROR);
                }
            };
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
}
