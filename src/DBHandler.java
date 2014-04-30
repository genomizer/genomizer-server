import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The handler classsss
 *
 * @author dv11ann
 *
 */
public class DBHandler {

	private static final String dbDriver = "org.postgresql.Driver";

	private static final String dbConString = "jdbc:postgresql://postgres/c5dv151_vt14" ;
	private static final String dbUser = "c5dv151_vt14";
	private static final String dbPass = "shielohh";

	private Connection dbCon=null;

	public DBHandler() {}

	/**
	 * Method to upload a file
	 *
	 * @param String, the ExpID to know which experiment to upload the file to
	 * @param Something, the object wuith the file information
	 *
	 */
	public void uploadFile() {
		openConnection();



		closeConnection();
	}

	/**
	 * Method to search for a file
	 *
	 * @param Object with file annotations
	 *
	 */
	public void searchFile() {

	}

	/**
	 * Method to add annotation
	 *
	 * @param the new annotation
	 *
	 */
	public void addAnnotation() {

	}

	/**
	 * Method to remove an annoatation
	 *
	 * @param parameter to identify the annotation
	 *
	 */
	public void removeAnnotation() {

	}

	/**
	 * private method to open a connection to the database
	 *
	 */
	private void openConnection() {

		dbCon=null;

		try {
			Class.forName(dbDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			dbCon = DriverManager.getConnection(
					dbConString, dbUser, dbPass);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * private method to close the connection to the database.
	 *
	 */
	public void closeConnection() {
		try {
			dbCon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbCon = null;
	}

}
