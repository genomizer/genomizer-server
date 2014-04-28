package JUnitTests;

import static org.junit.Assert.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseQueryTests {

	Connection connection = null;

	@Before
	public void setup(){

		try{
			Class.forName("org.postgresql.Driver");
		}
		catch(ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;

		}

		try{
		connection = DriverManager.getConnection(
					 "jdbc:postgresql://postgresql/c5dv151_vt14",
					 "c5dv151_vt14", "shielohh");
		}
		catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;

		}
	}



	@Test
	public void testStartUpConnectionDatabase() {



	}

	@After
	public void tearDown(){

		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Failed to close the connection!!\n");
		}
	}

}
