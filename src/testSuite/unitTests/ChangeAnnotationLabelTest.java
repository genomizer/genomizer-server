package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class ChangeAnnotationLabelTest {

	private static DatabaseAccessor dbac;

	private static String testLabel1 = "Sex";
	private static String testLabel2 = "Species";
	private static String testLabel3 = "Tissue";
	private static String testDefValue3 = "Arm";
	private static ArrayList<String> testChoices1 = new ArrayList<String>();
	private static ArrayList<String> testChoices2 = new ArrayList<String>();

	@BeforeClass
	public static void setupTestCase() throws Exception {
		dbac = new DatabaseAccessor(TestInitializer.username,
				TestInitializer.password, TestInitializer.host,
				TestInitializer.database);


		testChoices1.add("Male");
		testChoices1.add("Female");
		testChoices1.add("Unknown");
		testChoices1.add("moho");

		testChoices2.add("Donkey");
		testChoices2.add("Monkey");
		testChoices2.add("Human");
		testChoices2.add("Yuri");
	}

    @Before
    public void setup() throws SQLException, IOException {

        dbac.addDropDownAnnotation(testLabel1, testChoices1, 0, false);
        dbac.addDropDownAnnotation(testLabel2, testChoices2, 2, false);
        dbac.addFreeTextAnnotation(testLabel3, testDefValue3, false);
    	dbac.addExperiment("Exp1");
    	dbac.annotateExperiment("Exp1", testLabel1, "Female");
    	dbac.annotateExperiment("Exp1", testLabel2, "Yuri");

    }

    @After
    public void teardown() throws SQLException {
    	dbac.deleteExperiment("Exp1");
        dbac.deleteAnnotation(testLabel2);
        dbac.deleteAnnotation(testLabel3);

    }

    /**
     * Test to check that the label changes.
     *
     */
    @Test
    public void shouldChangeChosenLabel() {
    	try {
			assertTrue(1 == dbac.changeAnnotationLabel(testLabel1, "HEJSAN"));
			assertNotNull(dbac.getAnnotationObject("HEJSAN"));
			assertNull(dbac.getAnnotationObject(testLabel1));
			dbac.deleteAnnotation("HEJSAN");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    /**
     * Test to check if the new label name has the same drop down
     * value as the previous label.
     *
     */
    @Test
    public void shouldHaveRightValues() {
    	try {
			assertTrue(1 == dbac.changeAnnotationLabel(testLabel1, "MOI"));
			List<String> list = dbac.getChoices("MOI");
			for(int i = 0; i < testChoices1.size(); i++) {
				assertEquals(list.get(i), testChoices1.get(i));
			}
			dbac.deleteAnnotation("MOI");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
