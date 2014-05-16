package testSuite.unitTests;

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

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;
import database.FilePathGenerator;

public class ProcessRawToProfileTests {

    private static DatabaseAccessor dbac;
    private static FilePathGenerator fpg;
    private static TestInitializer ti;
    private static String testFolderName = "Genomizer Test Folder - Dont be afraid to delete me";
    private static String testFolderPath;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();

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
    }

    @After
    public void tearDown() throws Exception {
        ti.recursiveDelete(new File(testFolderPath));
    }

    @Test
    public void shouldGenerateRightFolderPaths() throws Exception {

        fpg.generateExperimentFolders("Exp1");

        Entry<String, String> folderPaths = dbac
                .processRawToProfile("Exp1");

        String expectedRawFolderPath = "/var/www/data/Exp1/raw/"; // From
                                                                  // add_test_tuples.sql
        String expectedProfileFolderPath = testFolderPath
                + "Exp1/profile/0/";

        assertEquals(expectedRawFolderPath, folderPaths.getKey());
        assertEquals(expectedProfileFolderPath,
                folderPaths.getValue());
    }

    @Test
    public void shouldBeAbleToAddProcessedProfiles() throws Exception {

        fpg.generateExperimentFolders("Exp1");

        Entry<String, String> folderPaths = dbac
                .processRawToProfile("Exp1");

        addMockFiles(folderPaths.getValue(), "prof1.sam",
                "prof2.sam", "input.sam");

        dbac.addGeneratedProfiles("Exp1", folderPaths.getValue(),
                "input.sam", "-n1 --best", "hg38", "Ruaridh", true);
        
        List<Experiment> experiments = dbac.search("Exp1[ExpID] AND prof1.sam[FileName]");
        assertEquals(1, experiments.size());
        assertEquals(1, experiments.get(0).getFiles().size());
        
        experiments = dbac.search("Exp1[ExpID] AND prof2.sam[FileName]");
        assertEquals(1, experiments.get(0).getFiles().size());
        
        experiments = dbac.search("Exp1[ExpID] AND input.sam[FileName]");
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
