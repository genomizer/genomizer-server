package testSuite.unitTests;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import database.DatabaseAccessor;

public class TestChainFiles {

	private static DatabaseAccessor dbac;

	@BeforeClass
	public static void setUp(){
		try {
			dbac = new DatabaseAccessor("c5dv151_vt14", "shielohh", "postgres", "c5dv151_vt14");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown(){

	}

	@AfterClass
	public static void after(){
		try {
			dbac.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Test
    public void shouldAddandGetAndRemoveChainFile () throws SQLException {
    	addChain_file();
    	removeChainFile();
	}




    public void addChain_file() throws SQLException {

    	String fromVersion = "hg18";
    	String toVersion = "hg38";
    	String fileName = "chainHuman";
    	String filePath = "";

    	filePath = dbac.addChainFile(fromVersion, toVersion, fileName);

    	System.out.println(filePath);
    	assertEquals("http://scratchy.cs.umu.se:8000/upload.php?path=/var/www/data/genome_releases/Human/chain_files/chainHuman",filePath);


    }

    public void removeChainFile() throws SQLException {
    	String fromVersion = "hg18";
    	String toVersion = "hg38";

    	assertEquals(1,dbac.removeChainFile(fromVersion, toVersion));

    }

    public void getChainFIle() throws SQLException {
    	String fromVersion = "hg18";
    	String toVersion = "hg38";

    	dbac.getChainFile(fromVersion, toVersion);
    }
}