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

	@Test
	public void testAddingExperimentWithEmptyName() throws SQLException, IOException {
		dbac.addExperiment("");
		assertNull(dbac.getExperiment(""));
	}

	@Test
	public void testAddingDropDownWithEmptyLabel() throws SQLException, IOException {
		List<String> choices = new ArrayList<String>();
		choices.add("Choice1");
		choices.add("Choice2");

		dbac.addDropDownAnnotation("", choices, 0, true);
		assertNull(dbac.getAnnotationObject(""));
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

	@Test
	public void testAddUserWithEmptyName() throws SQLException {
		List<String> userList;

		dbac.addUser("", "1234", "Admin", "Bert Larsson", "sdsdfsfsdf");
		userList = dbac.getUsers();

		assertFalse(userList.contains(""));
	}

	@Test(expected = IOException.class)
	public void testAddUserWithEmptyPassword() throws SQLException {
		dbac.addUser("Herbert", "", "Admin", "Herbert Svensson", "sdsdfsfsdf");
	}

	@Test(expected = SQLException.class)
	public void testAddSeveralUsersWithSameName() throws SQLException {
		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
		dbac.addUser("Rune", "blabla", "Admin", "Rune Karlsson", "sdsdfsfsdf");
	}

	@Test(expected = IOException.class)
	public void testRemoveChainFilesWithEmptyVersions() throws SQLException {
		dbac.removeChainFile("", "");
	}

	@Test(expected = IOException.class)
	public void testRemoveChainFilesWithEmptyFromVersion() throws SQLException {
		dbac.removeChainFile("", "hej");
	}

	@Test(expected = IOException.class)
	public void testRemoveChainFilesWithEmptyToVersion() throws SQLException {
		dbac.removeChainFile("hej", "");
	}

	@Test(expected = IOException.class)
	public void testRemoveDropDownAnnotationValueFromAnnotationDoesntExist() throws SQLException, IOException {
		dbac.removeDropDownAnnotationValue("whaaat", "dfsdf");
	}

}
