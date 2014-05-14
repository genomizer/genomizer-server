package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
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

    public boolean isPubMedStringValid(String pubMedString) throws IOException{

    	int squareBracketsStart=0, squareBracketsStop=0;

    	for(int i=0;i<pubMedString.length();i++){
        	System.out.print(pubMedString.charAt(i));

        	if (pubMedString.charAt(i) == '['){
        		squareBracketsStart++;

        	} else if (pubMedString.charAt(i) == ']') {
        		squareBracketsStop++;
        	}
    	}

    	System.out.println(" ");

    	if(squareBracketsStart==squareBracketsStop){
    		return true;
    	}else{
    		throw new IOException("Ugly PubMed String");
    	}

    }

    @Test
    public void isValid() throws IOException{

		assertTrue(isPubMedStringValid("Exp1[ExpID]"));

    }

    @Test(expected=IOException.class)
    public void isNotValid() throws IOException{

		isPubMedStringValid("Exp1[ExpID");

    }
}
