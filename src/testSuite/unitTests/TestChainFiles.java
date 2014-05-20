package testSuite.unitTests;

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

import testSuite.TestInitializer;
import database.ChainFile;
import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.ServerDependentValues;

public class TestChainFiles {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";
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
        assertEquals(
                ServerDependentValues.UploadURL
                		+ fpg.getChainFolderPath("Human", fromVersion, toVersion)
                        , filePath);
    }

    @Test
    public void shouldGetRightChainFilePath() throws Exception {
        String fromVersion = "hg18";
        String toVersion = "hg38";

        ChainFile cf = dbac.getChainFile(fromVersion, toVersion);
        String filePath = cf.folderPath;

        assertEquals("/var/www/data/chain_files/Human/hg18 - hg38/", filePath); // From add_test_tuples.sql
    }

    @Test
    public void shouldReturnNullFilePathWhenChainFileIsNotInDB() throws Exception {
        String fromVersion = "hg99";
        String toVersion = "hg38";

        ChainFile cf = dbac.getChainFile(fromVersion, toVersion);

        assertNull(cf);
    }

    @Test
    public void removeChainFileFromDatabase() throws SQLException {
        String fromVersion = "hg18";
        String toVersion = "hg38";

        assertEquals(1, dbac.removeChainFile(fromVersion, toVersion));
        assertNull(dbac.getChainFile(fromVersion, toVersion));

    }

    @Test
    public void shouldRemoveChainFilesFromDatabaseAndFileSystem() throws Exception {

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
		HashMap<String, String> files = (HashMap<String, String>) cf.getFilesWithStatus();

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