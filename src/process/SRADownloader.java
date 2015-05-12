package process;

import server.ErrorLogger;
import server.ServerSettings;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor {

	public static final int FASTQPATH = 0;
	public static final int METADATAPATH = 1;

    private final String http = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = ServerSettings.fileLocation;
    private final String outDir = dir + "/sra/";
    private final String fastqDumpExecutable = "sra-toolkit/fastq-dump";

    /**
     * Downloads a run file and meta data from the Sequence Read Archive
     * @param runID the run file's ID
     * @return file paths to run file and meta data file
     *
     **/
    public String[] downloadFromSRA(String runID) throws ProcessException {

        String filePaths[] = new String[2];

        try {

            filePaths[0] = getRunFile(runID);
            filePaths[1] = getMetaData(runID);

        } catch (RuntimeException e) {

            ErrorLogger.log("SRA", e.getMessage());

            if (e.getMessage().contains("No such directory")) {

                throw new ProcessException("File " + runID + " does not exist");

            } else {

                throw new ProcessException("Could not download file");

            }

        } catch (Exception e) {

            ErrorLogger.log("SRA", e.getMessage());

            throw new ProcessException("Could not download file");

        }

        cleanUp(runID);

        return filePaths;
    }
    /**
     * Downloads a run file from the Sequence Read Archive
     *
     * @param runID the run file's ID
     * @return the downloaded file's path
     * @throws IOException
     * @throws InterruptedException
     *
     */
    private String getRunFile(String runID) throws IOException, InterruptedException, RuntimeException {

        String command[] = parse(fastqDumpExecutable + " -O "  + outDir + " " + runID);

        executeProgram(command);

        File file = new File(outDir + runID + ".fastq");

        if(!file.exists())
            throw new RuntimeException("Error while downloading file");

        return file.getAbsolutePath();
    }

    /**
     * Retrieves meta data for a run file
     *
     * @param runID the run file's ID
     * @return the file path
     * @throws IOException
     * @throws InterruptedException
     */
    private String getMetaData(String runID) throws IOException, InterruptedException,
                                                                    RuntimeException {
        String command[] = parse("wget -O " + outDir + runID + "_info.csv" + " " + http + runID);

        executeCommand(command);

        File outFile = new File(outDir + runID + "_info.csv");

        return outFile.getAbsolutePath();
    }


    /**
     * Removes temporary .sra files after download
     * @param runID the run file's ID
     */
    private void cleanUp(String runID) {

        File runFile = new File(outDir + runID + ".sra").getAbsoluteFile();

        if(runFile.exists())
            runFile.delete();

    }



}
