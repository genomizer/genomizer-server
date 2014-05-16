package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.util.PSQLException;

import testSuite.TestInitializer;

import database.DatabaseAccessor;
import database.Experiment;
import database.FilePathGenerator;
import database.FileTuple;

public class ExperimentTests {

    private static DatabaseAccessor dbac;

    private static String testExpId = "testExpId1";
    private static String testExpId2 = "testExpId2";

    private static String testLabelFT = "testLabelFT1";
    private static String testValueFT = "testValueFT1";
    private static String testLabelDD = "testLabelDD1";
    private static String testChoice = "testchoice";
	private static String newLabel = "Tis";					//for changeLabel
    private static List<String> testChoices;

    private static FileTuple ft;
    private static String testName = "testFileName1";
    private static String testInputFile = "testInputFile";
    private static int testFileType = FileTuple.RAW;
    private static String testAuthor = "testFileAuthor1";
    private static String testUploader = "testUploader1";
    private static String testMetaData = "testMetaData";
    private static boolean testIsPrivate = false;
    private static String testGRVersion = null;

    private static FilePathGenerator fpg;
    private static String testFolderPath;
    private static File testFolder;
    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";

    @BeforeClass
    public static void setupTestCase() throws Exception {
        dbac = new DatabaseAccessor(TestInitializer.username, TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);
        testChoices = new ArrayList<String>();
        testChoices.add(testChoice);
        testChoices.add(testChoice + "2");

        testFolderPath = System.getProperty("user.home") + File.separator
                + testFolderName + File.separator;

        testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        fpg = dbac.getFilePathGenerator();
        fpg.setRootDirectory(testFolderPath);
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        dbac.deleteFile(ft.id);
        dbac.deleteExperiment(testExpId2);
        dbac.close();
        recursiveDelete(testFolder);
    }

    @Before
    public void setup() throws SQLException, IOException {
       dbac.addExperiment(testExpId);
       dbac.addFreeTextAnnotation(testLabelFT, null, true);
       dbac.addDropDownAnnotation(testLabelDD, testChoices, 0, false);
    }

    @After
    public void teardown() throws SQLException {
        dbac.deleteExperiment(testExpId);
        dbac.deleteAnnotation(testLabelFT);
        dbac.deleteAnnotation(testLabelDD);
        dbac.deleteAnnotation(newLabel);
    }

    @Test
    public void shouldBeAbleToConnectToDB() throws Exception {
        assertTrue(dbac.isConnected());
    }

    @Test
    public void testGetDeleteGetAddExperiment() throws Exception {

        assertTrue(dbac.hasExperiment(testExpId));
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());

        dbac.deleteExperiment(testExpId);
        assertFalse(dbac.hasExperiment(testExpId));
        e = dbac.getExperiment(testExpId);
        assertNull(e);

        dbac.addExperiment(testExpId);
        assertTrue(dbac.hasExperiment(testExpId));
        e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());
    }

    @Test
    public void shouldBeAbleToAnnotateExperimentFreeText()
            throws Exception {

        int res = dbac.annotateExperiment(testExpId,
                testLabelFT, testValueFT);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testValueFT, e.getAnnotations().get(testLabelFT));

    }

    @Test
    public void shouldBeAbleToDeleteExperimentAnnotationFreeText()
            throws Exception {

        dbac.annotateExperiment(testExpId, testLabelFT,
                testValueFT);

        int res = dbac.removeExperimentAnnotation(testExpId,
                testLabelFT);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertFalse(e.getAnnotations().containsKey(testLabelFT));
    }

    @Test
    public void shouldBeAbleToTagExperimentDropDown()
            throws Exception {

        int res = dbac.annotateExperiment(testExpId,
                testLabelDD, testChoice);
        assertEquals(1, res);
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testChoice, e.getAnnotations().get(testLabelDD));
    }

    @Test(expected = IOException.class)
    public void shouldNotBeAbleToTagExperimentWithInvalidDropdownChoice()
            throws Exception {

        String invalidValue = testChoice + "_invalid";
        try {
            dbac.annotateExperiment(testExpId, testLabelDD, invalidValue);
        } catch (Exception e) {
            throw e;
        } finally {
            Experiment e = dbac.getExperiment(testExpId);
            assertFalse(e.getAnnotations().containsKey(testLabelDD));
        }
    }

    @Test
    public void shouldBeAbleToSearchUsingExperimentID()
            throws Exception {
        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(testExpId, e.getID());
    }

    @Test
    public void shouldReturnExperimentObjectContainingAnnotationOnSearch()
            throws Exception {

        Experiment e = dbac.getExperiment(testExpId);
        assertFalse(e.getAnnotations().containsKey(testLabelFT));
        dbac.annotateExperiment(testExpId, testLabelFT, testValueFT);
        e = dbac.getExperiment(testExpId);
        assertTrue(e.getAnnotations().containsKey(testLabelFT));
    }

    @Test
    public void shouldReturnExperimentObjectContainingMultipleAnnotationsOnSearch()
            throws Exception {

        dbac.annotateExperiment(testExpId, testLabelDD, testChoice);

        dbac.annotateExperiment(testExpId, testLabelFT, testValueFT);

        Experiment e = dbac.getExperiment(testExpId);
        assertEquals(2, e.getAnnotations().size());
        assertTrue(e.getAnnotations().containsKey(testLabelFT));
        assertTrue(e.getAnnotations().containsKey(testLabelDD));

    }

    @Test
    public void getAllAnnotationLabelsTest(){

    	ArrayList<String> allAnnotationlabels = dbac.getAllAnnotationLabels();

    	/*should be equal to 6 iff 4 entries in database,
    	  and 2 adds in beginning every testrun.*/
        assertEquals(2, allAnnotationlabels.size());

    	//a bit hardcoded, works if database contains this values before.
    	assertTrue(allAnnotationlabels.contains(testLabelDD));
    	assertTrue(allAnnotationlabels.contains(testLabelFT));
    }

    @Test
    public void changeFromOldLabelToNewLabelTest()
    		throws Exception{

    	ArrayList<String> allLabelsBefore = dbac.getAllAnnotationLabels();

    	if(allLabelsBefore.contains(testLabelFT)){

    		int res = dbac.changeAnnotationLabel(testLabelFT, newLabel);

    		assertTrue(res > 0);

    		ArrayList<String> allLabelsAfter = dbac.getAllAnnotationLabels();
    		assertFalse(allLabelsAfter.contains(testLabelFT));
    		assertTrue(allLabelsAfter.contains(newLabel));
    	}else{
    		System.out.println("The old label did not exist in database!");
    		fail();
    	}
    }

    @Test(expected = SQLException.class)
    public void shouldNotDeleteDirectoryContainingFile() throws Exception {
    	dbac.addExperiment(testExpId2);
		ft = dbac.addNewFile(testExpId2, testFileType, testName, testInputFile,
		  		testMetaData, testAuthor, testUploader, testIsPrivate,
		  		testGRVersion);
    	fpg.generateExperimentFolders(testExpId2);
		addMockFile(ft.getParentFolder(), testName);
		dbac.deleteExperiment(testExpId2);
	}
    
    @Test
    public void shouldDeleteDirectories() throws Exception {
		fpg.generateExperimentFolders(testExpId);
		File dir = new File(testFolderPath + testExpId);
		assertTrue(dir.exists());
		dbac.deleteExperiment(testExpId);
		assertFalse(dir.exists());
	}

    private void addMockFile(String folderPath, String filename1) throws IOException {
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
