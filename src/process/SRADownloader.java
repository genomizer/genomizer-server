package process;

import java.io.IOException;

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

        String command[] = parse(prefetchExecutable + " " + filename);
        String result = executeProgram(command);
        System.out.println(result);

    }

    /**
     * Retrieves meta data for a file (study)
     *
     * @param filename
     * @throws IOException
     * @throws InterruptedException
     */
    //TODO : create folders for meta data file, extract metadata for specific run
    public void getMetaData(String filename) throws IOException, InterruptedException {
        String command[] =parse("wget -O " + dir + filename + ".csv" + " " + metaDataQuery + filename);
        String result = executeShellCommand(command);
        System.out.println(result);
    }


    private String getFileSize(String filename) throws IOException, InterruptedException {
        String command[] = parse(prefetchExecutable + " -s " + filename);
        String result = executeProgram(command);

        return result;
    }



    public static void main(String[] args) {
        try {
            SRADownloader sh = new SRADownloader();
            System.out.println(sh.getFileSize("SRR1970533"));
            sh.download("SRR1970533");
            sh.getMetaData("SRP056905");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
