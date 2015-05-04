package process;

import server.ErrorLogger;
import server.ServerSettings;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor {

    private final String http = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = ServerSettings.fileLocation;
    private final String ftpRoot = "ftp://ftp-trace.ncbi.nih.gov/sra/sra-instant/reads/ByRun/sra/";
    private final String outDir = dir + "/sra";

    /**
     *
     * @param runID
     *
     * @return file paths to run file and meta data file
     */
    public String[] downloadSRA(String runID) throws ProcessException {

        String filePaths[] = new String[2];

        try {

            filePaths[0] = getRunFile(runID);
            filePaths[1] = getMetaData(runID);

        } catch (RuntimeException e) {

            ErrorLogger.log("SRA", e.getMessage());

            if (e.getMessage().contains("No such directory"))
                throw new ProcessException("File " + runID + " does not exist");

            cleanUp(runID);

            throw new ProcessException("Could not download file");

        } catch (Exception e) {
            ErrorLogger.log("SRA", e.getMessage());
            cleanUp(runID);
            throw new ProcessException("Could not download file");
        }

        return filePaths;
    }
    /**
     * Downloads a file from the Sequence Read Archive
     *
     * @param runID the run file's ID
     * @return the downloaded file's path
     * @throws IOException
     * @throws InterruptedException
     *
     */
    private String getRunFile(String runID) throws IOException, InterruptedException, RuntimeException {

        String ftp = buildFTP(runID);

        String command[] = parse("wget -nc -P " + outDir + " " + ftp);
        File file = new File(outDir + runID + ".sra");

        executeCommand(command);

        return file.getAbsolutePath();
    }

    /**
     * Retrieves meta data for a run file and writes
     *
     * @param runID the run file's ID
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private String getMetaData(String runID) throws IOException, InterruptedException,
                                                                    RuntimeException {
        String command[] = parse("wget -O " + outDir +  "/" + runID + "_info.csv" + " " + http + runID);

        System.out.println(executeCommand(command));

        File outFile = new File(outDir + "/" + runID + "_info.csv");

        return outFile.getAbsolutePath();
    }

    /**
     * Builds the ftp download path for a run file
     *
     * @param runID
     * @return fpt download path
     */
    private String buildFTP(String runID) {

        StringBuilder sb = new StringBuilder();
        sb.append(ftpRoot);
        sb.append(runID.substring(0,3) + "/");
        sb.append(runID.substring(0,6) + "/");
        sb.append(runID + "/");
        sb.append(runID + ".sra");

        return sb.toString();
    }

    /**
     * Removes any files after unsuccessful download
     */
    private void cleanUp(String runID) {

        File metaFile = new File(outDir + runID + ".csv");
        File runFile = new File(outDir + runID + ".sra");

        if(runFile.exists())
            runFile.delete();

        if(metaFile.exists())
            metaFile.delete();

    }
    
    

}
