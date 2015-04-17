package response;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import database.containers.Genome;

public class GetGenomeReleaseResponse extends Response {

	ArrayList<Genome> genomeReleases;
	JsonArray arr;

	public GetGenomeReleaseResponse(int code, ArrayList<Genome> genomeReleases) {
		this.code=code;
		this.genomeReleases=genomeReleases;



		arr = new JsonArray();
		Gson gson = new GsonBuilder().create();

		if(genomeReleases != null){
			for (Genome genomeRelease : genomeReleases) {
				JsonElement elem = gson.toJsonTree(genomeRelease, Genome.class);
				arr.add(elem);
			}
		}
	}

	@Override
	public String getBody(){
		return arr.toString();
	}

}
