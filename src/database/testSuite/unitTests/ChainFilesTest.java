package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.containers.ChainFile;
import database.testSuite.TestInitializer;

public class ChainFilesTest {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    private static String testFolderName =
    		"Genomizer Test Folder - Dont be afraid to delete me";
    private static File testFolder;
    private static String testFolderPath;
    private static FilePathGenerator fpg;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {

        ti = new TestInitializer();
        dbac = ti.setup();

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

        ti.removeTuples();
        ti.recursiveDelete(testFolder);
    }

    @Test
    public void addChain_file() throws SQLException {

        String fromVersion = "hg19";
        String toVersion = "hg38";
        String fileName = "chainHuman";
        String filePath = dbac.addChainFile(fromVersion, toVersion, fileName);

        assertEquals(ServerDependentValues.UploadURL +
        		fpg.getChainFolderPath("Human", fromVersion, toVersion)
        		, filePath);
    }

    @Test
    public void shouldGetRightChainFilePath() throws Exception {

        String fromVersion = "hg38";
        String toVersion = "hg18";

        ChainFile cf = dbac.getChainFile(fromVersion, toVersion);
        String filePath = cf.folderPath;

        assertEquals("/var/www/data/chain_files/Human/hg38 - hg18/", filePath);
    }

    @Test
    public void shouldReturnNullFilePathWhenChainFileIsNotInDB()
    		throws Exception {

        String fromVersion = "hg99";
        String toVersion = "hg38";
        ChainFile cf = dbac.getChainFile(fromVersion, toVersion);

        assertNull(cf);
    }

    @Test
    public void shouldRemoveChainFileFromDatabase() throws SQLException {

        String fromVersion = "hg18";
        String toVersion = "hg38";

        assertEquals(1, dbac.removeChainFile(fromVersion, toVersion));
        assertNull(dbac.getChainFile(fromVersion, toVersion));
    }

    @Test
    public void shouldRemoveChainFilesFromDatabaseAndFileSystem()
    		throws Exception {

        dbac.addChainFile("rn3", "rn5", "rat.over.chain");

        String folderPath = fpg.generateChainFolder("Rat", "rn3", "rn5");
        File folder = new File(folderPath);
        assertTrue(folder.exists());

        addMockFile(folderPath, "rat.over.chain");
        File mockFile = new File(folderPath + "rat.over.chain");
        assertTrue(mockFile.exists());

        dbac.removeChainFile("rn3", "rn5");
        assertNull(dbac.getChainFile("rn3", "rn5"));
        assertFalse(mockFile.exists());
        assertFalse(folder.exists());
    }

    @Test
    public void chainFileObjectShouldContainExpectedValues() throws Exception {

        String fromVersion = "rn3";
        String toVersion = "rn5";
        String testName1 = "testName1";
        String testName2 = "testName2";

        dbac.addChainFile(fromVersion, toVersion, testName1);
        dbac.addChainFile(fromVersion, toVersion, testName2);
        ChainFile cf = dbac.getChainFile(fromVersion, toVersion);

        assertEquals(fromVersion, cf.fromVersion);
        assertEquals(toVersion, cf.toVersion);
        assertEquals(testFolderPath + "chain_files/Rat/" + fromVersion +
        		" - " + toVersion + "/", cf.folderPath);
        assertEquals(2, cf.getFilesWithStatus().size());
        assertTrue(cf.getFilesWithStatus().containsKey(testName1));
        assertTrue(cf.getFilesWithStatus().containsKey(testName2));
	}

    @Test
    public void shouldBeAbleToAddMultipleChainFileFiles() throws Exception {

        String fromVersion = "rn4";
        String toVersion = "rn5";
        String testName1 = "testName1";
        String testName2 = "testName2";
        String testName3 = "testName3";

		dbac.addChainFile(fromVersion, toVersion, testName1);
		dbac.addChainFile(fromVersion, toVersion, testName2);
		dbac.addChainFile(fromVersion, toVersion, testName3);

		ChainFile cf = dbac.getChainFile(fromVersion, toVersion);
		HashMap<String, String> files =
				(HashMap<String, String>) cf.getFilesWithStatus();

		assertTrue(files.containsKey(testName1));
		assertTrue(files.containsKey(testName2));
		assertTrue(files.containsKey(testName3));
	}

    private void addMockFile(String folderPath, String filename1)
            throws IOException {

        File file1 = new File(folderPath + filename1);
        file1.createNewFile();
    }
}