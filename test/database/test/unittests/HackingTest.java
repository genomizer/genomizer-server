package database.test.unittests;

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
import database.test.TestInitializer;

/**
 * Test class for finding last minute bugs in subMethods classes!
 */
public class HackingTest {

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

    //ExperimentMethods

    @Test(expected = IOException.class)
	public void shouldNotAddExperimentWithEmptyName() throws SQLException,
			IOException {

		dbac.addExperiment("");
	}

    @Test(expected = IOException.class)
    public void shouldNotAnnotateWithNonExistentExperiment()
    		throws SQLException, IOException {

    	dbac.annotateExperiment("blaj", "sdfdfs", "dfdf");
    }

    @Test(expected = IOException.class)
    public void shouldNotAnnotateWithNonExistentAnnotationLabel()
    		throws SQLException, IOException {

    	dbac.addExperiment("xp2");
    	dbac.annotateExperiment("xp2", "sdfdfs", "dfdf");
    }

    //AnnotationMethods

    @Test(expected = IOException.class)
	public void shouldNotAddDropDownWithEmptyLabel() throws SQLException,
			IOException {

    	List<String> choices = new ArrayList<String>();
		choices.add("Choice1");
		choices.add("Choice2");

		dbac.addDropDownAnnotation("", choices, 0, true);
	}

	@Test(expected = IOException.class)
	public void shouldNotAddDropDownAnnotationWithEmptyValue()
			throws SQLException, IOException {

		List<String> choices = new ArrayList<String>();
		choices.add("Choice1");

		dbac.addDropDownAnnotation("anno", choices, 0, true);
		dbac.addDropDownAnnotationValue("anno", "");
	}

	@Test(expected = IOException.class)
	public void shouldNotAddFreeTextAnnotationWithEmptyLabel()
			throws SQLException, IOException {

		dbac.addFreeTextAnnotation("", "hej", true);
	}

	@Test(expected = IOException.class)
	public void shouldNotAddingFreeTextAnnotationWithEmptyValue()
			throws SQLException, IOException {

		dbac.addFreeTextAnnotation("anno2", "", true);
	}

	@Test(expected = IOException.class)
	public void shouldNotChangeAnnotationLabelToEmptyLabel()
			throws SQLException, IOException {

		dbac.changeAnnotationLabel("", "value");
	}

	//UserMethods

	@Test(expected = IOException.class)
	public void shouldNotAddUserWithEmptyName()
			throws SQLException, IOException {

		dbac.addUser("", "1234", "Admin", "Bert Larsson", "sdsdfsfsdf");
	}

	@Test(expected = IOException.class)
	public void shouldNotAddUserWithEmptyPassword()
			throws SQLException, IOException {

		dbac.addUser("Herbert", "", "Admin", "Herbert Svensson", "sdsdfsfsdf");
	}

	@Test(expected = SQLException.class)
	public void shouldNotAddSeveralUsersWithSameName()
			throws SQLException, IOException {

		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
	}

	@Test(expected = IOException.class)
	public void shouldNotChangeToEmptyPassword()
			throws SQLException, IOException {

		dbac.addUser("Rolf", "1234", "Admin", "Rolf Persson", "dffddf@mail.com");
		dbac.resetPassword("Rolf", "");
	}
}