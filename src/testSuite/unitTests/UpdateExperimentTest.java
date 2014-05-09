package testSuite.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import testSuite.TestInitializer;
import database.DatabaseAccessor;
import database.Experiment;

public class UpdateExperimentTest {

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
   public void shouldUpdateExperimentFreetext() throws SQLException, IOException{

	   String resBefore = localGetExperiment("Exp1","Tissue");

	   assertEquals(1,dbac.updateExperiment("Exp1","Tissue","Leg"));

	   String resAfter = localGetExperiment("Exp1","Tissue");

	   assertFalse(resBefore.equals(resAfter));

   }


   @Test
   public void shouldUpdateExperimentDropDown() throws SQLException, IOException{

	   String resBefore = localGetExperiment("Exp1","Sex");

	   assertEquals(1,dbac.updateExperiment("Exp1","Sex","Male"));

	   String resAfter = localGetExperiment("Exp1","Sex");

	   assertFalse(resBefore.equals(resAfter));

   }

   public String localGetExperiment(String expID, String label) throws SQLException{

	   Experiment e=dbac.getExperiment(expID);
	   HashMap<String, String> hm = e.getAnnotations();

	   String res=hm.get(label);

	   return res;
   }


}
