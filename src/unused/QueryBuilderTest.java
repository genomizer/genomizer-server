package unused;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderTest {

	private QueryBuilder builder;
	private Connection con;

	private static final String dbConString =
									"jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	@Before
	public void setUp() {
		builder = new QueryBuilder();

		try{
			con = DriverManager.getConnection(
						dbConString, dbUser, dbPass);
		}
		catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
	}

	@After
	public void tearDown(){
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Failed to close the connection!!\n");
		}
	}


	@Test
	public void testCreateQueryBuilder() {
		assertNotNull(builder);
	}

	@Test
	public void testAddExperiment() {
		String anno = "awe1123(ExpID) AND human(Species) AND M(Sex) " +
					"AND arm(CellType) AND early(DevStage) AND cool(AntiName) "+
					"AND C(AntiSymbol) AND untzz(Antibody)";



		builder.addExperiment(con, anno);



	}

}
