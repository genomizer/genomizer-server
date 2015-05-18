package database.test.unittests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import database.containers.FileTuple;
import org.junit.*;

import database.DatabaseAccessor;
import database.containers.Experiment;
import database.test.TestInitializer;
import server.ServerSettings;

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
	private static DatabaseAccessor dbac;


	public static String username = "";
	public static String password = "";
	public static String host = "";
	public static String database = "";



	private static final String TESTFOLDER = "resources/conversionTestData/";
	private static final String OUTPUTFOLDER = "output/";
	private static String userDir, tempFileLocation;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ti = new TestInitializer();
		username = TestInitializer.username;
		password = TestInitializer.password;
		host = TestInitializer.host;
		database = TestInitializer.database;
		dbac = ti.setup();
		userDir = new File("").getAbsolutePath();
		tempFileLocation = ServerSettings.fileLocation;
		ServerSettings.fileLocation = userDir+File.separator+TESTFOLDER+
				OUTPUTFOLDER;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ti.removeTuples();
		ServerSettings.fileLocation = tempFileLocation;
	}

	@After
	public void tearDown() throws Exception {
		ti.removeTuples();
	}

	@Ignore("This is tested elsewhere") @Test
	public void shouldaddNRemoveOneFileFromCurrentThread()
			throws SQLException, IOException, ParseException {
		String fileName = "StressTest0_t9.hej";
		String filePath = "Stress0_t9.hej";

		dbac.addGenomeRelease("banan", "Fly", "banan38.gr", null);
		dbac.addExperiment("Exper1");
		FileTuple ft = dbac.addNewFile("Exper1", FileTuple.RAW, fileName, filePath,
				"-m -a -te", "Smurf", "Claes", true, "banan", null);
		dbac.markReadyForDownload(ft);

		List<Experiment> resExp = dbac.search("Claes[Uploader]");

		assertEquals(1, resExp.size());
		assertEquals(fileName,resExp.get(0).getFiles().get(0).filename);

		dbac.deleteFile(resExp.get(0).getFiles().get(0).id);
		dbac.deleteExperiment("Exper1");
		dbac.removeGenomeRelease("banan");
		dbac.close();
	}

	@Test
	public void shouldAddOnDifferentThreads() throws Exception {
		ArrayList<Thread> allThreads = new ArrayList<Thread>();
		//dbac.addGenomeRelease("banan", "Fly", "banan38.gr", null);

		String experimentId = "Exper1";
		dbac.addExperiment(experimentId);
		Experiment exp = dbac.search("Exper1[ExpId]").get(0);

		int nrOfThreads = 10;

		for(int i = 0; i < nrOfThreads; i++) {
			Runnable r = new myRunnable();
			Thread t = new Thread(r);
			allThreads.add(t);
		}

		for (Thread t : allThreads) {
			t.start();
		}


		try {
			// wait some time to be sure that threads are done.
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		exp = dbac.search("Exper1[ExpId]").get(0);

		ArrayList<FileTuple> tuples = exp.getFiles();
		assertEquals(100, tuples.size());

	}

	/*
	 * Test if 10 threads can add 10 files at once. Note that nr of threads and
	 * nr of files can be changed.
	 */
    @Ignore("temporary ignore") @Test
    public void shouldAddNRemoveFileFromSeparateThreads() throws SQLException,
    		IOException, ParseException {

		ArrayList<Thread> allThreads = new ArrayList<Thread>();
		//dbac.addGenomeRelease("banan", "Fly", "banan38.gr", null);

    	String experimentId = "Exper1";
		dbac.addExperiment(experimentId);
		Experiment exp = dbac.search("Exper1[ExpId]").get(0);

    	int nrOfFiles = 10;
    	int nrOfThreads = 10;

		for(int i = 0; i < nrOfThreads; i++) {
			Runnable r = new myRunnable();
			Thread t = new Thread(r);
			allThreads.add(t);
		}

		for (Thread t : allThreads) {
			t.start();
		}


    	try {
    		// wait some time to be sure that threads are done.
    		Thread.sleep(2000);
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}


		exp = dbac.search("Exper1[ExpId]").get(0);

		ArrayList<FileTuple> tuples = exp.getFiles();
		assertEquals(0, tuples.size());

    	//all files added to db by sepparate threads, threads counting from 9.
    	//hard coded thread nr
    	for (int i = 0; i < (nrOfThreads * nrOfFiles); i++){
    		assertEquals(experimentId, exp.getFiles().get(i).expId);
    		assertEquals("Claes", exp.getFiles().get(i).uploader);
    	}

    	//hard coded thread nr
    	for (int i = 0; i< (nrOfThreads * nrOfFiles); i++){
    		dbac.deleteFile(exp.getFiles().get(i).id);
    	}

    	dbac.deleteExperiment(experimentId);
    	dbac.removeGenomeRelease("banan");
    	dbac.close();
    }

    //class where threads will add files to database.
    public class myRunnable implements Runnable {

		public void run(){

			try {

					String expID = "Exper1";
					String inputFile = null;
					String metaData = null;
					String author = "Gammelsmurf";
					String uploader = "Claes";
					boolean isPrivate = true;
					String genomeRelease = null;
					String checkSumMD5 = null;
					DatabaseAccessor dbac2 = new DatabaseAccessor(
							ti.username, ti.password, ti.host, ti.database);
				for (int i = 0; i < 10; i++) {
					String fileName = "StressTest" + i + "_t" +
							Thread.currentThread().getId() + ".txt";
					FileTuple ft = dbac2.addNewFile(expID, FileTuple.PROFILE, fileName, inputFile, metaData, author, uploader, isPrivate, genomeRelease, checkSumMD5);
					dbac2.markReadyForDownload(ft);
					System.out.println("Thread "+Thread.currentThread().getId()+" added #"+i);
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			int nrOfFiles = 10, nr = 0;
/*
		    try {
				DatabaseAccessor dbac2 = new DatabaseAccessor(
						ti.username, ti.password, ti.host, ti.database);

		    	//adding files
		    	for (int i = 0; i < nrOfFiles; i++) {
		    		nr = i+1;
//		    		dbac2.addNewFile("Exper1", 1, "StressTest" + i + "_t" +
//		    				Thread.currentThread().getId() + ".hej" , "Stress" +
//		    				i + "_t" +	Thread.currentThread().getId(),
//		    				"-m -a -te","Smurf", "Claes", true,"banan", null);

					String expID = "Exper1";
					String fileName = "StressTest" + i + "_t" +
							Thread.currentThread().getId() + ".txt";
					String inputFile = null;
					String metaData = null;
					String author = "Gammelsmurf";
					String uploader = "Claes";
					boolean isPrivate = true;
					String genomeRelease = null;
					String checkSumMD5 = null;

					FileTuple ft = dbac2.addNewFile(expID, FileTuple.RAW, fileName, inputFile,
							metaData, author, uploader, isPrivate, genomeRelease, checkSumMD5);
					dbac2.markReadyForDownload(ft);
		    	}

		    	dbac2.close();

		    } catch (SQLException e) {
		    	//System.err.println("Failed to add so many files at the " +
		    	//		"same time!, Exception thrown when " +
				//		"adding the " + nr + " file.");
		    	//e.printStackTrace();
				System.err.println("SQLException");
		    	//fail();
		    } catch (IOException e) {
				//System.err.println("A MEN DURÅ");
				System.err.println("IOException: " + e.getMessage());
				e.printStackTrace();

				//fail();
			} catch (Exception e) {
				System.err.println("Another Exception: " + e.getCause());
				e.printStackTrace();
				//fail();
			}
			*/
		}
	}
}