package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class GenomeRelease {

	public GenomeRelease() {
		// TODO Auto-generated constructor stub
	}

	static void sendGetGenomeRelease() throws Exception {

		URL obj = new URL(testSettings.url + "/genomeRelease");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", Login.getToken());


		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendGetGenomeReleaseSpecies(String specie) throws Exception {
		URL obj = new URL(testSettings.url + "/genomeRelease/" + specie);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", Login.getToken());


		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendAddGenomeRelease(String specie, String version) throws Exception {
		URL obj = new URL(testSettings.url + "/genomeRelease/");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", Login.getToken());

		JsonObject jj=new JsonObject();
		jj.addProperty("genomeVersion", version);
		jj.addProperty("specie", specie);

		JsonArray ja = new JsonArray();
		JsonPrimitive element = new JsonPrimitive("GR1338");
		JsonPrimitive element2 = new JsonPrimitive("GR1339");
		JsonPrimitive element3 = new JsonPrimitive("GR13310");
		ja.add(element);
		ja.add(element2);
		ja.add(element3);

		jj.add("files", ja);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + obj.toString());
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendDeleteGenomeReleaseSpecies(String specie, String version) throws Exception {
		URL obj = new URL(testSettings.url + "/genomeRelease/" + specie + "/" + version);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Authorization", Login.getToken());

		System.out.println("\nSending 'DELETE' request to URL : " + obj.toString());
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

}
