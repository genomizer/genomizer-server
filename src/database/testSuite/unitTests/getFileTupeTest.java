package database.testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;
import database.FileTuple;
import database.testSuite.TestInitializer;

public class getFileTupeTest {
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
    public void testGetFileTupleID() throws IOException, SQLException, ParseException {
    	List<Experiment> elist = dbac.search("exp2[expid] AND UCSC[author]");

    	int id = elist.get(0).getFiles().get(0).id;
    	FileTuple tuple = dbac.getFileTuple(id);
    	assertEquals(elist.get(0).getFiles().get(0).id, tuple.id);
    	assertEquals(elist.get(0).getFiles().get(0).author, tuple.author);

    }

    @Test
    public void testGetFilePath() throws IOException, SQLException, ParseException {
    	List<Experiment> elist = dbac.search("exp3[expid] AND genomizer[author]");

    	String path = elist.get(0).getFiles().get(0).path;
    	FileTuple tuple = dbac.getFileTuple(path);
    	assertEquals(elist.get(0).getFiles().get(0).id, tuple.id);
    	assertEquals(elist.get(0).getFiles().get(0).author, tuple.author);
    }
}
