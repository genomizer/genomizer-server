package database.test.unittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.PubMedToSQLConverter;
import database.test.TestInitializer;

public class PubMedToSQLConverterTests {

    private static DatabaseAccessor dbac;
    private static PubMedToSQLConverter pm2sql;

    public String expConstraintPmStr = "Human[Species]";
    public String fileConstraintPmStr = "Ruaridh Watt[Author]";

    public String multipleExpConstraintsPmStr =
    		"Human[Species] AnD Unknown[Sex]";
    public String multipleExpConstraintsPmStrWithBrackets =
    		"Human[Species] ANd (Unknown[Sex] OR Arm[Tissue])";

    public String multipleMixedConstraintsPmStrWithBrackets =
    		"(Ruaridh Watt[Author] OR (Human[Species] AND Arm[Tissue]))";

    public String expConstraintPmStrNOT = "NOT Human[Species]";
    public String multipleExpConstraintsPmStrNOT =
    		"Human[Species] NOT Unknown[Sex]";

    private String sqlFragmentForExpSearch =
    		"SELECT ExpID FROM Experiment NATURAL JOIN Annotated_With "
            + "WHERE Label ~~* ? AND Value ~* ?";

    private String sqlFragmentForExpSearchNegated =
    		"SELECT ExpID FROM Experiment AS E "
            + "WHERE NOT EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE E.ExpID = A.ExpID AND Label ~~* ? AND Value ~* ?)";

    private String sqlFragmentForExpAttrInFileSearch =
    		"SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND A.Label ~~* ? AND A.Value ~* ?) AND F.Status = 'Done'";

    private String sqlFragmentForExpAttrInFileSearchNegated =
    		"SELECT * FROM File AS F "
            + "WHERE NOT EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND A.Label ~~* ? AND A.Value ~* ?) AND F.Status = 'Done'";

    private String sqlFragmentForFileAttr = "SELECT * FROM File " + "WHERE Status='Done' AND ";

    private String orderBySqlFragment = "\nORDER BY ExpID";

    @BeforeClass
    public static void setupBeforeClass() throws SQLException, IOException {

        dbac = new DatabaseAccessor(TestInitializer.username,
        		TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);
        pm2sql = new PubMedToSQLConverter();
    }

    @AfterClass
    public static void teardownAfterClass() throws SQLException, IOException {

        dbac.close();
    }

    @Test
    public void shouldConvertExpConstraintPmStr() throws Exception {

        String query = pm2sql.convertExperimentSearch(expConstraintPmStr);
        String expected = sqlFragmentForExpSearch + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(2, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0).getKey());
        assertEquals("Human", pm2sql.getParameters().get(1).getKey());
    }

    @Test
    public void shouldConvertMultipleExpConstraintPmStr() throws Exception {

        String query = pm2sql
                .convertExperimentSearch(multipleExpConstraintsPmStr);

        String expected = sqlFragmentForExpSearch + "\nINTERSECT\n"
                + sqlFragmentForExpSearch + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(4, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0).getKey());
        assertEquals("Human", pm2sql.getParameters().get(1).getKey());
        assertEquals("Sex", pm2sql.getParameters().get(2).getKey());
        assertEquals("Unknown", pm2sql.getParameters().get(3).getKey());
    }

    @Test
    public void shouldConvertMultipleExpConstraintPmStrRetainingRoundBrackets()
            throws Exception {

        String query = pm2sql
                .convertExperimentSearch(
                multipleExpConstraintsPmStrWithBrackets);

        String expected = sqlFragmentForExpSearch + "\nINTERSECT\n" + "("
                + sqlFragmentForExpSearch + "\nUNION\n"
                + sqlFragmentForExpSearch + ")" + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(6, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0).getKey());
        assertEquals("Human", pm2sql.getParameters().get(1).getKey());
        assertEquals("Sex", pm2sql.getParameters().get(2).getKey());
        assertEquals("Unknown", pm2sql.getParameters().get(3).getKey());
        assertEquals("Tissue", pm2sql.getParameters().get(4).getKey());
        assertEquals("Arm", pm2sql.getParameters().get(5).getKey());
    }

    @Test
    public void shouldConvertFileConstraintPmStr() throws Exception {

        String query = pm2sql.convertFileSearch(fileConstraintPmStr);

        String expected = sqlFragmentForFileAttr + "Author ~* ?"
                + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(1, pm2sql.getParameters().size());
        assertEquals("Ruaridh Watt", pm2sql.getParameters().get(0).getKey());
    }

    @Test
    public void shouldConvertMultplMixedConstraintPmStrRetainingRoundBrackets()
            throws Exception {

        String query = pm2sql
                .convertFileSearch(multipleMixedConstraintsPmStrWithBrackets);

        // (Ruaridh Watt[Author] OR (Human[Species] AND Arm[Tissue]))

        String expected = "(" + sqlFragmentForFileAttr + "Author ~* ?"
                + "\nUNION\n" + "(" + sqlFragmentForExpAttrInFileSearch
                + "\nINTERSECT\n" + sqlFragmentForExpAttrInFileSearch + "))"
                + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(5, pm2sql.getParameters().size());
        assertEquals("Ruaridh Watt", pm2sql.getParameters().get(0).getKey());
        assertEquals("Species", pm2sql.getParameters().get(1).getKey());
        assertEquals("Human", pm2sql.getParameters().get(2).getKey());
        assertEquals("Tissue", pm2sql.getParameters().get(3).getKey());
        assertEquals("Arm", pm2sql.getParameters().get(4).getKey());
    }

    @Test
    public void shouldConvertPubMedStringBeginingWithNot() throws Exception {

        String query = pm2sql.convertExperimentSearch(expConstraintPmStrNOT);

        String expected = sqlFragmentForExpSearchNegated + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(2, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0).getKey());
        assertEquals("Human", pm2sql.getParameters().get(1).getKey());
    }

    @Test
    public void shouldConvertNOTInMultiPubMedString() throws Exception {

        String query = pm2sql
                .convertExperimentSearch(multipleExpConstraintsPmStrNOT);

        String expected = sqlFragmentForExpSearch + "\nINTERSECT\n"
                + sqlFragmentForExpSearchNegated + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(4, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0).getKey());
        assertEquals("Human", pm2sql.getParameters().get(1).getKey());
        assertEquals("Sex", pm2sql.getParameters().get(2).getKey());
        assertEquals("Unknown", pm2sql.getParameters().get(3).getKey());
    }

    @Test
    public void shouldConvertNOTInMultiPubMedString2() throws Exception {

        String query = pm2sql
                .convertFileSearch("NOT Ruaridh[Author] Not Human[Species]");

        String expected = sqlFragmentForFileAttr + "Author NOT ~~* ?" +
        		"\nINTERSECT\n" + sqlFragmentForExpAttrInFileSearchNegated +
        		orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(3, pm2sql.getParameters().size());
        assertEquals("Ruaridh", pm2sql.getParameters().get(0).getKey());
        assertEquals("Species", pm2sql.getParameters().get(1).getKey());
        assertEquals("Human", pm2sql.getParameters().get(2).getKey());
    }

    @Test
    public void printTest() throws Exception {

        System.out.println(pm2sql.convertFileSearch("sgfsa[Path]"));
    }
}
