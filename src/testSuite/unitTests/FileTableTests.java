package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;
import database.FileTuple;

public class FileTableTests {

    private static DatabaseAccessor dbac;

    private String testGenomePath;
    private String testPath;
    private String testName = "testFileName1";
    private String testInputFile = "testInputFile";
    private String testType = "testFileType4";
    private int testFileType = FileTuple.RAW;
    private String testAuthor = "testFileAuthor1";
    private String testUploader = "testUploader1";
    private String testMetaData = "testUploader1";
    private boolean testIsPrivate = false;
    private String testExpId = "testExpId2";
    private String testGRVersion = null;

    private String testFileName = "testFileName3";

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username,
        		TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        dbac.close();
    }

    @Before
    public void setup() throws SQLException, IOException {
        dbac.addExperiment(testExpId);
        testPath = dbac.addFile(testType, testName, testMetaData,
                testAuthor, testUploader, testIsPrivate, testExpId,
                testGRVersion);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteFile(testPath);
        dbac.deleteExperiment(testExpId);
    }

    @Test
    public void testGetDeleteGetAddGetFile() throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        assertEquals(testPath, e.getFiles().get(0).path);

        dbac.deleteFile(testPath);
        e = dbac.getExperiment(testExpId);
        assertEquals(0, e.getFiles().size());

        testPath = dbac.addFile(testType, testName, testMetaData,
                testAuthor, testUploader, testIsPrivate, testExpId,
                testGRVersion);
        e = dbac.getExperiment(testExpId);
        assertEquals(1, e.getFiles().size());
        assertEquals(testPath, e.getFiles().get(0).path);
    }

    @Test(expected = SQLException.class)
    public void shouldNotBeAbleToDeleteAnExperimentContainingAFile()
            throws Exception {

        try {
            dbac.deleteExperiment(testExpId);
        } catch (Exception e) {
            throw e;
        } finally {
            assertTrue(dbac.hasExperiment(testExpId));
        }
    }

    @Test
    public void addRemoveGenomeReleaseTest(){

    	ArrayList<String> genomeVersions;

    	try {
    		testGenomePath = dbac.addGenomeRelease("F2.3","Fly");

    		genomeVersions = dbac.getStoredGenomeVersions();
    		assertTrue(genomeVersions.contains("F2.3"));

    		boolean succeed = dbac.removeGenomeRelease("F2.3", "Fly");
    		assertTrue(succeed);

    	} catch (SQLException e) {
    		System.out.println("Failed to insert/remove a new" +
    								" Genome releaseVersion");
    		e.printStackTrace();
    		fail();
    	}
    }

    @Test
    public void getStoredGenomeVersionsTest(){

    	ArrayList<String> genomeVersions;

		try {
			dbac.addGenomeRelease("chemicalX", "cat");
			genomeVersions = dbac.getStoredGenomeVersions();
			assertTrue(genomeVersions.contains("chemicalX"));

			//then remove inserted genome_Release
			dbac.removeGenomeRelease("chemicalX", "cat");

		} catch (SQLException e) {
			System.out.println("Failed to get stored genome versions!");
			e.printStackTrace();
			fail();
		}
    }

    @Test
    public void shouldBeAbleToCheckIfFileExistsInDatabase() throws Exception {
    	ArrayList<Experiment> experiments =
    			(ArrayList<Experiment>) dbac.search(testPath + "[Path]");
    	Experiment experiment = experiments.get(0);
    	int fileID = experiment.getFiles().get(0).id;
		assertTrue(dbac.hasFile(fileID));
	}

    @Test
    public void shouldBeAbleToDeleteFileUsingFileID() throws Exception {
    	FileTuple ft = dbac.addNewFile(testExpId, testFileType,
    			testFileName, testInputFile, testMetaData, testAuthor,
    			testUploader, testIsPrivate, testGRVersion);
    	int fileID = ft.id;
    	assertEquals(1, dbac.deleteFile(fileID));
    	assertFalse(dbac.hasFile(fileID));

	}

    @Test
    public void shouldReturnZeroIfFileToBeDeletedDoesNotExistInDatabase() throws Exception {
    	if (dbac.hasFile(123)) {
    		fail("Use another ID for the test, this one" +
    				"exists in the database.");
    	}
    	assertEquals(0, dbac.deleteFile(123));
	}
}
