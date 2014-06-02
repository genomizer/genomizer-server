package response;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import database.containers.Genome;

public class GetGenomeReleaseRespons extends Response {

	ArrayList<Genome> genomeReleases;
	JsonArray arr;

	public GetGenomeReleaseRespons(int code, ArrayList<Genome> genomeReleases) {
		this.code=code;
		this.genomeReleases=genomeReleases;



		arr = new JsonArray();
		Gson gson = new GsonBuilder().create();

		if(genomeReleases != null){
			for(int i =0; i<genomeReleases.size();i++){
				JsonElement elem = gson.toJsonTree(genomeReleases.get(i), Genome.class);
				arr.add(elem);
			}
		}
	}

	@Override
	public String getBody(){
		return arr.toString();
	}

}
