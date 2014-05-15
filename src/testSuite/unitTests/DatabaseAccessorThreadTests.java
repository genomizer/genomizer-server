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
	public void addNRemoveOneFileFromCurrentThread() throws SQLException, IOException{

		DatabaseAccessor dbac = new DatabaseAccessor(username, password, host, database);

		String fileName = "StressTest0_t:9.hej";
		String filePath = "Stress0_t:9.hej";


		dbac.addGenomeRelease("hg38", "Fly", "hg38.gr");
		dbac.addExperiment("Exp1");

		dbac.addNewFile("Exp1", 1, fileName , filePath,
								"-m -a -te","Smurf", "Claes", true,"hg38");

		List<Experiment> resExp = dbac.search("Claes[Uploader]");

		assertEquals(1,resExp.size());
		assertEquals(fileName,resExp.get(0).getFiles().get(0).filename);

		dbac.deleteFile(resExp.get(0).getFiles().get(0).id);
		dbac.deleteExperiment("Exp1");
		dbac.removeGenomeRelease("hg38", "Fly");

		dbac.close();
	}

    @Test
    public void addNRemoveFileFromSepparateThreads() throws SQLException, IOException {

    	ArrayList<Runnable> allRunnables = new ArrayList<Runnable>();
    	ArrayList<Thread> allThreads = new ArrayList<Thread>();

    	//specify the number of threads. 1 = works, 1< = nope.
    	int nrOfThreads = 1;

    	for(int i=0;i<nrOfThreads;i++){
    		allRunnables.add(new myRunnable());
    		allThreads.add(new Thread(allRunnables.get(i)));
    	}

    	DatabaseAccessor dbac = new DatabaseAccessor(username, password,
    			host, database);

    	dbac.addGenomeRelease("hg38", "Fly", "hg38.gr");
    	dbac.addExperiment("Exp1");

    	// start all the threads
    	for(int i=0;i<nrOfThreads;i++){
    		allThreads.get(i).start();
    	}

    	try {
    		//wait some time to be sure that threads are done.
    		Thread.sleep(2000);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}

    	List<Experiment> resExp = dbac.search("Claes[Uploader]");


    	//THINGS BELOW DOESN'T WORK FOR MORE SEPPARATE THREADS THAN ONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    	//all files added to db by sepparate threads, threads counting from 9.
    	for(int i=9;i<nrOfThreads+9;i++){	//hard coded thread nr, fix later :D
    		String fileName1 = "StressTest0_t:"+ i + ".hej";

    		assertEquals(1,resExp.size());
    		assertEquals(fileName1,resExp.get(0).getFiles().get(0).filename);
    	}

    	dbac.deleteFile(resExp.get(0).getFiles().get(0).id);

    	dbac.deleteExperiment("Exp1");
    	dbac.removeGenomeRelease("hg38", "Fly");
    	dbac.close();
    }

    //class where threads will add files to database.
    public class myRunnable implements Runnable{

		public void run(){

			System.err.println("Started thread nr: " + Thread.currentThread().getId());

			int nrOfFiles = 1, nr = 0;

		    try {
		    	System.err.println("--z<<<<<<<<<<<<<<<<<<<<<<<--");
		    	DatabaseAccessor dbac2 = new DatabaseAccessor(username, password,
		    			host, database);

		    	System.err.println("-------------------");
		    	//adding files
		    	for(int i=0;i<nrOfFiles;i++){
		    		nr = i+1;
System.err.println("thread: " + Thread.currentThread().getId() +" loop lap: " + nr);

		    		dbac2.addNewFile("Exp1", 1,
		    		"StressTest" + i + "_t:" + Thread.currentThread().getId() +
		    		".hej" , "Stress" + i + "_t:" +
		    		Thread.currentThread().getId(),
		    		"-m -a -te","Smurf", "Claes", true,"hg38");

		    	}
		    	dbac2.close();

		    } catch (SQLException e) {
		    	System.err.println("Failed to add so many files at the " +
		    			"same time!, Expcetion thrown when " +
		    			"adding the " + nr + "file.");
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
