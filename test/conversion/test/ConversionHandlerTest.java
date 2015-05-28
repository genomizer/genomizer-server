package conversion.test;

import conversion.ConversionHandler;
import database.DatabaseAccessor;
import database.containers.FileTuple;
import database.test.TestInitializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import server.ServerSettings;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/*
 * Tests for ConversionHandler
 * Created 2015-05-04.
 *
 * @author Albin Råstander <c12arr@cs.umu.se>
 * @author Martin Larsson <dv13mln@cs.umu.se>
 *
 */
@Ignore
public class ConversionHandlerTest {
    private static TestInitializer ti;
    private static DatabaseAccessor db;
    private static ConversionHandler ch;

    private static final String TESTFOLDER = "resources/conversionTestData/";
    private static final String OUTPUTFOLDER = "output/";

    private static final String bedFilePath = TESTFOLDER+"BED-testdata.bed";
    private static final String gffFilePath = TESTFOLDER+"GFF-testdata.gff";
    private static final String sgrFilePath = TESTFOLDER+"SGR-testdata.sgr";
    private static final String wigVarStepFilePath =
            TESTFOLDER+"WIG-varstep-testdata.wig";
    private static final String wigBedFilePath =
            TESTFOLDER+"WIG-from-SGR-testdata.wig";
    private static final String wigFixedStepFilePath =
            TESTFOLDER+"WIG-testdata.wig";

    private static int bedFileID, gffFileID, sgrFileID,wigVarStepFileID,
            wigBedFileID, wigFixedStepFileID;

    private static String userDir, tempFileLocation;

    @BeforeClass
    public static void setUpTestCase() throws Exception {
        userDir = new File("").getAbsolutePath();

        TestInitializer.setupServerSettings();
        tempFileLocation = ServerSettings.fileLocation;
        ServerSettings.fileLocation = userDir+File.separator+TESTFOLDER+
                OUTPUTFOLDER;

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

    /*
     * Removes test tuples from database, deletes generated test files
     * and resets the servers file location.
     * @throws SQLException
     * @throws IOException
     */
    @AfterClass
    public static void undoAllChanges() throws SQLException, IOException {
        ti.removeTuples();
        deleteGeneratedTestFiles();
        ServerSettings.fileLocation = tempFileLocation;
    }

    /*
     * Deletes the files that all tests generated
     */
    private static void deleteGeneratedTestFiles() {
        new File(TESTFOLDER+"BED-testdata.sgr").delete();
        new File(TESTFOLDER+"BED-testdata.wig").delete();
        new File(TESTFOLDER+"GFF-testdata.sgr").delete();
        new File(TESTFOLDER+"GFF-testdata.wig").delete();
        new File(TESTFOLDER+"SGR-testdata.wig").delete();
        new File(TESTFOLDER+"WIG-from-SGR-testdata.sgr").delete();
        new File(TESTFOLDER+"WIG-testdata.sgr").delete();
        new File(TESTFOLDER+"WIG-varstep-testdata.sgr").delete();

        new File(TESTFOLDER+OUTPUTFOLDER+"Exp3/profile/0").delete();
        new File(TESTFOLDER+OUTPUTFOLDER+"Exp3/profile").delete();
        new File(TESTFOLDER+OUTPUTFOLDER+"Exp3").delete();
    }

    /*
     * BED TO X ---------------------------------------------------------------
     *
     *
     *IllegalArgumentException should be thrown when converting from bed to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnBedToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", bedFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from bed to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnBedToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", bedFileID);
    }

    /*
     * A file tuple with correct path should be present in database
     * after conversion from bed to sgr
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromBedToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", bedFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/BED-testdata.sgr"));
    }

    /*
     * A file tuple with correct path should be present in database
     * after conversion from bed to wig
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromBedToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", bedFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/BED-testdata.wig"));
    }


    /*
     * SGR TO X ---------------------------------------------------------------
     *
     *
     * IllegalArgumentException should be thrown when converting from sgr to sgr
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToSgr() throws IOException, SQLException {
        ch.convertProfileData("sgr", sgrFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from sgr to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", sgrFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from sgr to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnSgrToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", sgrFileID);
    }


    /*
     * A file tuple with correct path should be present in database
     * after conversion from sgr to wig
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromSgrToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", sgrFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/SGR-testdata.wig"));
    }

    /*
     * GFF TO X ---------------------------------------------------------------
     *
     *
     * IllegalArgumentException should be thrown when converting from gff to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnGffToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", gffFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from gff to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnGffToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", gffFileID);
    }

    /*
     * A file tuple with correct path should be present in database
     * after conversion from gff to sgr
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromGffToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", gffFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/GFF-testdata.sgr"));
    }

    /*
     * A file tuple with correct path should be present in database
     * after conversion from gff to wig
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromGffToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", gffFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/GFF-testdata.wig"));
    }


    /*
     * WIG FIXEDSTEP TO X -----------------------------------------------------
     *
     *
     * IllegalArgumentException should be thrown when converting from
     * wigFixedStep to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", wigFixedStepFileID);
    }


    /*
     * IllegalArgumentException should be thrown when converting from
     * wigFixedStep to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", wigFixedStepFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigFixedStep to wig
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", wigFixedStepFileID);
    }

    /*
     * A file tuple with correct path should be present in database
     * after conversion from wigFixedStep to sgr
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromWigFixedStepToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigFixedStepFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/WIG-testdata.sgr"));
    }

    /*
     * WIG BED TO X -----------------------------------------------------------
     *
     *
     * A file tuple with correct path should be present in database
     * after conversion from wigBed to sgr
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromWigBedToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigBedFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/WIG-from-SGR-testdata.sgr"));
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigBed to wig
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigBedToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", wigFixedStepFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigBed to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigBedToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", wigFixedStepFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigBed to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigBedToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", wigFixedStepFileID);
    }

    /*
     * WIG VARSTEP TO X -------------------------------------------------------
     *
     *
     * A file tuple with correct path should be present in database
     * after conversion from wigVarStep to sgr
     * Test created: 2015-05-04
     */
    @Test
    public void shouldFetchFileFromDBAfterConversionFromWigVarStepToSgr()
            throws IOException, SQLException {
        ch.convertProfileData("sgr", wigVarStepFileID);
        assertNotNull(db.getFileTuple(userDir+"/resources/conversionTestData/" +
                "output/Exp3/profile/0/WIG-varstep-testdata.sgr"));
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigVarStep to wig
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigVarStepToWig()
            throws IOException, SQLException {
        ch.convertProfileData("wig", wigVarStepFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigVarStep to gff
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigVarStepToGff()
            throws IOException, SQLException {
        ch.convertProfileData("gff", wigVarStepFileID);
    }

    /*
     * IllegalArgumentException should be thrown when converting from
     * wigVarStep to bed
     * Test created: 2015-05-04
     */
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnWigVarStepToBed()
            throws IOException, SQLException {
        ch.convertProfileData("bed", wigVarStepFileID);
    }



 /*
 * File should be exist at correct path after conversion
 * after conversion from sgr to wig
 * Test created: 2015-05-04
 */
    @Test
    public void shouldExistFileAfterConversion()
            throws IOException, SQLException {
        FileTuple ft = ch.convertProfileData("wig", sgrFileID);

        assertTrue(new File(ft.path).exists());
    }
}