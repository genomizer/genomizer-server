package process;

import server.ServerSettings;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor {

    private final String metaDataHTTP = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = ServerSettings.fileLocation;
    private final String ftpRoot = "ftp://ftp-trace.ncbi.nih.gov/sra/sra-instant/reads/ByRun/sra/";


    /**
     * Downloads a file from the Sequence Read Archive
     *
     * @param runID the run file's ID
     * @return the dowloaded file's path
     * @throws IOException
     * @throws InterruptedException
     *
     */
    public String download(String runID) throws IOException, InterruptedException {

        String ftp = getFTP(runID);

        String command[] = parse("wget -nc -P " + dir + "/sra/" + " " + ftp);
        File file = new File(dir + "/sra/" + runID + ".sra");

        executeCommand(command);

        return file.getAbsolutePath();
    }

    /**
     * Retrieves meta data for a run file and writes
     *
     * @param runID the run file's ID
     * @param studyID the study to which the run file belongs
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String getMetaData(String runID, String studyID) throws IOException, InterruptedException {

        String command[] = parse("wget -O " + dir + "/sra/temp.csv" + " " + metaDataHTTP + studyID);
        executeCommand(command);

        File tempFile = new File(dir + "/sra/temp.csv");

        File outFile = new File(dir + "/sra/" + runID + ".csv");
        BufferedReader br = new BufferedReader(new FileReader(tempFile));

        String line;

        while((line = br.readLine()) != null) {
            if (line.startsWith(runID)) {
                if(!outFile.exists())
                    outFile.createNewFile();

                FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(line);
                bw.close();

            }
        }

        tempFile.delete();

        return outFile.getAbsolutePath();
    }

    /**
     * Builds the ftp download path for a run file
     *
     * @param runID
     * @return fpt download path
     */
    private String getFTP(String runID) {

        StringBuilder sb = new StringBuilder();
        sb.append(ftpRoot);
        sb.append(runID.substring(0,3) + "/");
        sb.append(runID.substring(0,6) + "/");
        sb.append(runID + "/");
        sb.append(runID + ".sra");

        return sb.toString();
    }

}
