package database.testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;
import database.FilePathGenerator;
import database.FileTuple;
import database.testSuite.TestInitializer;

public class ProcessRawToProfileTests {

    private static DatabaseAccessor dbac;
    private static FilePathGenerator fpg;
    private static TestInitializer ti;
    private static String testFolderName =
    		"Genomizer Test Folder - Dont be afraid to delete me";
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        ti = new TestInitializer();
        dbac = ti.setupWithoutAddingTuples();

        testFolderPath = System.getProperty("user.home")
                + File.separator + testFolderName + File.separator;

        fpg = dbac.getFilePathGenerator();
        fpg.setRootDirectory(testFolderPath);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

        ti.removeTuples();
    }

    @Before
    public void setUp() throws Exception {

        ti.addTuples();
    }

    @After
    public void tearDown() throws Exception {

        ti.recursiveDelete(new File(testFolderPath));
        ti.removeTuplesKeepConnection();
    }

    @Test
    public void shouldGenerateRightFolderPaths() throws Exception {

        fpg.generateExperimentFolders("Exp1");

        Entry<String, String> folderPaths = dbac.processRawToProfile("Exp1");

        String expectedRawFolderPath = "/var/www/data/Exp1/raw/";

        // From add_test_tuples.sql
        String expectedProfileFolderPath = testFolderPath
                + "Exp1/profile/0/";

        assertEquals(expectedRawFolderPath, folderPaths.getKey());
        assertEquals(expectedProfileFolderPath,
                folderPaths.getValue());
    }
/*
    private FileTuple searchFileTuples(String string, List<FileTuple> fts) {
        for (FileTuple ft: fts) {
            if (ft.filename.equals(string)) {
                return ft;
            }
        }
        return null;
    }
*/
    @Test
    public void shouldBeAbleToAddProcessedProfiles() throws Exception {

        fpg.generateExperimentFolders("Exp1");
        Entry<String, String> folderPaths = dbac.processRawToProfile("Exp1");

        addMockFiles(folderPaths.getValue(), "prof1.sam",
                "prof2.sam", "input.sam");

        dbac.addGeneratedProfiles("Exp1", folderPaths.getValue(), "input.sam",
        		"-n1", "hg38", "Ruaridh", false);

        List<Experiment> experiments =
        		dbac.search("Exp1[ExpID] AND prof1.sam[FileName]");
        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());

        FileTuple ft = experiments.get(0).getFiles().get(0);
        System.out.println(ft.toString());

        experiments = dbac.search("Exp1[ExpID] AND prof2.sam[FileName]");
        assertEquals(1, experiments.get(0).getFiles().size());

        experiments = dbac.search("Exp1[ExpID] AND input.sam[FileName]");
        assertEquals(0, experiments.size());

        experiments = dbac.search("Exp1[ExpID] AND processing...[FileName]");
        assertEquals(0, experiments.size());
    }

    private void addMockFiles(String folderPath, String filename1,
            String filename2, String filename3) throws IOException {

        File file1 = new File(folderPath + File.separator + filename1);
        file1.createNewFile();

        File file2 = new File(folderPath + File.separator + filename2);
        file2.createNewFile();

        File file3 = new File(folderPath + File.separator + filename3);
        file3.createNewFile();

    }

}