package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UtkastTest {

	public static Token token = null;

	public static void main(String args[]) throws Exception {
		sendLogin();
		sendAddFile();
	}

	private static void sendLogin() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		//con.setRequestProperty("Authorization", UUID.randomUUID().toString());

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		//System.out.println(String.valueOf(jj.toString().getBytes().length));
//		con.setRequestProperty("Content-Length", String.valueOf(jj.toString().getBytes().length));

		//System.out.println(jj.toString());

		String json_output = jj.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);

		System.out.println("TOKEN: " + token.getToken());


	}


	private static void sendUpdateAnnotationPriveleges() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/sysadm/"+"username";

		URL obj = new URL(url);


		//Nytt test, update annotation priveleges
		HttpURLConnection con1=(HttpURLConnection) obj.openConnection();
		con1.setRequestMethod("PUT");
		con1.setRequestProperty("Authorization", "UUID");

		JsonObject jj1=new JsonObject();
		jj1.addProperty("pubmedId", 1);
		jj1.addProperty("type", 2);
		jj1.addProperty("specie", 1);
		jj1.addProperty("genoRelease", 1);
		jj1.addProperty("cellLine", 1);
		jj1.addProperty("devStage", 1);
		jj1.addProperty("sex", 3);
		jj1.addProperty("tissue", 3);

		System.out.println(String.valueOf(jj1.toString().getBytes().length));
		System.out.println(jj1.toString());

		String json_output = jj1.toString();

		con1.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con1.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

		int responseCode = con1.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);



	}

	private static void sendGetAnnotationPriveleges() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/sysadm/"+"username";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();


		//Nytt test,get annotation priveleges
		HttpURLConnection con1=(HttpURLConnection) obj.openConnection();
		con1.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "UUID");



		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		System.out.println("RESPONSE: " + response);


	}

	private static void sendDeleteAnnotation() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/annotation";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();


		//Nytt test, delete annotation
		HttpURLConnection con1=(HttpURLConnection) obj.openConnection();
		con1.setRequestMethod("DELETE");
		con1.setRequestProperty("Content-Type","application/json");


		JsonObject jj1=new JsonObject();


		System.out.println(String.valueOf(jj1.toString().getBytes().length));
		System.out.println(jj1.toString());

		String json_output = jj1.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		System.out.println("RESPONSE: " + response);


	}

	private static void sendProcessing() throws Exception {

		String username = "splutt";
		String filename = "filename12";
		String filepath = "path/to/local/file";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
							"\"param2\"," +
							"\"param3\"," +
							"\"param4\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";


		String url = "http://scratchy.cs.umu.se:7000/process";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("PUT");

		//add request header
		con.setRequestProperty("Authorization", "UUID");




		String json = "{" +
				"\"filename\": \"" + filename + "\"," +
				"\"filepath\": \"" + filepath + "\"," +
				"\"expid\": \"" + expid + "\"," +
				"\"processtype\": \"" + processtype + "\"," +
				"\"parameters\": [" + parameters + "]," +
				"\"metadata\": \"" + metadata + "\"," +
				"\"genomeRelease\": \"" + genomeRelease + "\"," +
				"\"author\": \"" + author + "\"}";

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();

		System.out.println(response.toString());


	}

	private static void sendAddFile() throws Exception {

		String url = "http://scratchy.cs.umu.se:7000/file";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Authorization",token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		//con.setRequestProperty("Authorization", UUID.randomUUID().toString());

		JsonObject jj=new JsonObject();
		jj.addProperty("experimentID", "idid");
		jj.addProperty("fileName", "name");
		jj.addProperty("size", "1.3gb");
		jj.addProperty("type", "raw");


		String json_output = jj.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();

		String response = responseBuffer.toString();
		System.out.println(response.toString());;
	}

}
