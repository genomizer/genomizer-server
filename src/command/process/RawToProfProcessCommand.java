package command.process;

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.FileTuple;
import database.containers.Genome;
import database.subClasses.FileMethods;
import org.apache.commons.io.FileUtils;
import process.ProcessException;
import process.RawToProfileConverter;
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
 * The class is a processing command that holds a list of
 * RawToProfProcessFile (also implemented here).
 * A RawToProfProcessFile is used to call upon the actual raw to profile
 * processing with the correct parameters.
 */
public class RawToProfProcessCommand extends ProcessCommand {

    /**
     * Validate the infile, outfile and genomeVersion on each
     * RawToProfProcessFile to make sure they have valid
     * characters and are a correct length.
     * @throws ValidateException
     */
    @Override
    public void validate() throws ValidateException {
        for (RawToProfProcessFile file : files) {
            Command.validateName(
                    file.getInfile(),
                    MaxLength.FILE_FILENAME,
                    "Infile");
            Command.validateName(
                    file.getOutfile(),
                    MaxLength.FILE_FILENAME,
                    "Outfile");
            Command.validateName(
                    file.getGenomeVersion(),
                    MaxLength.GENOME_VERSION,
                    "Genome version");
            Command.validateExists(
                    file.getParams(),
                    MaxLength.FILE_METADATA,
                    "Parameters");
            if (file.shouldKeepSam() == null) {
                throw new ValidateException(
                        HttpStatusCode.BAD_REQUEST,
                        "Specify if .SAM file should be kept.");
            }
        }
    }

    public ArrayList<RawToProfProcessFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "RawToProfileCommand{" +
               "files=" + files +
               '}';
    }

    @Expose
    protected ArrayList<RawToProfProcessFile> files;

    @Override
    protected Collection<Callable<Response>> getCallables(
            String rawFilesDir,
            String profileFilesDir) {
        Collection<Callable<Response>> callables = new ArrayList<>();
        for (RawToProfProcessFile file : files) {
            callables.add(file.getCallable(expID, rawFilesDir, profileFilesDir));
        }
        return callables;
    }

    /**
     * Class is used to start a single raw to profile processing with correct
     * parameters.
     */
    public class RawToProfProcessFile {

        @Expose
        protected String infile;

        @Expose
        protected String outfile;

        @Expose
        protected String params;

        @Expose
        protected String genomeVersion;

        @Expose
        protected Boolean keepSam;


        public String getInfile() {
            return infile;
        }

        public String getOutfile() {
            return outfile;
        }

        public String getParams() {
            return params;
        }

        public String getGenomeVersion() {
            return genomeVersion;
        }

        public Boolean shouldKeepSam() {
            return keepSam;
        }

        @Override
        public String toString() {
            return "RawToProfProcessFile{" +
                   "infile='" + infile + '\'' +
                   ", outfile='" + outfile + '\'' +
                   ", params='" + params + '\'' +
                   ", genomeVersion='" + genomeVersion + '\'' +
                   ", keepSam=" + keepSam +
                   '}';
        }

        /**
         * Call upon a single raw to profile processing with correct parameters.
         *
         * @param rawFilesDir Location of raw files for experiment.
         * @param profileFilesDir Location of profile files for experiment.
         * @throws IOException
         * @throws SQLException
         * @throws ProcessException
         */
        public void processFile(
                String expId,
                String rawFilesDir,
                String profileFilesDir)
                throws IOException, SQLException, ProcessException {

            //Get the genome information from the database.
            Genome g;
            try (DatabaseAccessor db = initDB()) {
                g = db.getGenomeRelease(getGenomeVersion());
            }

            if (g == null) {
                throw new IOException(
                        "Could not find genome version: " +
                                getGenomeVersion());
            }

            //Get the path of the genome.
            String genomeFolderPath = g.getFolderPath();
            //Get the prefix of the genome files.
            String genomeFilePrefix = g.getFilePrefix();

            if (genomeFilePrefix == null) {
                String msg = "Error when processing. Could not get "
                        + "genomeFilePrefix: "
                        + genomeVersion;
                Debug.log(msg);
                throw new IOException(msg);
            }

            if (genomeFolderPath == null) {
                String msg = "Error when processing. Could not get "
                        + "genomeFolderPath: "
                        + genomeVersion;
                Debug.log(msg);
                throw new IOException(msg);
            }

            String referenceGenome = genomeFolderPath + genomeFilePrefix;

            Debug.log("\nParameters for raw to profile:" +
                      "    - params: " + getParams() +
                      "    - infile: " + getInfile() +
                      "    - outfile: " + getOutfile() +
                      "    - should keep .SAM: " + shouldKeepSam() +
                      "    - Genome version: " + getGenomeVersion() +
                      "    - Genome version location: " + referenceGenome +
                      "    - infile dir: " + rawFilesDir +
                      "    - outfile dir: " + profileFilesDir +
                      "\n" );

            File[] outFiles = RawToProfileConverter.procedureRaw(
                    getParams(),
                    getInfile(),
                    getOutfile(),
                    shouldKeepSam(),
                    getGenomeVersion(),
                    referenceGenome,
                    rawFilesDir,
                    profileFilesDir);


            String inPath = new File(rawFilesDir + "/" + infile).getAbsolutePath();

            // Add generated file to the database.

            try {

                for (File outFile: outFiles) {

                    FileMethods fileMethods = initDB().getFileMethods();
                    FileTuple inTuple = fileMethods.getFileTuple(inPath);

                    long fileSize = FileUtils.sizeOf(outFile);

                    FileTuple outTuple = fileMethods.addNewFileWithStatus(
                        /* expId = */ expId,
                        /* fileType = */ FileTuple.PROFILE,
                        /* fileName = */ outFile.getName(),
                        /* inputFilename = */ infile,
                        /* metaData = */ params,
                        /* author = */ "Generated by Genomizer",
                        /* uploader = */ "Generated by Genomizer",
                        /* isPrivate = */ false,
                        /* genomeVersion =*/ getGenomeVersion(),
                        /* md5 = */ "",
                        /* status */ "Done");
                    Debug.log("Adding to DB: " + outTuple);
                    fileMethods.updateFileSize(outTuple.id, fileSize);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Debug.log("Unable to add " + getOutfile() + " to DB");

                for (File outfile: outFiles) {
                    Debug.log("Removing out file: " + outfile.getAbsolutePath());
                    try {
                        outfile.delete();
                        FileMethods methods = initDB().getFileMethods();
                        FileTuple outTuple = methods.getFileTuple(outfile.getAbsolutePath());
                        methods.deleteFile(outTuple.id);
                    } catch (Exception ignored) {}
                }

                throw e;
            }
        }

        public Callable<Response> getCallable(
                final String expId,
                final String rawFilesDir,
                final String profileFilesDir) {
            return new Callable<Response>() {
                @Override
                public Response call() throws Exception {
                    try {
                        processFile(expId, rawFilesDir, profileFilesDir);
                        return new ProcessResponse(HttpStatusCode.OK,
                                "Raw to profile process for " + expId +
                                " completed successfully, outfile: " + outfile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new ProcessResponse(HttpStatusCode
                                .INTERNAL_SERVER_ERROR,
                                "Error when processing. Could not execute raw" +
                                " to profile process: " +
                                e.getClass().getName() + ": " + e.getMessage());
                    }
                }
            };
        }
    }
}
