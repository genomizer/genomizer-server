package response;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import database.containers.Genome;

/**
 * Class which represents the response for get genome release.
 *
 * @author
 * @version 1.0
 */
public class GetGenomeReleaseResponse extends Response {

	private ArrayList<Genome> genomeReleases;
	private JsonArray arr;


	/**
	 * Creator for the response.
	 * @param code The return code for the response.
	 * @param genomeReleases A list containing the genome releases which
	 *                       are returned.
	 */
	public GetGenomeReleaseResponse(int code, ArrayList<Genome> genomeReleases) {

		this.code=code;
		this.genomeReleases=genomeReleases;

		arr = new JsonArray();
		Gson gson = new GsonBuilder().create();

		if(genomeReleases != null){
			for (Genome genome : genomeReleases) {
				JsonElement elem = gson.toJsonTree(genome, Genome.class);
				arr.add(elem);
			}
		}
	}

	/**
	 * Getter for the response information and return code.
	 * @return The return code followed by the response information as a String
	 */
	@Override
	public String getBody(){

		return arr.toString();
	}

}
