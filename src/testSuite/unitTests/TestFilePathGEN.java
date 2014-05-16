package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.FilePathGenerator;
import database.FileTuple;

public class TestFilePathGEN {

    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";

    private static FilePathGenerator fpg;
    private static File testFolder;

    private static String testFolderPath;

    @BeforeClass
    public static void setupClass() throws IOException {

        testFolderPath = System.getProperty("user.home") + File.separator
                + testFolderName + File.separator;

        testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        fpg = new FilePathGenerator(testFolder.toString() + File.separator);
    }

    @AfterClass
    public static void teardownClass() {
        recursiveDelete(testFolder);
    }

    @Before
    public void setup() {
        fpg.generateExperimentFolders("Exp1");
    }

    @After
    public void teardown() {
        recursiveDelete(testFolder);
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

    @Test
    public void shouldGenerateRightNumberOfExperimentFolders() throws Exception {

        testFolder = new File(testFolderPath);
        assertEquals(1, testFolder.listFiles().length);
        assertEquals(4, testFolder.listFiles()[0].listFiles().length);
    }

    @Test
    public void shouldGenerateFilePath() throws Exception {
        String expID = "expID";
        int fileType = FileTuple.RAW;
        String fileName = "fileName";
        String filePath = fpg.generateFilePath(expID, fileType, fileName);
        assertEquals(testFolder.toString() + File.separator + expID
                + File.separator + "raw" + File.separator + fileName, filePath);
    }

    @Test(expected = IOException.class)
    public void shouldThrowAnExceptionWhenSeperatorIsMissingFromHomeDir()
            throws Exception {
        new FilePathGenerator(testFolder.toString());
    }

    @Test
    public void shouldGenerateRightChainFolderPath() throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        String expectedFolderPath = testFolder.toString() + File.separator
                + "chain_files" + File.separator + species + File.separator
                + fromVersion + " - " + toVersion + File.separator;

        String chainFilePath = fpg.getChainFolderPath(species,
                fromVersion, toVersion);

        assertEquals(expectedFolderPath, chainFilePath);

    }

    @Test
    public void shouldGenerateRightGenomeReleaseFolderPath() throws Exception {
        String version = "hg13";
        String species = "Human";

        String expectedGenomeReleaseFolderPath = testFolder + File.separator + "genome_releases" + File.separator + species + File.separator + version + File.separator;
        String path = fpg.generateGenomeReleaseFolder(version, species);

        assertEquals(expectedGenomeReleaseFolderPath, path);

    }

    @Test
    public void shouldGenerateChainFileFolder() throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolder(species, fromVersion, toVersion);

        assertNotNull(searchForSubFolder(testFolder, "chain_files"));
    }

    @Test
    public void shouldGenerateSpeciesFolderForChainFiles() throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolder(species, fromVersion, toVersion);

        File chainFileFolder = searchForSubFolder(testFolder, "chain_files");
        assertNotNull(searchForSubFolder(chainFileFolder, species));
    }

    @Test
    public void shouldGenerateFromVersionToVersionFolderForChainFiles()
            throws Exception {
        String species = "Human";
        String fromVersion = "v1";
        String toVersion = "v2";

        fpg.generateChainFolder(species, fromVersion, toVersion);

        File chainFileFolder = searchForSubFolder(testFolder, "chain_files");
        File speciesFolder = searchForSubFolder(chainFileFolder, species);
        assertNotNull(searchForSubFolder(speciesFolder, fromVersion + " - "
                + toVersion));
    }

    private File searchForSubFolder(File folder, String name) {
        for (File f : folder.listFiles()) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        return null;
    }


}