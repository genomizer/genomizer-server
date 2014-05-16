package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.Genome;
import database.ServerDependentValues;

public class GenomeReleaseTableTests {
    public static TestInitializer ti;
    public static DatabaseAccessor dbac;
    public static FilePathGenerator fpg;

    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";
    private static File testFolder;
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
        fpg = dbac.getFilePathGenerator();

        testFolderPath = System.getProperty("user.home") + File.separator
                + testFolderName + File.separator;

        testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        fpg.setRootDirectory(testFolderPath);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ti.removeTuples();
        recursiveDelete(testFolder);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnRightNamesOfGenomeVersions() throws Exception {
        List<Genome> genomeList = dbac.getAllGenomReleasesForSpecies("Human");

        assertTrue(searchGenomeForVersion(genomeList, "hg38"));
        assertTrue(searchGenomeForVersion(genomeList, "hg19"));
        assertTrue(searchGenomeForVersion(genomeList, "hg18"));

    }

    @Test
    public void shouldReturnSpecificGenomeVersionFilePath() throws Exception {
        Genome genome = dbac.getGenomeRelease("hg38");
        assertEquals("/var/www/data/GenomeRelease/Human/hg38.fasta",
                genome.path); // From add_test_tuples.sql
    }

    @Test
    public void shouldReturnUploadURLUponAdd() throws Exception {
        String uploadURL = dbac.addGenomeRelease("hg39", "Human", "hg39.fasta");
        String expectedUploadURL = ServerDependentValues.UploadURL
                + fpg.generateGenomeReleaseFolder("hg39", "Human")
                + "hg39.fasta";
        assertEquals(expectedUploadURL, uploadURL);
    }

    @Test
    public void shouldUpdateDatabaseUponAdd() throws Exception {
        dbac.addGenomeRelease("hg40", "Human", "hg40.fasta");
        String expectedFilePath = fpg.generateGenomeReleaseFolder("hg40",
                "Human") + "hg40.fasta";
        Genome genome = dbac.getGenomeRelease("hg40");
        assertEquals(expectedFilePath, genome.path);
    }

    @Test
    public void shouldReturnNullForInvalidVersionOponGet() throws Exception {
        Genome genome = dbac.getGenomeRelease("hg50");
        assertNull(genome);
    }

    @Test
    public void shouldReturnFileName() throws Exception {
        dbac.addGenomeRelease("rn50", "Rat", "aRatFile.fasta");
        Genome genome = dbac.getGenomeRelease("rn50");
        assertEquals(genome.fileName, "aRatFile.fasta");
        assertEquals(genome.specie, "Rat");
        assertEquals(genome.genomeVersion, "rn50");

    }

    @Test
    public void shouldDeleteFromBothDatabaseAndFileSystem() throws Exception {
        dbac.addGenomeRelease("hg41", "Human", "hg41.txt");

        String genomeReleaseFolderPath = fpg.generateGenomeReleaseFolder(
                "hg41", "Human");
        addMockFile(genomeReleaseFolderPath, "hg41.txt");

        File genomeReleaseFolder = new File(genomeReleaseFolderPath);
        assertTrue(genomeReleaseFolder.exists());

        File genomeReleaseFile = new File(genomeReleaseFolderPath + "hg41.txt");
        assertTrue(genomeReleaseFile.exists());

        dbac.removeGenomeRelease("hg41", "Human");

        Genome g = dbac.getGenomeRelease("hg41");
        assertNull(g);

        assertFalse(genomeReleaseFolder.exists());
    }

    private boolean searchGenomeForVersion(List<Genome> genomeList,
            String version) {

        for (int i = 0; i < genomeList.size(); i++) {
            if (genomeList.get(i).genomeVersion.equals(version)) {
                return true;
            }
        }

        return false;
    }

    private static void addMockFile(String folderPath, String filename1)
            throws IOException {
        File file1 = new File(folderPath + filename1);
        file1.createNewFile();
    }

    private static void recursiveDelete(File folder) {
        File[] contents = folder.listFiles();
        if (contents == null || contents.length == 0) {
            folder.delete();
        } else {
            for (File f : contents) {
                recursiveDelete(f);
            }
        }
        folder.delete();
    }
}
