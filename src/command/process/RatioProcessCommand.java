package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import org.apache.commons.io.FileUtils;
import process.Ratio;
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
 * Class handles a ratio processing command. The command can include multiple
 * file packages to run one at a time.
 */
public class RatioProcessCommand extends ProcessCommand {

    @Expose
    protected ArrayList<RatioProcessFile> files;

    /**
     * Validate to make sure all input from clients is in correct format.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (RatioProcessFile file : files) {
            Command.validateName(
                    file.getInfile1(),
                    MaxLength.FILE_FILENAME,
                    "PreChipFile");
            Command.validateName(
                    file.getInfile2(),
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
            if (file.getReadsCutOff() < 0) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "ReadsCutOff should not be less than 0.");
            }
        }
    }

    @Override
    protected Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir) {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (RatioProcessFile file : files) {
            callables.add(file.getCallable(expID, profileFilesDir));
        }
        return callables;
    }

    /**
     * Class is used to start a single ratio processing with correct
     * parameters.
     */
    public class RatioProcessFile {

        @Expose
        protected String infile1;

        @Expose
        protected String infile2;

        @Expose
        protected String outfile;

        @Expose
        protected String mean;

        @Expose
        protected int readsCutOff;

        @Expose
        protected String chromosomes;

        public String getInfile1() {return infile1;}

        public String getInfile2() {return infile2;}

        public String getOutfile() {return outfile;}

        public String getMean() {return mean;}

        public int getReadsCutOff() {return readsCutOff;}

        public String getChromosomes() {return chromosomes;}


        @Override
        public String toString() {
            return "RatioProcessFile{" +
                   "infile1='" + infile1 + '\'' +
                   ", infile2='" + infile2 + '\'' +
                   ", outfile='" + outfile + '\'' +
                   ", mean='" + mean + '\'' +
                   ", readsCutOff=" + readsCutOff + '\'' +
                   ", chromosomes=" + chromosomes +
                   '}';
        }

        public void processFile(String expId, final String profileFilesDir)
                throws ValidateException, IOException,
                       InterruptedException, SQLException {

            File infile1File = new File(profileFilesDir + "/" + infile1);
            File infile2File = new File(profileFilesDir + "/" + infile2);
            File outfileFile = new File(profileFilesDir + "/" + outfile);

            Ratio.runRatio(
                    infile1File.getAbsolutePath(),
                    infile2File.getAbsolutePath(),
                    outfileFile.getAbsolutePath(),
                    Ratio.Mean.getMean(mean),
                    readsCutOff,
                    chromosomes);

            // Add generated file to the database.
            FileTuple outTuple = null;
            try (DatabaseAccessor db = initDB()) {

                FileTuple infile1Tuple = db.getFileMethods().getFileTuple(
                        infile1File.getAbsolutePath());

                long fileSize = FileUtils.sizeOf(outfileFile);

                outTuple = db.getFileMethods().addNewFileWithStatus(
                        /* expId = */ expId,
                        /* fileType = */ FileTuple.PROFILE,
                        /* fileName = */ outfile,
                        /* inputFilename = */ infile1 + "_" + infile2,
                        /* metaData = */
                        "mean: " + mean +
                        ", readsCutOff: " + readsCutOff +
                        ", chroms: " + chromosomes,
                        /* author = */ "Generated by Genomizer",
                        /* uploader = */ "Generated by Genomizer",
                        /* isPrivate = */ false,
                        /* genomeVersion =*/ infile1Tuple.grVersion,
                        /* md5 = */ "",
                        /* status = */ "Done");
                db.getFileMethods().updateFileSize(outTuple.id, fileSize);
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

        public Callable<Response> getCallable(
                final String expId,
                final String profileFilesDir) {
            return new Callable<Response>() {
                @Override
                public Response call() throws Exception {
                    try {
                        processFile(expId, profileFilesDir);

                        return new ProcessResponse(HttpStatusCode.OK);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new ProcessResponse(
                            HttpStatusCode
                                    .INTERNAL_SERVER_ERROR);
                }
            };
        }
    }

    public ArrayList<RatioProcessFile> getFiles() {return files;}

    @Override
    public String toString() {
        return "RatioProcessCommand{" +
               "files=" + files +
               '}';
    }
}
