package command;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import database.DatabaseAccessor;
import database.Genome;
import response.GetGenomeReleaseRespons;
import response.Response;
import response.StatusCode;
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
			ArrayList<Genome> genomeReleases=db.getAllGenomReleases();
			JsonArray arr = new JsonArray();
			
			
			for(int i =0; i<genomeReleases.size();i++){
				Gson gson = new GsonBuilder().create();
				JsonElement elem = gson.toJsonTree(genomeReleases.get(i), Genome.class);
				arr.add(elem);
			}
			
			String jsonOutput=arr.toString();
			return new GetGenomeReleaseRespons(StatusCode.OK, jsonOutput);

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
