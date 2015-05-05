package conversion.test;

import conversion.ConversionHandler;
import database.DatabaseAccessor;
import database.test.TestInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;

import java.sql.SQLException;

import static junit.framework.Assert.assertNotNull;
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
    private static final String wigVarStepFilePath =
            "resources/conversionTestData/WIG-varstep-testdata.wig";
    private static final String wigBedFilePath =
            "resources/conversionTestData/WIG-from-SGR-testdata.wig";
    private static final String wigFixedStepFilePath =
            "resources/conversionTestData/WIG-testdata.wig";

    private static int bedFileID;
    private static int gffFileID;
    private static int sgrFileID;
    private static int wigVarStepFileID;
    private static int wigBedFileID;
    private static int wigFixedStepFileID;

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
        wigVarStepFileID = db.getFileTuple(wigVarStepFilePath).id;
        wigBedFileID = db.getFileTuple(wigBedFilePath).id;
        wigFixedStepFileID = db.getFileTuple(wigFixedStepFilePath).id;
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException, IOException {
        ti.removeTuples();
        new File("resources/conversionTestData/BED-testdata.sgr").delete();
        new File("resources/conversionTestData/BED-testdata.wig").delete();
        new File("resources/conversionTestData/GFF-testdata.sgr").delete();
        new File("resources/conversionTestData/GFF-testdata.wig").delete();
        new File("resources/conversionTestData/SGR-testdata.wig").delete();
        new File("resources/conversionTestData/WIG-from-SGR-testdata.sgr").delete();
        new File("resources/conversionTestData/WIG-testdata.sgr").delete();
        new File("resources/conversionTestData/WIG-varstep-testdata.sgr").delete();

        new File("resources/conversionTestData/output/Exp3/profile/0").delete();
        new File("resources/conversionTestData/output/Exp3/profile").delete();
        new File("resources/conversionTestData/output/Exp3").delete();
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
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/BED-testdata.sgr"));
    }

    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromBedToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", bedFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/BED-testdata.wig"));
    }


    /* IllegalArgumentException should be thrown when converting from sgr to sgr
  * Test created: 2015-05-04
  */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToSgr() throws IOException, SQLException {
        ch.convertProfileData("sgr", sgrFileID);
    }

    /* IllegalArgumentException should be thrown when converting from sgr to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", sgrFileID);
    }

    /* IllegalArgumentException should be thrown when converting from sgr to bed
 * Test created: 2015-05-04
 */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", sgrFileID);
    }


    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromSgrToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", sgrFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/SGR-testdata.wig"));
    }



    /* IllegalArgumentException should be thrown when converting from sgr to bed
* Test created: 2015-05-04
*/
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnGffToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", gffFileID);
    }

    /* IllegalArgumentException should be thrown when converting from sgr to bed
 * Test created: 2015-05-04
 */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnGffToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", gffFileID);
    }

    /* A file tuple should be present in database after conversion
 * Test created: 2015-05-04
 */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromGffToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", gffFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/GFF-testdata.sgr"));
    }

    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromGffToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", gffFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/GFF-testdata.wig"));
    }


    /* IllegalArgumentException should be thrown when converting from sgr to bed
* Test created: 2015-05-04
*/
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", wigFixedStepFileID);
    }


    /* IllegalArgumentException should be thrown when converting from sgr to bed
* Test created: 2015-05-04
*/
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", wigFixedStepFileID);
    }

    /* IllegalArgumentException should be thrown when converting from sgr to bed
* Test created: 2015-05-04
*/
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", wigFixedStepFileID);
    }




    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromWigBedToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigBedFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/WIG-from-SGR-testdata.sgr"));
    }

    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromWigVarStepToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigVarStepFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/WIG-varstep-testdata.sgr"));
    }


    /* A file tuple should be present in database after conversion
     * Test created: 2015-05-04
     */
    @Test
    public void shouldBeAbleToFetchFileFromDatabaseAfterConversionFromWigFixedStepToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigFixedStepFileID);
        assertNotNull(db.getFileTuple(System.getProperty("user.dir")+"/resources/conversionTestData/output/Exp3/profile/0/WIG-testdata.sgr"));
    }

}