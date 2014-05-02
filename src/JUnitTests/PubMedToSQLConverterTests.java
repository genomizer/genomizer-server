package JUnitTests;

import static org.junit.Assert.*;

import java.util.List;

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

    // Test Exp
    public static String expId = "ExperimentId_";

    // Test File
    public static String path = "/TestPath/gkdbfalkfnvlankfl/testFileName_fghasgha.fastq";
    public static String filename = "testFileName_fghasgha.fastq";
    public static String type = "raw";
    public static String metaData = "/TestPath/inputfile.fastq";
    public static String author = "Ruaridh";
    public static boolean isPrivate = false;

    @Test
    public void shouldParseSinglePairRightFormat() throws Exception {
        PubMedToSQLConverter pmsl = new PubMedToSQLConverter();
        assertEquals(sqlStr1, pmsl.convertFileSearch(pmStr1));

            assertEquals("Species", pmsl.getParameters().get(0));
            assertEquals("Human", pmsl.getParameters().get(1));
            assertEquals(2, pmsl.getParameters().size());
    }

    @Test
    public void shouldBeAbleToSearchUsingPubMedStringWithExpAttributes() throws Exception {

        String username = "c5dv151_vt14";
        String password = "shielohh";
        String host = "postgres";
        String database = "c5dv151_vt14";

        DatabaseAccessor dbac = new DatabaseAccessor(username, password, host, database);

        dbac.addExperiment(expId);
        dbac.addFile(path, type, filename, metaData, author, author, isPrivate, expId, null);
        dbac.tagExperiment(expId, "Species", "Human");

        List<Experiment> experiments = dbac.search("Human[Species]");
        for (Experiment e: experiments) {
            System.out.println(e.getID());
            for (FileTuple ft: e.getFiles()) {
                System.out.println("    " + ft.expId + ft.path);
            }
        }

        dbac.deleteFile(path);
        dbac.deleteExperiment(expId);
    }
}
