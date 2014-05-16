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
import database.Genome;
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
        List<Genome> versions = dbac.getAllGenomReleasesForSpecies("Human");
        assertEquals(3, versions.size());
    }

    @Test
    public void shouldReturnRightNamesOfGenomeVersions() throws Exception {
        List<Genome> genomeList = dbac.getAllGenomReleasesForSpecies("Human");

        assertTrue(searchGenomeForVersion(genomeList, "hg38"));
        assertTrue(searchGenomeForVersion(genomeList, "hg19"));
        assertTrue(searchGenomeForVersion(genomeList, "hg18"));

    }

    private boolean searchGenomeForVersion(List<Genome> genomeList, String version) {

        for (int i=0;i<genomeList.size();i++){
        	if (genomeList.get(i).genomeVersion.equals(version)){
        		return true;
        	}
        }

    	return false;
    }

    @Test
    public void shouldReturnSpecificGenomeVersionFilePath() throws Exception {
        Genome genome = dbac.getGenomeRelease("Hg38");
        assertEquals("/var/www/data/GenomeRelease/Human/hg38.fasta", genome.path);
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
    	assertEquals(genome.fileName,"aRatFile.fasta");
    	assertEquals(genome.specie,"Rat");
    	assertEquals(genome.genomeVersion,"rn50");

    }
}
