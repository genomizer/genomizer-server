package database.test.unittests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import database.containers.ChainFiles;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.constants.ServerDependentValues;
import database.test.TestInitializer;

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
    public void addChain_file() throws Exception {

        String fromVersion = "hg19";
        String toVersion = "hg38";
        String fileName = "chainHuman.txt";
        String filePath = dbac.addChainFile(fromVersion, toVersion, fileName, null);
        dbac.markReadyForDownload(fromVersion, toVersion, fileName);

        assertEquals(ServerDependentValues.UploadURL +
        		fpg.getChainFolderPath("Human", fromVersion, toVersion)
        		, filePath);
    }

    @Test
    public void shouldGetRightChainFilePath() throws Exception {

        String fromVersion = "hg38";
        String toVersion = "hg18";

        ChainFiles cf = dbac.getChainFiles(fromVersion, toVersion);
        String filePath = cf.folderPath;

        assertEquals("/var/www/data/chain_files/Human/hg38 - hg18/", filePath);
    }

    @Test
    public void shouldReturnNullFilePathWhenChainFileIsNotInDB()
    		throws Exception {

        String fromVersion = "hg99";
        String toVersion = "hg38";
        ChainFiles cf = dbac.getChainFiles(fromVersion, toVersion);

        assertNull(cf);
    }

    @Test
    public void shouldRemoveChainFileFromDatabase() throws SQLException {

        String fromVersion = "hg18";
        String toVersion = "hg38";

        assertEquals(1, dbac.removeChainFiles(fromVersion, toVersion));
        assertNull(dbac.getChainFiles(fromVersion, toVersion));
    }

    @Test
    public void shouldRemoveChainFilesFromDatabaseAndFileSystem()
    		throws Exception {

        dbac.addChainFile("rn3", "rn5", "rat.over.chain", null);
        dbac.markReadyForDownload("rn3", "rn5", "rat.over.chain");

        String folderPath = fpg.generateChainFolder("Rat", "rn3", "rn5");
        File folder = new File(folderPath);
        assertTrue(folder.exists());

        addMockFile(folderPath, "rat.over.chain");
        File mockFile = new File(folderPath + "rat.over.chain");
        assertTrue(mockFile.exists());

        dbac.removeChainFiles("rn3", "rn5");
        assertNull(dbac.getChainFiles("rn3", "rn5"));
        assertFalse(mockFile.exists());
        assertFalse(folder.exists());
    }

    @Test
    public void chainFileObjectShouldContainExpectedValues() throws Exception {

        String fromVersion = "rn3";
        String toVersion = "rn5";
        String testName1 = "testName1.txt";
        String testName2 = "testName2.txt";

        dbac.addChainFile(fromVersion, toVersion, testName1, null);
        dbac.markReadyForDownload(fromVersion, toVersion, testName1);
        dbac.addChainFile(fromVersion, toVersion, testName2, null);
        dbac.markReadyForDownload(fromVersion, toVersion, testName2);
        ChainFiles cf = dbac.getChainFiles(fromVersion, toVersion);

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
        String testName1 = "testName1.txt";
        String testName2 = "testName2.txt";
        String testName3 = "testName3.txt";

		dbac.addChainFile(fromVersion, toVersion, testName1, null);
        dbac.markReadyForDownload(fromVersion, toVersion, testName1);
		dbac.addChainFile(fromVersion, toVersion, testName2, null);
        dbac.markReadyForDownload(fromVersion, toVersion, testName2);
		dbac.addChainFile(fromVersion, toVersion, testName3, null);
        dbac.markReadyForDownload(fromVersion, toVersion, testName3);

		ChainFiles cf = dbac.getChainFiles(fromVersion, toVersion);
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