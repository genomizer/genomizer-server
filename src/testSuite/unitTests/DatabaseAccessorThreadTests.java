package testSuite.unitTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import database.DatabaseAccessor;
import database.Experiment;

public class DatabaseAccessorThreadTests {

	public static String username = "genomizer";
	public static String password = "genomizer";
	public static String host = "85.226.111.95";
	public static String database = "genomizer_threads";


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnection() throws Exception {
		DatabaseAccessor dbac = new DatabaseAccessor(username, password, host, database);
		assertTrue(dbac.isConnected());
		dbac.close();
	}

	@Test
	public void addOneFileFromCurrentThread() throws SQLException, IOException{

		DatabaseAccessor dbac = new DatabaseAccessor(username, password, host, database);

		String fileName = "StressTest" + 0 + "_t:9.hej";
		String filePath = "Stress" + 0 + "_t:9.hej";


		dbac.addGenomeRelease("hg38", "Fly", "hg38.gr");
		dbac.addExperiment("Exp1");

		dbac.addNewFile("Exp1", 1, fileName , filePath,
								"-m -a -te","Smurf", "Claes", true,"hg38");

		List<Experiment> resExp = dbac.search("Claes[Uploader]");

		assertEquals(1,resExp.size());

		dbac.close();

	}






    @Ignore
    public void stressTestAddAndRemoveFiles() {

    	int currentWorkedFile = 0;
    	int nrOfThreads = 1;
    	Runnable theAddRun = new myRunnable();
    	ArrayList<Thread> allThreads = new ArrayList<Thread>();

    	//adding with multi-threaded adds.

    	//for(int i=0;i<nrOfThreads;i++){
    		allThreads.add(new Thread(theAddRun));
    	//}

    	// start all the threads
    	//for(int i=0;i<nrOfThreads;i++){
    		allThreads.get(0).start();
    	//}

    	//removing
    	/*try {

    		List<Experiment> resFiles;
			try {
				resFiles = dbac.search("Claes[Uploader]");

				for(int j=0;j<resFiles.get(0).getFiles().size();j++){

					currentWorkedFile = resFiles.get(0).getFiles().get(j).id;
					dbac.deleteFile(currentWorkedFile);

				}
			} catch (IOException e) {

				System.err.println("Failed to search the database for the " +
								    "added files!");
				fail();
			}
		} catch (SQLException e) {
			System.err.println("Failed to Delete the file: "+currentWorkedFile);
			fail();
		}*/
    }

    //class where threads will add files to database.
    public class myRunnable implements Runnable{

		public void run(){

			System.err.println("Started thread nr: " + Thread.currentThread().getId());

			int nrOfFiles = 3, nr = 0;
			String fileName = "";
			String filePath = "";

		    try {
		    	System.err.println("--z<<<<<<<<<<<<<<<<<<<<<<<--");
		    	DatabaseAccessor dataAccess = new DatabaseAccessor(username,
		    										password,host,database);

		    	System.err.println("-------------------");
				//adding files
		    	for(int i=0;i<nrOfFiles;i++){
	    			nr = i+1;
	    			System.err.println("thread: " +
	    					Thread.currentThread().getId() +" loop lap: " + nr);


	    			fileName = "StressTest" + i + "_t:" +
	    						 Thread.currentThread().getId() +".hej";
	    			filePath = "Stress" + i + "_t:" +
	    						 Thread.currentThread().getId();

	    			dataAccess.addNewFile("Exp1", 1, fileName , filePath,
	    									"-m -a -te","Smurf", "Claes", true,
	    										"hg38");
	    			System.err.println("SUCCESS!!");
		    	}

		    	dataAccess.close();

		    } catch (SQLException e) {
		    	System.err.println("Failed to add so many files at the " +
		    			"same time!, Expcetion thrown when " +
		    			"adding the " + nr + " file: " +
		    			fileName);
		    	e.printStackTrace();
		    	fail();
		    } catch (IOException e){
		    	System.err.println("Failed to connect to database!");
		    	e.printStackTrace();
		    	fail();
			}
		}
	}

}
