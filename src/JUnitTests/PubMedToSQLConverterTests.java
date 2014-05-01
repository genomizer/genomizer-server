package JUnitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import database.PubMedToSQLConverter;

public class PubMedToSQLConverterTests {

    public static String pmStr1 = "Human[Species]";

    public static String sqlStr1 = "SELECT * FROM File AS F " +
                                   "WHERE EXISTS (SELECT * FROM Annotated_With AS A " +
                                                 "WHERE F.ExpID = A.ExpID AND " +
                                                 "A.Label = ? AND A.Value = ?);";


    @Test
    public void shouldParseSinglePairRightFormat() throws Exception {

        assertEquals(, PubMedToSQLConverter.convert(pmStr1));
    }

}
