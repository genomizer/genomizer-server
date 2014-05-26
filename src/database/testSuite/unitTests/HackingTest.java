package database.testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.FilePathGenerator;
import database.testSuite.TestInitializer;

public class HackingTest {

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

    //Experiment

    @Test(expected = IOException.class)
	public void testAddingExperimentWithEmptyName() throws SQLException, IOException {
		dbac.addExperiment("");
	}

    @Test(expected = IOException.class)
    public void testAnnotatWithNonExistentExperiment() throws SQLException, IOException {
    	dbac.annotateExperiment("blaj", "sdfdfs", "dfdf");
    }

    @Test(expected = IOException.class)
    public void testAnnotatWithNonExistentAnnotationLabel() throws SQLException, IOException {
    	dbac.addExperiment("xp2");
    	dbac.annotateExperiment("xp2", "sdfdfs", "dfdf");
    }

    //Annotation

    @Test(expected = IOException.class)
	public void testAddingDropDownWithEmptyLabel() throws SQLException, IOException {
		List<String> choices = new ArrayList<String>();
		choices.add("Choice1");
		choices.add("Choice2");

		dbac.addDropDownAnnotation("", choices, 0, true);
	}

	@Test(expected = IOException.class)
	public void testAddingDropDownAnnotationWithEmptyValue() throws SQLException, IOException {
		List<String> choices = new ArrayList<String>();
		choices.add("Choice1");

		dbac.addDropDownAnnotation("anno", choices, 0, true);
		dbac.addDropDownAnnotationValue("anno", "");
	}

	@Test(expected = IOException.class)
	public void testAddingFreeTextAnnotationWithEmptyLabel() throws SQLException, IOException {
		dbac.addFreeTextAnnotation("", "hej", true);
	}

	@Test(expected = IOException.class)
	public void testAddingFreeTextAnnotationWithEmptyValue() throws SQLException, IOException {
		dbac.addFreeTextAnnotation("anno2", "", true);
	}

	@Test(expected = IOException.class)
	public void testChangeAnnotationLabelToEmptyLabel() throws SQLException, IOException {
		dbac.changeAnnotationLabel("", "value");
	}

	//User

	@Test(expected = IOException.class)
	public void testAddUserWithEmptyName() throws SQLException, IOException {
		dbac.addUser("", "1234", "Admin", "Bert Larsson", "sdsdfsfsdf");
	}

	@Test(expected = IOException.class)
	public void testAddUserWithEmptyPassword() throws SQLException, IOException {
		dbac.addUser("Herbert", "", "Admin", "Herbert Svensson", "sdsdfsfsdf");
	}

	@Test(expected = SQLException.class)
	public void testAddSeveralUsersWithSameName() throws SQLException, IOException {
		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
	}

	@Test(expected = IOException.class)
	public void testChangeToEmptyPassword() throws SQLException, IOException {
		dbac.addUser("Rolf", "1234", "Admin", "Rolf Persson", "dffddf@mail.com");
		dbac.resetPassword("Rolf", "");
	}

	// Add tests for remove files if theye are in use or not
}
