package testSuite.unitTests;

import static org.junit.Assert.*;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.FilePathGenerator;

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

        recursiveDelete(testFolder);
    }

    @Test
    public void addChain_file() throws SQLException {

        String fromVersion = "hg18";
        String toVersion = "hg38";
        String fileName = "chainHuman";

        String filePath = dbac.addChainFile(fromVersion, toVersion, fileName);
        System.out.println(filePath);
        assertEquals(
                "http://scratchy.cs.umu.se:8000/upload.php?path="
                		+ testFolderPath
                		+ "chain_files/Human/"
                        + fromVersion
                        + " - "
                        + toVersion
                        + File.separator
                        + "chainHuman", filePath);
    }

    @Test
    public void shouldGetRightChainFilePath() throws Exception {
        String fromVersion = "hg19";
        String toVersion = "hg38";

        String filePath = dbac.getChainFile(fromVersion, toVersion);

        assertEquals("/var/www/data/chain_files/Human/hg19 - hg38/hg19ToHg38.over.chain", filePath); // From add_test_tuples.sql
    }

    @Test
    public void shouldReturnNullFilePathWhenChainFileIsNotInDB() throws Exception {
        String fromVersion = "hg99";
        String toVersion = "hg38";

        String filePath = dbac.getChainFile(fromVersion, toVersion);

        assertNull(filePath);
    }

    @Test
    public void removeChainFileFromDatabase() throws SQLException {
        String fromVersion = "hg19";
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

    private void addMockFile(String folderPath, String filename1)
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