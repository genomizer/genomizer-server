package testSuite.unitTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class PubMedIsValidTest {


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
    public void isValid(){
    	assertTrue(dbac.isPubMedStringValid("Exp1[ExpID]"));
    }

    @Test
    public void isNotValid(){
    	assertFalse(dbac.isPubMedStringValid("Exp1[ExpID"));
    }
}
