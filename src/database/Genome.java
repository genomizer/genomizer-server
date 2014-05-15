package database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Genome {

	public final String genomeVersion;
	public final String specie;
	public final String path;
	public final String fileName;

	public Genome(ResultSet resSet) throws SQLException {
		genomeVersion = resSet.getString("Version");
		specie = resSet.getString("Species");
		path = resSet.getString("FilePath");
		fileName = path.substring(path.lastIndexOf('/')+1);
	}
}
