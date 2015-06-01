package database.test.unittests;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.containers.Genome;
import database.test.TestInitializer;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GenomeReleaseTableTests {
    public static TestInitializer ti;
    public static DatabaseAccessor dbac;
    public static FilePathGenerator fpg;

    private static File testFolder;
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setupWithoutAddingTuples();
        fpg = dbac.getFilePathGenerator();

        testFolderPath = TestInitializer.createScratchDir();
        testFolder = new File(testFolderPath);
        fpg.setRootDirectory(testFolderPath);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        recursiveDelete(testFolder);
    }

    @Before
    public void setUp() throws Exception {
        ti.addTuples();
    }

    @After
    public void tearDown() throws Exception {
        ti.removeTuplesKeepConnection();
    }

    @Test
    public void testGetDeleteGetAddGet() throws Exception {
        Genome g = dbac.getGenomeRelease("hg19");
        assertEquals("hg19", g.genomeVersion);

        dbac.removeGenomeRelease("hg19");
        g = dbac.getGenomeRelease("hg19");
        assertNull(g);

        dbac.addGenomeRelease("hg19", "Human", "hg19.txt", null);
        dbac.markReadyForDownload("hg19", "hg19.txt");
        g = dbac.getGenomeRelease("hg19");
        assertEquals("hg19", g.genomeVersion);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionWhenAddFileAlreadyExist()
    									throws Exception {

    	dbac.addGenomeRelease("test12", "Bear", "test12.txt", null);
        dbac.markReadyForDownload("test12", "test12.txt");
    	dbac.addGenomeRelease("test12", "Bear", "test12.txt", null);
    }

    @Test
    public void shouldReturnNullWhenGenomeReleaseDontExist()
    									throws Exception {

    	assertNull(dbac.getGenomeRelease("pangabanga"));

    }

    @Test
    public void shouldReturnRightNamesOfGenomeVersions() throws Exception {
        List<Genome> genomeList = dbac.getAllGenomeReleasesForSpecies("Human");

        assertEquals(3, genomeList.size());
        assertTrue(searchGenomeForVersion(genomeList, "hg38"));
        assertTrue(searchGenomeForVersion(genomeList, "hg19"));
        assertTrue(searchGenomeForVersion(genomeList, "hg18"));

    }

    @Test
    public void shouldReturnSpecificGenomeVersionFolderPath() throws Exception {

        Genome genome = dbac.getGenomeRelease("hg38");
        assertEquals("/var/www/data/genome_releases/Human/hg38/",
                genome.folderPath); // From add_test_tuples.sql
    }

    @Test
    public void shouldReturnUploadURLUponAdd() throws Exception {
        String uploadURL = dbac.addGenomeRelease("hg39", "Human", "hg39.fasta", null);
        String expectedUploadURL = ServerDependentValues.UploadURL
                + fpg.generateGenomeReleaseFolder("hg39", "Human")
                + "hg39.fasta";
        assertEquals(expectedUploadURL, uploadURL);
    }

    @Test (expected = IOException.class)
    public void shouldThrowIOExceptionWhenAttemptingToAddDuplicateFile() throws Exception {
        dbac.addGenomeRelease("hg38", "Human", "hg38.fasta", null);
    }

    @Test
    public void shouldUpdateDatabaseUponAdd() throws Exception {
        dbac.addGenomeRelease("hg40", "Human", "hg40.fasta", null);
        dbac.markReadyForDownload("hg40", "hg40.fasta");
        String expectedFolderPath = fpg.getGenomeReleaseFolderPath("hg40",
                "Human");
        Genome genome = dbac.getGenomeRelease("hg40");
        assertEquals(expectedFolderPath, genome.folderPath);
    }

    @Test
    public void shouldReturnFileName() throws Exception {
        dbac.addGenomeRelease("rn50", "Rat", "aRatFile.fasta", null);
        dbac.markReadyForDownload("rn50", "aRatFile.fasta");
        Genome genome = dbac.getGenomeRelease("rn50");

        assertEquals(1, genome.getFiles().size());
        assertEquals("aRatFile.fasta", genome.getFiles().get(0));
        assertEquals(genome.species, "Rat");
        assertEquals(genome.genomeVersion, "rn50");

    }

    @Test
    public void shouldDeleteFromBothDatabaseAndFileSystem() throws Exception {
        dbac.addGenomeRelease("hg41", "Human", "hg41.txt", null);
        dbac.markReadyForDownload("hg41", "hg41.txt");

        String genomeReleaseFolderPath = fpg.generateGenomeReleaseFolder(
                "hg41", "Human");
        addMockFile(genomeReleaseFolderPath, "hg41.txt");

        File genomeReleaseFolder = new File(genomeReleaseFolderPath);
        assertTrue(genomeReleaseFolder.exists());

        File genomeReleaseFile = new File(genomeReleaseFolderPath + "hg41.txt");
        assertTrue(genomeReleaseFile.exists());

        dbac.removeGenomeRelease("hg41");

        Genome g = dbac.getGenomeRelease("hg41");
        assertNull(g);

        assertFalse(genomeReleaseFolder.exists());
    }

    @Test(expected = IOException.class)
    public void shouldNotDeleteGenomeReleaseWhenFileNeedsIt() throws Exception{
    	dbac.removeGenomeRelease("hg38");
    }

    @Test
    public void shouldBeAbleToGetDownloadURLs() throws Exception {
        Genome g = dbac.getGenomeRelease("hg38");
        assertEquals(2, g.getFiles().size());
        String downloadURL = getDownloadURL(g, "hg38.fasta");
        assertEquals(ServerDependentValues.DownloadURL + g.folderPath + "hg38.fasta", downloadURL);
    }

    private String getDownloadURL(Genome g, String string) {
        for (String s: g.getDownloadURLs()) {
            if (s.endsWith(string)) {
                return s;
            }
        }
        return null;
    }

    @Test
    public void shouldBeAbleToGetAllSpeciesThatHaveAGenomeRelease() throws Exception {
        List<String> species = dbac.getAllGenomeReleaseSpecies();
        assertEquals(3, species.size());
    }

    @Test
    public void shouldBeAbleToGetAllGenomeReleases() throws Exception {
        List<Genome> genomes = dbac.getAllGenomeReleases();
        assertEquals(7, genomes.size());
    }

    @Test
    public void shouldGetFilePrefix() throws Exception {
        Genome g = dbac.getGenomeRelease("hg18");
        assertEquals("hg18", g.getFilePrefix());
    }

    @Test
    public void shouldGetFilePrefixForComplexFileNames() throws Exception {
        dbac.addGenomeRelease("rua888", "Superhero", "superheroRua888.ping.pong", null);
        dbac.markReadyForDownload("rua888", "superheroRua888.ping.pong");
        Genome g = dbac.getGenomeRelease("rua888");
        assertEquals("superheroRua888", g.getFilePrefix());
    }

    @Test
    public void shouldReturnEmptyListFromGetAllGenomeReleasesWhenTableIsEmpty() throws Exception{

    	tearDown();

    	assertEquals(0, dbac.getAllGenomeReleases().size());
    }
    @Test
    public void shouldRemoveGenomeReleaseFileInProgress() throws Exception{
        dbac.addInProgressGenomeRelease("testversion", "Testy", "testy.test", null);
        boolean removed = dbac.removeGenomeReleaseFile("testversion","testy.test");
        assertTrue(removed);
    }
    @Test
    public void shouldRemoveChainFileInProgress() throws Exception {
        dbac.addGenomeRelease("testFromVersion", "Ablibn", "from.test", null);
        dbac.addGenomeRelease("testToVersion", "Ablibn", "to.test", null);
        dbac.addInProgressChainFile("testFromVersion", "testToVersion","testChain.file",null);
        int removed = dbac.removeChainFiles("testFromVersion", "testToVersion");
        assertTrue(removed == 1);
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
