package process;

import java.io.*;

/**
 * Created by oi12fun & tfy12jsg on 2015-04-29.
 */
public class SRADownloader extends Executor{

    private final String prefetchExecutable = "sra-toolkit/prefetch";
    private final String fastqDumpExecutable ="sra-toolkit/fastq-dump";
    private final String metaDataQuery = "http://trace.ncbi.nlm.nih.gov/Traces/sra/sra.cgi?save=efetch&db=sra&rettype=runinfo&term=";
    private final String dir = "../data/sra/"; //ServerSettings.fileLocation + "sra/";

    public SRADownloader() {

    }

    /**
     * Downloads a file from the Sequence Read Archive
     *
     * @param filename the SRA file name
     * @throws IOException
     * @throws InterruptedException
     *
     */
    public void download(String filename) throws IOException, InterruptedException {

        String command[] = parse(fastqDumpExecutable + " -O " + dir + " " + filename);
        String result = executeProgram(command);
        System.out.println(result);
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        File downloaded = new File(System.getProperty("user.dir")+ "/data/sra/"+filename+".fastq");

        if (!downloaded.exists())
            throw new IOException("File download failed");

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
    //TODO : create folders for meta data file
    public void getMetaData(String runID, String studyID) throws IOException, InterruptedException {
        String command[] =parse("wget -O " + dir + "temp.csv" + " " + metaDataQuery + studyID);
        String result = executeShellCommand(command);
        System.out.println(result);

        File tempFile = new File(System.getProperty("user.dir")+ "/data/sra/temp.csv");
        File outFile = new File(System.getProperty("user.dir")+ "/data/sra/" + runID + ".csv");
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
