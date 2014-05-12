package testSuite.unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class TestGenomeRelase {

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
    public void getAllGenomeReleases() throws SQLException{
    	assertEquals(3,dbac.getAllGenomReleases("Human").size());
    }

    @Test
    public void getGenomeRelease() throws SQLException{
    	assertEquals("/var/www/data/GenomeRelease/Rat/rn5.fasta", dbac.getGenomeRelease("rn5"));
    }
}
