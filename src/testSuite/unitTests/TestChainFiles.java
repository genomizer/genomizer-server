package testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class TestChainFiles {

    private static DatabaseAccessor dbac;
    private static TestInitializer ti;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        ti = new TestInitializer();
        dbac = ti.setup();
    }

    @AfterClass
    public static void undoAllChanges() throws SQLException {
        ti.removeTuples();
    }

    @Test
    public void addChain_file() throws SQLException {

        String fromVersion = "hg18";
        String toVersion = "hg38";
        String fileName = "chainHuman";

        String filePath = dbac.addChainFile(fromVersion, toVersion, fileName);

        assertEquals(
                "http://scratchy.cs.umu.se:8000/upload.php?path=/var/www/data/chain_files/Human/"
                        + fromVersion
                        + " - "
                        + toVersion
                        + File.separator
                        + "chainHuman", filePath);
    }

    @Test
    public void removeChainFile() throws SQLException {
        String fromVersion = "hg19";
        String toVersion = "hg38";

        assertEquals(1, dbac.removeChainFile(fromVersion, toVersion));

    }

    @Test
    public void getChainFIle() throws SQLException {
        String fromVersion = "rn3";
        String toVersion = "rn4";

        String filePath = dbac.getChainFile(fromVersion, toVersion);

        assertEquals("/var/www/data/Chain_File/Rat/rn3-rn4.fasta", filePath);
    }
}