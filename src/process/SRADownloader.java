package process;

import server.ServerSettings;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor{

    private final String prefetchExecutable = "sra-toolkit/prefetch";
    private final String fastqDumpExecutable ="sra-toolkit/fastq-dump";
    private final String metaDataQuery = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = ServerSettings.fileLocation;


    /**
     * Downloads a file from the Sequence Read Archive
     *
     * @param runID the SRA file name
     * @throws IOException
     * @throws InterruptedException
     *
     */
    public void download(String runID) throws IOException, InterruptedException {

        String command[] = parse(fastqDumpExecutable + " -O " + dir + "/sra/ " + runID);
        String result = executeProgram(command);

        File downloaded = new File(dir + "/sra/" + runID + ".fastq");

        if (!downloaded.exists())
            System.out.println(result);
        else
            System.out.println("Successfully downloaded file: " + runID);

        System.out.println("Downloaded file size= " + downloaded.length());
    }

    /**
     * Retrieves meta data for a run file
     *
     * @param runID
     * @param studyID the study to which the run file belongs
     * @throws IOException
     * @throws InterruptedException
     */
    public void getMetaData(String runID, String studyID) throws IOException, InterruptedException {

        String command[] = parse("wget -O " + dir + "/sra/temp.csv" + " " + metaDataQuery + studyID);
        String result = executeCommand(command);
        System.out.println(result);

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
            System.out.println(sh.getFileSize("SRR1970533"));
            sh.download("SRR1970533");
            sh.getMetaData("SRR1970533","SRP056905");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
