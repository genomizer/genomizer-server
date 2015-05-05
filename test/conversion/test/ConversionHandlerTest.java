package conversion.test;

import conversion.ConversionHandler;
import database.DatabaseAccessor;
import database.test.TestInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import server.ServerSettings;

import java.io.IOException;

import java.sql.SQLException;

import static junit.framework.TestCase.fail;


/**
 * Created by dv13mln on 2015-05-04.
 */

public class ConversionHandlerTest {
    private static TestInitializer ti;
    private static DatabaseAccessor db;
    private static ConversionHandler ch;
    private static final String bedFilePath =
            "resources/conversionTestData/BED-testdata.bed";
    private static final String gffFilePath =
            "resources/conversionTestData/GFF-testdata.gff";
    private static final String sgrFilePath =
            "resources/conversionTestData/SGR-testdata.sgr";
    private static final String wigFilePath =
            "resources/conversionTestData/WIG-testdata.wig";
    private static int bedFileID;
    private static int gffFileID;
    private static int sgrFileID;
    private static int wigFileID;
    private static String tempdbUser, tempdbPw, tempdbHost, tempdbName;

    @BeforeClass
    public static void setUpTestCase() throws Exception {
        TestInitializer.setupServerSettings();
        String travis = System.getenv("TRAVIS");
        if(travis == null || travis.equals("false")) {
            // Not running on Travis.
            ServerSettings.fileLocation = System.getProperty("user.dir")+"/resources/conversionTestData/output/";
        }

        ti = new TestInitializer();
        db = ti.setup();
        ch = new ConversionHandler();

        bedFileID = db.getFileTuple(bedFilePath).id;
        gffFileID = db.getFileTuple(gffFilePath).id;
        sgrFileID = db.getFileTuple(sgrFilePath).id;
        wigFileID = db.getFileTuple(wigFilePath).id;
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException, IOException {
        ti.removeTuples();
    }

    /* IllegalArgumentException should be thrown when converting from bed to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnBedToBed() throws IOException, SQLException {
        ch.convertProfileData("bed", bedFileID);
    }

    /* IllegalArgumentException should be thrown when converting from bed to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnBedToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", bedFileID);
    }

    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromBedToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", bedFileID);

    }

    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromBedToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", bedFileID);
        //TODO check against database

    }
}