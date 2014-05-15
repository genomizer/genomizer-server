package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import database.DatabaseAccessor;
import response.Response;
import server.DatabaseSettings;

public class GetGenomeReleaseCommand extends Command{

	private String species;
	public GetGenomeReleaseCommand(String restful) {
		species=restful;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response execute() {
		DatabaseAccessor db=null;
		try {
			db = new DatabaseAccessor(DatabaseSettings.username, DatabaseSettings.password, DatabaseSettings.host, DatabaseSettings.database);
			List<String> genomeReleases=db.getAllGenomReleases(species);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				db.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

}
