package command.process;

import com.google.gson.annotations.Expose;
import database.DatabaseAccessor;
import database.containers.Genome;
import process.ProcessHandler;
import server.Debug;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static command.Command.initDB;
/**
 + * Created by dv13jen on 2015-05-19.
 + */
public class RawToProfileProcess extends Process {

    private ProcessFiles files;
    private Map.Entry<String,String> filepaths;

public RawToProfileProcess(String type, ProcessFiles files, Map.Entry<String,String> filepaths) {
    super(type);
    this.files = files;
    this.filepaths = filepaths;
}
@Override
    public void runProcess() throws UnsupportedOperationException{
        DatabaseAccessor db = null;
        ProcessHandler processHandler;
        try {
            db = initDB();
            processHandler = new ProcessHandler();
            //Get the genome information from the database.
            Genome g = db.getGenomeRelease(files.getGenomeVersion());

            if (g == null) {
                throw new UnsupportedOperationException("Could not find genome version: " +
                        files.getGenomeVersion());
                } else {
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
                /*
	+ try {
	+ processHandler.rawToProfile(params, infile, outfile, keepSam, genomeVersion, referenceGenome, filepaths);
	+
	+ } catch (ProcessException e) {
	+ Debug.log("Error when processing. Could not execute raw to profile process. "+e.getMessage());
	+ throw new UnsupportedOperationException("Error when processing. Could not execute " +
	+ "raw to profile process");
	+ } catch (SQLException | IOException e) {
	+ Debug.log("Error when processing. Could not execute raw to profile process due to temporary " +
	+ "problems with database "+e.getMessage());
	+ throw new UnsupportedOperationException("Error when processing. Could not " +
	+ "execute raw to profile process due to temporary problems with database.");
	+ }
	+ */}
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
        }
    }