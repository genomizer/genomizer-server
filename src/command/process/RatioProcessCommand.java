package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.constants.MaxLength;
import process.Ratio;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.Doorman;
import server.ProcessPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Class handles a ratio processing command. The command can include multiple
 * file packages to run one at a time.
 */
public class RatioProcessCommand extends ProcessCommand {

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (RatioProcessFile file : files) {
            Command.validateName(
                    file.getPreChipFile(),
                    MaxLength.FILE_FILENAME,
                    "PreChipFile");
            Command.validateName(
                    file.getPostChipFile(),
                    MaxLength.FILE_FILENAME,
                    "PostChipFile");
            Command.validateName(
                    file.getOutfile(),
                    MaxLength.FILE_FILENAME,
                    "Outfile");
            if (file.getChromosomes() == null) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Chromosomes may not be null.");
            }
            if (!file.getMean().equals("single") &&
                !file.getMean().equals("double")) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Incorrect mean, should be 'single' or 'double'.");
            }
            if (file.getReadsCutoff() < 0) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "ReadsCutOff should not be less than 0.");
            }
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

    @Override
    protected Collection<Callable<Response>> getCallables() {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (RatioProcessFile file: files) {
            callables.add(file.getCallable());
        }
        return callables;
    }

    public class RatioProcessFile {

        /**
         * Class is used to start a single ratio processing with correct
         * parameters.
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
        private String chromosomes;

        public String getPreChipFile() {return preChipFile;}

        public String getPostChipFile() {return postChipFile;}

        public String getOutfile() {return outfile;}

        public String getMean() {return mean;}

        public int getReadsCutoff() {return readsCutoff;}

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

//        /**
//         * Call upon a ratio processing with correct parameters.
//         *
//         * @param filePaths
//         */
//        public void processFile(Map.Entry<String, String> filePaths) {
//            throw new UnsupportedOperationException(
//                    "Error when processing. Ratio processing not yet implemented!");
//        }

        public Callable<Response> getCallable() {
            return new Callable<Response>() {
                @SuppressWarnings("TryWithIdenticalCatches")
                @Override
                public Response call() throws Exception {
                    try {
                        Ratio.runRatio(preChipFile, postChipFile, outfile,
                                Ratio.Mean.getMean(mean), readsCutoff, chromosomes);
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
