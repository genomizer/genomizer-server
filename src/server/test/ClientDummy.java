package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.*;

public class ClientDummy {

	public static final int port = 7000;


	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";


	public static String url = "http://" + host + ":" + port;
	public static Token token = null;

	public static String expName = "spluttexp6669";
	public static String filename = "spluttfile6669";


	public static void main(String args[]) throws Exception {

		sendLogin();
		//sendGetAnnotationInformation();
		//sendAddFileToExperiment();
		sendAddExperiment();
//		sendAddFileToExperiment();
//		sendProcessing();
		sendAddFileToExperiment();
//		sendDeleteExperiment();
//		sendProcessing();
		//sendLogout();
	}



	private static void sendLogin() throws Exception {

		String url = "http://" + host + ":" + port + "/login";
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

		System.out.println(String.valueOf(jj.toString().getBytes().length));
//		con.setRequestProperty("Content-Length", String.valueOf(jj.toString().getBytes().length));

		System.out.println(jj.toString());

		String json_output = jj.toString();

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

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);
	}

	private static void sendGetAnnotationInformation() throws Exception {

		String url = "http://" + host + ":" + port + "/annotation";
//		String url = "http://scratchy.cs.umu.se:"+port+"/annotation";
//		String url = "http://localhost:"+ port +"/annotation";


		URL obj = new URL(url + "/annotation");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
//		con.setRequestProperty("Content-Length", String.valueOf(jj.toString().getBytes().length));

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

		System.out.println("sendprocessing");
		String username = "splutt";
//		String filename = filename;
		String fileid = "1";
//		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
							"\"param2\"," +
							"\"param3\"," +
							"\"param4\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";


//		String url = "http://localhost:"+ port +"/process";
//		String url = "http://scratchy.cs.umu.se:7000/process";
		String url = "http://" + host + ":" + port + "/process";
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		System.out.println("sendprocessing2");
		// optional default is GET
		con.setRequestMethod("PUT");

		//add request header
		System.out.println("Sending token: " + token.getToken());
		con.setRequestProperty("Authorization", token.getToken());


		System.out.println("sendprocessing3");

		String json = "{" +
				"\"filename\": \"" + filename + "\"," +
				"\"fileId\": \"" + fileid + "\"," +
				"\"expid\": \"" + expName + "\"," +
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
		System.out.println("sendprocessing4");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		System.out.println("sendprocessing5");


		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		System.out.println("sendprocessing6");

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();


		System.out.println("sendprocessing7");
		String response = responseBuffer.toString();

		System.out.println(response.toString());


	}

	private static void sendAddExperiment() throws Exception {

//		String url = "http://scratchy.cs.umu.se:"+port+"/experiment";
//		String url = "http://localhost:"+ port +"/experiment";
		String url = "http://" + host + ":" + port + "/experiment";


		URL obj = new URL(url + "/experiment");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		JsonObject ja=new JsonObject();
		JsonObject name=new JsonObject();
		ja.addProperty("name", expName);

		JsonObject createdBy=new JsonObject();
		ja.addProperty("createdBy", "jonas");

		JsonArray annotations = new JsonArray();

		JsonObject ann1=new JsonObject();
		ann1.addProperty("id", 1);
		ann1.addProperty("name", "Development Stage");
		ann1.addProperty("value", "aster");
		annotations.add(ann1);

		//JsonObject ann2=new JsonObject();
		//ann2.addProperty("id", 2);
		//ann2.addProperty("name", "type");
		//ann2.addProperty("value", "raw");
		//annotations.add(ann2);

//		JsonObject ann3=new JsonObject();
//		ann3.addProperty("id", 3);
//		ann3.addProperty("name", "specie");
//		ann3.addProperty("value", "human");
//		annotations.add(ann3);
//
//
//		JsonObject ann4=new JsonObject();
//		ann4.addProperty("id", 4);
//		ann4.addProperty("name", "genome release");
//		ann4.addProperty("value", "v.123");
//		annotations.add(ann4);
//
//
//		JsonObject ann5=new JsonObject();
//		ann5.addProperty("id", 5);
//		ann5.addProperty("name", "cell line");
//		ann5.addProperty("value", "yes");
//		annotations.add(ann5);
//
//		JsonObject ann6=new JsonObject();
//		ann6.addProperty("id", 6);
//		ann6.addProperty("name", "development stage");
//		ann6.addProperty("value", "larva");
//		annotations.add(ann6);
//
//		JsonObject ann7=new JsonObject();
//		ann7.addProperty("id", 7);
//		ann7.addProperty("name", "sex");
//		ann7.addProperty("value", "male");
//		annotations.add(ann7);
//
//		JsonObject ann8=new JsonObject();
//		ann8.addProperty("id", 8);
//		ann8.addProperty("name", "tissue");
//		ann8.addProperty("value", "eye");
//		annotations.add(ann8);

		ja.add("annotations", annotations);


		String json_output = ja.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		System.out.println("RESPONSE CODE: " + responseCode);

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

	private static void sendDeleteExperiment() throws Exception {

		URL obj = new URL(url + "/experiment/" + expName);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("DELETE");

		//add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url + "/experiment/" + expName);
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

	private static void sendAddFileToExperiment() throws Exception {


//		String url = "http://scratchy.cs.umu.se:"+port+"/file";
//		String url = "http://localhost:"+ port +"/file";
		String url = "http://" + host + ":" + port + "/file";
		System.out.println("\nSending Add File To Experiment.");

		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		//con.setRequestProperty("Authorization", UUID.randomUUID().toString());
		JsonObject ja=new JsonObject();

		ja.addProperty("experimentID", expName);
		ja.addProperty("fileName", "hugofiltest.txt");
		ja.addProperty("size", "1mb");
		ja.addProperty("type", "raw");
		ja.addProperty("fileType", "unknown");
		ja.addProperty("metaData", "not impl. yet");
		ja.addProperty("author", "Jonas M");
		ja.addProperty("uploader", "Jonas M");
		ja.addProperty("isPrivate", "false");
		ja.addProperty("grVersion", "rn5");

		String json_output = ja.toString();
		System.out.println("JSON: " + json_output);


		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		System.out.println("RESPONSE CODE: " + responseCode);

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

	private static void sendLogout() throws Exception {
		//String url = "http://scratchy.cs.umu.se:"+port+"/login";
//		String url = "http://localhost:"+ port +"/login";
		String url = "http://" + host + ":" + port + "/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("DELETE");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
//		con.setRequestProperty("Content-Length", String.valueOf(jj.toString().getBytes().length));

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'DELETE' request to URL : " + url);
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
}
