package process.test;

import static org.junit.Assert.*;

import command.ValidateException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import process.Bowtie2;
import server.ServerSettings;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class Bowtie2Test {
    private static String inFile1;
    private static String inFile2;
    private static String inFileU;
    private static File   outFile;
    private static String genomeRelease;

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String testDataDir =
                new File("resources/bowtie2TestData/").getCanonicalPath();

        inFile1 = testDataDir + File.separator + "testFile1.fastq";
        inFile2 = testDataDir + File.separator + "testFile2.fastq";
        inFileU = testDataDir + File.separator + "testFileU.fastq";
        outFile = new File(Util.replaceExtension(inFile1, ".sam"));

        genomeRelease = new File("resources/bowtie2/example/index").getCanonicalPath()
                + File.separator + "lambda_virus";

        ServerSettings.bowtie2Location =
                new File("resources/bowtie2/bowtie2").getCanonicalPath();
    }

    @After
    public void tearDown() throws IOException {
        try {
            Files.delete(outFile.toPath());
        }
        catch (NoSuchFileException e) {
            // ignore
        }
    }

    @Test
    public void shouldDetectScoringScheme()
            throws ValidateException, IOException, InterruptedException {
        Bowtie2 bowtie2 = new Bowtie2(genomeRelease, null, null,
                inFileU, outFile.getCanonicalPath(), new String[]{});
        bowtie2.validate();
        assertEquals(Bowtie2.ScoringScheme.Phred33, bowtie2.detectScoringScheme(inFile1));
    }

    @Test
    public void shouldConvertUnpairedToSAM()
            throws ValidateException, IOException, InterruptedException {
        Bowtie2 bowtie2 = new Bowtie2(genomeRelease, null, null,
                inFileU, outFile.getCanonicalPath(), new String[]{});
        bowtie2.validate();
        bowtie2.execute();

        assertTrue(outFile.exists());
    }

    @Test
    public void shouldConvertPairedToSAM()
            throws ValidateException, IOException, InterruptedException {
        Bowtie2 bowtie2 = new Bowtie2(genomeRelease, inFile1, inFile2,
                null, outFile.getCanonicalPath(), new String[]{});
        bowtie2.validate();
        bowtie2.execute();

        assertTrue(outFile.exists());
    }
}
