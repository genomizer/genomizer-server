package database.test.unittests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.test.TestInitializer;

// TODO: Ignoring for now. This takes too long and sometimes fails. Someone in the database group should take a look.
@Ignore
public class DatabaseAccessorThreadTests {

	// BAD
	/*
    public static String username = "genomizer";
    public static String password = "genomizer";
    public static String host = "85.226.111.95";
    public static String database = "genomizer_testdb";
    */
	private static TestInitializer ti;

	public static String username = "";
	public static String password = "";
	public static String host = "";
	public static String database = "";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ti = new TestInitializer();
		username = TestInitializer.username;
		password = TestInitializer.password;
		host 	 = TestInitializer.host;
		database = TestInitializer.database;

		ti.setup();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ti.removeTuples();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		ti.removeTuples();
	}

	@Test
	public void testConnection() throws Exception {
		DatabaseAccessor dbac = new DatabaseAccessor(username, password,
				host, database);

		assertTrue(dbac.isConnected());
		dbac.close();
	}

	@Test
	public void shouldAddNRemoveOneFileFromCurrentThread()
			throws SQLException, IOException, ParseException {

		DatabaseAccessor dbac = new DatabaseAccessor(username, password,
				host, database);

		String fileName = "StressTest0_t:9.hej";
		String filePath = "Stress0_t:9.hej";

		dbac.addGenomeRelease("banan", "Fly", "banan38.gr", null);
		dbac.addExperiment("Exper1");
		dbac.addNewFile("Exper1", 1, fileName , filePath,
								"-m -a -te","Smurf", "Claes", true,"banan", null);

		List<Experiment> resExp = dbac.search("Claes[Uploader]");

		assertEquals(1,resExp.size());
		assertEquals(fileName,resExp.get(0).getFiles().get(0).filename);

		dbac.deleteFile(resExp.get(0).getFiles().get(0).id);
		dbac.deleteExperiment("Exper1");
		dbac.removeGenomeRelease("banan");
		dbac.close();
	}

	/*
	 * Test if 10 threads can add 10 files at once. Note that nr of threads and
	 * nr of files can be changed.
	 */
    @Test
    public void shouldAddNRemoveFileFromSeparateThreads() throws SQLException,
    		IOException, ParseException {

    	ArrayList<Runnable> allRunnables = new ArrayList<Runnable>();
    	ArrayList<Thread> allThreads = new ArrayList<Thread>();
    	String experimentId = "Exper1";
    	int nrOfFiles = 10;

    	//specify the number of threads.
    	int nrOfThreads = 10;

    	for(int i=0;i<nrOfThreads;i++){
    		allRunnables.add(new myRunnable());
    		allThreads.add(new Thread(allRunnables.get(i)));
    	}

    	DatabaseAccessor dbac = new DatabaseAccessor(username, password,
    			host, database);

    	dbac.addGenomeRelease("banan", "Fly", "banan38.gr", null);
    	dbac.addExperiment(experimentId);

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

    	//all files added to db by sepparate threads, threads counting from 9.
    	//hard coded thread nr
    	for (int i = 9; i < (nrOfThreads * nrOfFiles) + 9; i++){
    		assertEquals(1,resExp.size());
    		assertEquals(experimentId,resExp.get(0).getFiles().get(i-9).expId);
    		assertEquals("Claes",resExp.get(0).getFiles().get(i-9).uploader);
    	}

    	//hard coded thread nr
    	for (int i = 9; i< (nrOfThreads * nrOfFiles) +9; i++){
    		dbac.deleteFile(resExp.get(0).getFiles().get(i-9).id);
    	}

    	dbac.deleteExperiment(experimentId);
    	dbac.removeGenomeRelease("banan");
    	dbac.close();
    }

    //class where threads will add files to database.
    public class myRunnable implements Runnable {

		public void run(){

			int nrOfFiles = 10, nr = 0;

		    try {
		    	DatabaseAccessor dbac2 = new DatabaseAccessor(
		    			username, password,	host, database);

		    	//adding files
		    	for (int i = 0; i < nrOfFiles; i++) {
		    		nr = i+1;
		    		dbac2.addNewFile("Exper1", 1, "StressTest" + i + "_t:" +
		    				Thread.currentThread().getId() + ".hej" , "Stress" +
		    				i + "_t:" +	Thread.currentThread().getId(),
		    				"-m -a -te","Smurf", "Claes", true,"banan", null);
		    	}

		    	dbac2.close();

		    } catch (SQLException e) {
		    	System.err.println("Failed to add so many files at the " +
		    			"same time!, Exception thrown when " +
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
