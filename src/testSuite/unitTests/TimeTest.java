package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;

public class TimeTest {

    public static String username = "genomizer";
    public static String password = "genomizer";
    public static String host = "85.226.111.95";
    public static String database = "genomizer_testdb";


    private static DatabaseAccessor dbac;




    @BeforeClass
    public static void setup() throws SQLException, IOException {
        dbac = new DatabaseAccessor(TestInitializer.username,
        		TestInitializer.password, TestInitializer.host,
        		TestInitializer.database);
    }

    @AfterClass
    public static void tearDown(){
    	dbac.close();
    }

    @Test
    public void ShouldStillBeConnectedAfter15Minutes() throws SQLException{
    	/*
    	assertTrue(dbac.isConnected());

    	dbac.close();

    	assertFalse(dbac.isConnected());*/

    	assertTrue(dbac.isConnected());

    	try {
			Thread.sleep(15*1000*60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	assertTrue(dbac.isConnected());
    }



}
