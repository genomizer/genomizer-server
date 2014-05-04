package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import database.DatabaseAccessor;
import database.PubMedToSQLConverter;

public class PubMedToSQLConverterTests {

    private static DatabaseAccessor dbac;
    private static PubMedToSQLConverter pm2sql;

    public String expConstraintPmStr = "Human[Species]";
    public String fileConstraintPmStr = "Ruaridh Watt[Author]";

    public String multipleExpConstraintsPmStr = "Human[Species] AND Unknown[Sex]";
    public String multipleExpConstraintsPmStrWithBrackets = "Human[Species] AND (Unknown[Sex] OR Arm[Tissue])";

    public String multipleMixedConstraintsPmStrWithBrackets = "(Ruaridh Watt[Author] OR (Human[Species] AND Arm[Tissue]))";

    private String sqlFragmentForExpSearch = "SELECT ExpID FROM Experiment NATURAL JOIN Annotated_With "
            + "WHERE Label = ? AND Value = ?";

    private String sqlFragmentForExpAttrInFileSearch = "SELECT * FROM File AS F "
            + "WHERE EXISTS (SELECT * FROM Annotated_With AS A "
            + "WHERE F.ExpID = A.ExpID AND "
            + "A.Label = ? AND A.Value = ?)";

    private String sqlFragmentForFileAttr = "SELECT * FROM File "
            + "WHERE ";

    private String orderBySqlFragment = "\nORDER BY ExpID";

    @BeforeClass
    public static void setupBeforeClass() throws SQLException,
            IOException {

        dbac = new DatabaseAccessor(SearchDatabaseTests.username,
                SearchDatabaseTests.password,
                SearchDatabaseTests.host,
                SearchDatabaseTests.database);
        pm2sql = new PubMedToSQLConverter();
    }

    @AfterClass
    public static void teardownAfterClass() throws SQLException,
            IOException {

        dbac.close();
    }

    @Test
    public void shouldConvertExpConstraintPmStr() throws Exception {
        String query = pm2sql
                .convertExperimentSearch(expConstraintPmStr);

        String expected = sqlFragmentForExpSearch
                + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(2, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0));
        assertEquals("Human", pm2sql.getParameters().get(1));
    }

    @Test
    public void shouldConvertMultipleExpConstraintPmStr()
            throws Exception {
        String query = pm2sql
                .convertExperimentSearch(multipleExpConstraintsPmStr);

        String expected = sqlFragmentForExpSearch + "\nINTERSECT\n"
                + sqlFragmentForExpSearch + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(4, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0));
        assertEquals("Human", pm2sql.getParameters().get(1));
        assertEquals("Sex", pm2sql.getParameters().get(2));
        assertEquals("Unknown", pm2sql.getParameters().get(3));
    }

    @Test
    public void shouldConvertMultipleExpConstraintPmStrRetainingRoundBrackets()
            throws Exception {

        String query = pm2sql
                .convertExperimentSearch(multipleExpConstraintsPmStrWithBrackets);

        String expected = sqlFragmentForExpSearch + "\nINTERSECT\n"
                + "(" + sqlFragmentForExpSearch + "\nUNION\n"
                + sqlFragmentForExpSearch + ")" + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(6, pm2sql.getParameters().size());
        assertEquals("Species", pm2sql.getParameters().get(0));
        assertEquals("Human", pm2sql.getParameters().get(1));
        assertEquals("Sex", pm2sql.getParameters().get(2));
        assertEquals("Unknown", pm2sql.getParameters().get(3));
        assertEquals("Tissue", pm2sql.getParameters().get(4));
        assertEquals("Arm", pm2sql.getParameters().get(5));
    }

    @Test
    public void shouldConvertFileConstraintPmStr() throws Exception {

        String query = pm2sql.convertFileSearch(fileConstraintPmStr);

        String expected = sqlFragmentForFileAttr + "Author = ? "
                + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(1, pm2sql.getParameters().size());
        assertEquals("Ruaridh Watt", pm2sql.getParameters().get(0));
    }

    @Test
    public void shouldConvertMultipleMixedConstraintPmStrRetainingRoundBrackets()
            throws Exception {

        String query = pm2sql
                .convertFileSearch(multipleMixedConstraintsPmStrWithBrackets);

        // (Ruaridh Watt[Author] OR (Human[Species] AND Arm[Tissue]))

        String expected = "(" + sqlFragmentForFileAttr
                + "Author = ? " + "\nUNION\n" + "("
                + sqlFragmentForExpAttrInFileSearch + "\nINTERSECT\n"
                + sqlFragmentForExpAttrInFileSearch + "))"
                + orderBySqlFragment;

        assertEquals(expected, query);
        assertEquals(5, pm2sql.getParameters().size());
        assertEquals("Ruaridh Watt", pm2sql.getParameters().get(0));
        assertEquals("Species", pm2sql.getParameters().get(1));
        assertEquals("Human", pm2sql.getParameters().get(2));
        assertEquals("Tissue", pm2sql.getParameters().get(3));
        assertEquals("Arm", pm2sql.getParameters().get(4));
    }

}
