package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Genome {

	public final String version;
	public final String species;
	public final String filePath;
	public final String fileName;

	public Genome(ResultSet resSet) throws SQLException {
		version = resSet.getString("Version");
		species = resSet.getString("Species");
		filePath = resSet.getString("FilePath");
		fileName = filePath.substring(filePath.lastIndexOf('/'));
	}
}
