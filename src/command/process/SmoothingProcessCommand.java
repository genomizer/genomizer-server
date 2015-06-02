package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.subClasses.FileMethods;
import org.apache.commons.io.FileUtils;
import process.Smooth;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import server.Debug;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import static command.Command.initDB;

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
            callables.add(file.getCallable(expID, profileFilesDir));
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
        public void processFile(String expId, String profileFilesDir)
                throws ValidateException, InterruptedException, IOException, SQLException {

            File infileFile = new File(profileFilesDir + "/" + infile);
            File outfileFile = new File(profileFilesDir + "/" + outfile);
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
                    infileFile.getAbsolutePath(),
                    getWindowSize(),
                    meanOrMedian,
                    getMinSmooth(),
                    0,
                    0,
                    outfileFile.getAbsolutePath());

            // Add generated file to the database.
            FileTuple outTuple = null;
            try (DatabaseAccessor db = initDB()) {
                FileMethods fileMethods = db.getFileMethods();
                FileTuple inTuple = fileMethods.getFileTuple(infileFile.getAbsolutePath());

                long fileSize = FileUtils.sizeOf(new File(outfileFile.getAbsolutePath()));

                outTuple = fileMethods.addNewFileWithStatus(
                        /* expId = */ expId,
                        /* fileType = */ FileTuple.PROFILE,
                        /* fileName = */ outfile,
                        /* inputFilename = */ infile,
                        /* metaData = */
                        "window size: " + getWindowSize() +
                        ", meanOrMedian: " + getMeanOrMedian() +
                        ", minSmooth: " + getMinSmooth(),
                        /* author = */ "Generated by Genomizer",
                        /* uploader = */ "Generated by Genomizer",
                        /* isPrivate = */ false,
                        /* genomeVersion =*/ inTuple.grVersion,
                        /* md5 = */ "",
                        /* status */ "Done");
                Debug.log("Adding to DB: " + outTuple);
                fileMethods.updateFileSize(outTuple.id, fileSize);
            } catch (Exception e) {
                e.printStackTrace();
                Debug.log("Unable to add " + getOutfile() + " to DB");
                Debug.log("Removing out file");
                new File(profileFilesDir + "/" + getOutfile()).delete();
                if (outTuple != null) {
                    initDB().deleteFile(outTuple.id);
                }
                throw e;
            }
        }

        public Callable<Response> getCallable(final String expId, final String profileFilesDir) {
            return new Callable<Response>() {
                @Override
                public Response call() throws Exception {
                    try {
                        processFile(expId, profileFilesDir);
                        return new ProcessResponse(HttpStatusCode.OK,
                                "Smoothing process for " + expId +
                                " completed successfully, outfile: " + outfile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ProcessResponse(HttpStatusCode
                                .INTERNAL_SERVER_ERROR,
                                "Unable to perform stepping for " + expId +
                                ": " + e.getMessage());
                    }
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
