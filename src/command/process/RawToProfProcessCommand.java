package command.process;
/**
 * File:        BowtieProcessCommand.java
 * Author:      Niklas Fries
 * Contact:     niklasf@cs.umu.se
 * Date:        2015-05-22
 */

import com.google.gson.annotations.Expose;
import command.Command;
import command.ValidateException;
import database.DatabaseAccessor;
import database.constants.MaxLength;
import database.containers.Genome;
import process.ProcessException;
import process.ProcessHandler;
import response.HttpStatusCode;
import server.Debug;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import static command.Command.initDB;

public class RawToProfProcessCommand extends ProcessCommand {

    @Override
    public void validate() throws ValidateException {
        for(RawToProfProcessFile file: files) {
            Command.validateName(file.getInfile(), MaxLength.FILE_FILENAME, "Infile");
            Command.validateName(file.getOutfile(), MaxLength.FILE_FILENAME, "Outfile");
            Command.validateName(file.getGenomeVersion(), MaxLength.GENOME_VERSION, "Genome version");
        }
    }

    @Override
    public void doProcess(Map.Entry<String,String> filePath) {
        System.out.println("doing raw to profile");
        for(RawToProfProcessFile file: files) {
            file.ProcessFile(filePath);
        }
    }

    @Expose
    private ArrayList<RawToProfProcessFile> files;

    public class RawToProfProcessFile {

        @Expose
        private String infile;

        @Expose
        private String outfile;

        @Expose
        private String params;

        @Expose
        private String genomeVersion;

        @Expose
        private boolean keepSam;


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

        public boolean shouldKeepSam() {
            return keepSam;
        }

        @Override
        public String toString() {
            return "BowtieProcessFile{" +
                   "infile='" + infile + '\'' +
                   ", outfile='" + outfile + '\'' +
                   ", params='" + params + '\'' +
                   ", genomeVersion='" + genomeVersion + '\'' +
                   ", keepSam=" + keepSam +
                   '}';
        }

        public void ProcessFile(Map.Entry<String,String> filePaths) {
            DatabaseAccessor db = null;

            throw new UnsupportedOperationException("not yet implemented");
            /*
            try {
                db = initDB();
                ProcessHandler processHandler = new ProcessHandler();
                //Get the genome information from the database.
                Genome g = db.getGenomeRelease(getGenomeVersion());

                if (g == null) {
                    throw new UnsupportedOperationException("Could not find genome version: " +
                            getGenomeVersion());
                }
                else {
                    //Get the path of the genome.
                    String genomeFolderPath = g.folderPath;
                    //Get the prefix of the genome files.
                    String genomeFilePrefix = g.getFilePrefix();

                    if (genomeFilePrefix == null) {
                        Debug.log("Error when processing. Could not get genomeFilePrefix: "
                                + genomeFilePrefix);
                        throw new UnsupportedOperationException("Error when processing. Could not get genomeFilePrefix: "
                                + genomeFilePrefix);
                    }

                    if (genomeFolderPath == null) {
                        Debug.log("Error when processing. Could not get genomeFolderPath: "
                                + genomeFolderPath);
                        throw new UnsupportedOperationException("Error when processing. Could not get genomeFolderPath: "
                                + genomeFolderPath);
                    }

                    String referenceGenome = genomeFolderPath + genomeFilePrefix;

                    try {
                        processHandler.executeRawToProfileProcess(getParams(), getInfile(), getOutfile(),
                                shouldKeepSam(), getGenomeVersion(), referenceGenome, filePaths);
                    }catch (ProcessException e){
                        Debug.log("Error when processing. Could not execute raw to profile process. " + e.getMessage());
                        throw new UnsupportedOperationException("Error when processing. Could not execute " + "raw to profile process");
                    }
                }
            } catch (SQLException | IOException e) {
                Debug.log("Error when processing. Could not execute raw to profile process due to temporary " +
                        "problems with database "+e.getMessage());
                throw new UnsupportedOperationException("Error when processing. Could not execute raw to " +
                        "profile process due to temporary problems with database.");
            } finally{
                if (db != null) {
                    db.close();
                }
            }
            */
        }

    }

    public ArrayList<RawToProfProcessFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "BowtieProcessCommand{" +
               "files=" + files +
               '}';
    }
}
