package process;

import server.ServerSettings;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor {

    private final String prefetchExecutable = "sra-toolkit/prefetch";
    private final String fastqDumpExecutable ="sra-toolkit/fastq-dump";
    private final String metaDataHTTP = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = ServerSettings.fileLocation;
    private String ftpRoot = "ftp://ftp-trace.ncbi.nih.gov/sra/sra-instant/reads/ByRun/sra/";


    /**
     * Downloads a file from the Sequence Read Archive
     *
     * @param runID the run file's ID
     * @throws IOException
     * @throws InterruptedException
     *
     */
    public void download(String runID) throws IOException, InterruptedException {
        String ftp = getFTP(runID);
        String command[] = parse("wget -P " + dir + "/sra/" + " " + ftp);
        String result = executeCommand(command);
        File downloaded = new File(dir + "/sra/" + runID + ".sra");

        if (!downloaded.exists())
            System.out.println(result);
        else
            System.out.println("Successfully downloaded file: " + runID);

        System.out.println("Downloaded file size = " + downloaded.length());
    }

    /**
     *
     * @param runID
     * @return
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

    /**
     * Retrieves meta data for a run file
     *
     * @param runID the run file's ID
     * @param studyID the study to which the run file belongs
     * @throws IOException
     * @throws InterruptedException
     */
    public void getMetaData(String runID, String studyID) throws IOException, InterruptedException {

        String command[] = parse("wget -O " + dir + "/sra/temp.csv" + " " + metaDataHTTP + studyID);
        String result = executeCommand(command);
        System.out.println(result);

        File tempFile = new File(dir + "/sra/temp.csv");

        if (!tempFile.exists())
            System.out.println("nu blev det fel");

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
    }

    private String getFileSize(String filename) throws IOException, InterruptedException {
        String command[] = parse(prefetchExecutable + " -s " + filename);
        String result = executeProgram(command);
        String[] fileSize = result.split(filename);
        return fileSize[1].trim();
    }


    public static void main(String[] args) {

        try {
            ServerSettings.readSettingsFile(System.getProperty("user.dir")+"/settings.cfg");
            SRADownloader sh = new SRADownloader();
            sh.download("SRR1970533");
            sh.getMetaData("SRR1970533","SRP056905");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
