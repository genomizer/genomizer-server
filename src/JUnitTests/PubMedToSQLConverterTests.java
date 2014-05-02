package JUnitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import database.PubMedToSQLConverter;
import databaseAccessor.DatabaseAccessor;
import databaseAccessor.Experiment;
import databaseAccessor.FileTuple;

public class PubMedToSQLConverterTests {

    public static String pmStr1 = "Human[Species]";

    public static String sqlStr1 = "SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND "
            + "A.Label = ? AND A.Value = ?)\nORDER BY ExpID";

    // Test Experiments
    public static String expId = "fadhbv787435hgqae983jnbvafkåöä";

    // test Annotation
    public static String testFreeTextAnnotationLabel = "FreeText_annotation";
    public static String testDropDownAnnotationLabel = "DropDown_annotation";
    public static ArrayList<String> testChoices = new ArrayList<String>();
    public static String testChoice = "test_choice_1234rwt";
    public static String testFreeTextValue = "test_free_text_annotation_value";

    // Test File
    public static String filename = "testFileName_fghasgha.fastq";
    public static String type = "raw";
    public static String metaData = "/TestPath/inputfile.fastq";
    public static String author = "Ruaridh Watt";
    public static String uploader = "ruaridhwat";
    public static boolean isPrivate = false;

    public static DatabaseAccessor dbac;

    @BeforeClass
    public static void setup() throws SQLException, IOException {

        testChoices.add(testChoice);
        testChoices.add(testChoice + "2");

        String username = "c5dv151_vt14";
        String password = "shielohh";
        String host = "postgres";
        String database = "c5dv151_vt14";


        // Ruaridh's DB Info (Comment out when at school)
//        String username = "genomizer_prog";
//        String password = "secret";
//        String host = "localhost";
//        String database = "genomizerdb";

        dbac = new DatabaseAccessor(username, password, host, database);

        fillDatabase();
    }

    private static void fillDatabase() throws SQLException, IOException {
/*
        int nrTuples = 24;

        for (int i = 0; i < nrTuples; i++) {
            if (i % 2 == 0) {
                dbac.addDropDownAnnotation(testDropDownAnnotationLabel + i, testChoices);
            } else {
                dbac.addFreeTextAnnotation(testFreeTextAnnotationLabel + i);
            }
        }

        for (int i = 0; i < nrTuples; i++) {
            dbac.addExperiment(expId + i);
            dbac.addFile(type, filename + i, metaData, author, uploader, isPrivate, expId + i, null);
            if (i % 4 == 0) {
                dbac.tagExperiment(expId + i, testDropDownAnnotationLabel + i, testChoices.get(0));
            } else if (i % 4 == 2) {
                dbac.tagExperiment(expId + i, testDropDownAnnotationLabel + i, testChoices.get(1));
            } else if (i % 4 == 3) {
                dbac.tagExperiment(expId + i, testFreeTextAnnotationLabel + i, "Free Tesxt Annotation " + i);
            }
        }
*/
    }

    @Test
    public void shouldParseSinglePairRightFormat() throws Exception {
        PubMedToSQLConverter pmsl = new PubMedToSQLConverter();
        assertEquals(sqlStr1, pmsl.convertFileSearch(pmStr1));

            assertEquals("Species", pmsl.getParameters().get(0));
            assertEquals("Human", pmsl.getParameters().get(1));
            assertEquals(2, pmsl.getParameters().size());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedStringWithExperimentAttribute() throws Exception {

        String username = "c5dv151_vt14";
        String password = "shielohh";
        String host = "postgres";
        String database = "c5dv151_vt14";

        DatabaseAccessor dbac = new DatabaseAccessor(username, password, host, database);

        dbac.addExperiment(expId);
        String path = dbac.addFile(type, filename, metaData, author, author, isPrivate, expId, null);
        dbac.tagExperiment(expId, "Species", "Human");

        List<Experiment> experiments = dbac.search("Human[Species] AND Ruaridh[Author]");
        for (Experiment e: experiments) {
            System.out.println(e.getID());
            for (FileTuple ft: e.getFiles()) {
                System.out.println("    " + ft.expId + ft.path);
            }
        }

        dbac.deleteFile(path);
        dbac.deleteExperiment(expId);
    }

    @Test
    public void testname() throws Exception {

    }
}
