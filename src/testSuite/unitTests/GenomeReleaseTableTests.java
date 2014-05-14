package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.ServerDependentValues;

public class GenomeReleaseTableTests {
    public static TestInitializer ti;
    public static DatabaseAccessor dbac;
    public static FilePathGenerator fpg;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
        fpg = dbac.getFilePathGenerator();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        ti.removeTuples();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldReturnRightNumberOfGenomeVersion() throws Exception {
        List<String> versions = dbac.getAllGenomReleases("Human");
        assertEquals(3, versions.size());
    }

    @Test
    public void shouldReturnRightNamesOfGenomeVersions() throws Exception {
        List<String> versions = dbac.getAllGenomReleases("Human");
        assertTrue(versions.contains("hg38"));
        assertTrue(versions.contains("hg19"));
        assertTrue(versions.contains("hg18"));
    }
    
    @Test
    public void shouldReturnSpecificGenomeVersionFilePath() throws Exception {
        String path = dbac.getGenomeReleaseFilePath("hg38");
        assertEquals("/var/www/data/GenomeRelease/Human/hg38.fasta", path);
    }
    
    @Test
    public void shouldReturnUploadURLUponAdd() throws Exception {
        String uploadURL = dbac.addGenomeRelease("hg39", "Human", "hg39.fasta");
        String expectedUploadURL = ServerDependentValues.UploadURL + fpg.generateGenomeReleaseFolder("hg39", "Human") + "hg39.fasta";
        assertEquals(expectedUploadURL, uploadURL);
    }
    
    @Test
    public void shouldUpdateDatabaseUponAdd() throws Exception {
        dbac.addGenomeRelease("hg40", "Human", "hg40.fasta");
        String expectedFilePath = fpg.generateGenomeReleaseFolder("hg40", "Human") + "hg40.fasta";
        String genomeReleaseFilePath = dbac.getGenomeReleaseFilePath("hg40");
        assertEquals(expectedFilePath, genomeReleaseFilePath);
    }
    
    @Test
    public void shouldReturnNullForInvalidVersionOponGet() throws Exception {
        String genomeReleaseFilePath = dbac.getGenomeReleaseFilePath("hg50");
        assertNull(genomeReleaseFilePath);
    }

}
