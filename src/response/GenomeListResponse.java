package response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import database.containers.Genome;

import java.util.ArrayList;

/**
 * Class which represents the response for get genome release.
 *
 * @author Business Logic
 * @version 1.0
 */
public class GenomeListResponse extends Response {

	ArrayList<Genome> genomeReleases;
	private JsonArray arr;


	/**
	 * Creator for the response.
	 * @param code The return code for the response.
	 * @param genomeReleases A list containing the genome releases which
	 *                       are returned.
	 */
	public GenomeListResponse(int code, ArrayList<Genome> genomeReleases) {

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
	 * Creates a Json representation of the body
	 * @return The response body as a String
	 */
	@Override
	public String getBody(){

		return arr.toString();
	}

}
