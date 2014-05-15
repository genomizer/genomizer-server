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
	public static String expName = "huggaboy7";
	public static String filename = "spluttfile662693";

	public static void main(String args[]) throws Exception {
		sendLogin();
//		sendGetAnnotationInformation();
//		sendAddAnnotation();
//		sendDeleteAnnotation();
//		sendAddExperiment();
//		sendAddFileToExperiment();
		sendProcessing();
//		sendDeleteExperiment();
		sendLogout();
	}



	private static void sendLogin() throws Exception {

		URL obj = new URL(url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		sendToServer(con, jj.toString());

		String response = printResponse(con);
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Body: " + response);

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);
		System.out.println(token.getToken());

	}

	private static void sendGetAnnotationInformation() throws Exception {

		URL obj = new URL(url + "/annotation");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Body: " + printResponse(con));
	}

	private static void sendProcessing() throws Exception {

		System.out.println("sendprocessing");
	/*	String username = "splutt";
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
		String author = "yuri";*/


		String username = "splutt";
		String filename = "filename1234";
		String fileid = "1";
		String expid = "Exp1";
		String processtype = "rawtoprofile";
		String parameters = "\"param1\"," +
				"\"param2\"," +
				"\"param3\"," +
				"\"param4\"," +
				"\"param5\"," +
				"\"param6\"," +
				"\"param7\"," +
				"\"param8\"";
		String metadata = "astringofmetadata";
		String genomeRelease = "hg38";
		String author = "yuri";


		URL obj = new URL(url + "/process");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Authorization", token.getToken());

		String json = "{" +
				"\"filename\": \"" + filename + "\"," +
				"\"fileId\": \"" + fileid + "\"," +
				"\"expid\": \"" + expName + "\"," +
				"\"processtype\": \"" + processtype + "\"," +
				"\"parameters\": [" + parameters + "]," +
				"\"metadata\": \"" + metadata + "\"," +
				"\"genomeRelease\": \"" + genomeRelease + "\"," +
				"\"author\": \"" + author + "\"}";


		sendToServer(con, json);
		/*con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json.getBytes());
		wr.flush();
		wr.close();*/

		//int responseCode = con.getResponseCode();
		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("Reponse Body: " + printResponse(con));
	}

	private static void sendAddExperiment() throws Exception {

		URL obj = new URL(url + "/experiment");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
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
		ja.add("annotations", annotations);
		sendToServer(con, ja.toString());

		int responseCode = con.getResponseCode();
		System.out.println("Response Body: " + printResponse(con));
	}

	private static void sendDeleteExperiment() throws Exception {

		URL obj = new URL(url + "/experiment/" + expName);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + url + "/experiment/" + expName);
		System.out.println("Response Body: " + printResponse(con));
	}

	private static void sendAddFileToExperiment() throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject ja=new JsonObject();
		ja.addProperty("experimentID", expName);
		ja.addProperty("fileName", "hugofiltest.txt");
		ja.addProperty("type", "raw");
		ja.addProperty("metaData", "not impl. yet");
		ja.addProperty("author", "Jonas M");
		ja.addProperty("uploader", "Jonas M");
		ja.addProperty("isPrivate", false);
		ja.addProperty("grVersion", "rn5");

		String json_output = ja.toString();
		System.out.println("JSON: " + json_output);
		sendToServer(con, json_output);

		System.out.println("Response Body: " + printResponse(con));
	}

	private static void sendDeleteAnnotation() throws Exception {
		URL obj = new URL(url + "/annotation/testanno221");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Body: " + printResponse(con));
	}

	private static void sendAddAnnotation() throws Exception {
		URL obj = new URL(url + "/annotation");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("name", "testanno1");
		JsonArray ja = new JsonArray();
		JsonPrimitive element = new JsonPrimitive("val1");
		ja.add(element);
		jj.add("type", ja);
		jj.addProperty("default", "val1");
		jj.addProperty("forced", false);

		sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Body: " + printResponse(con));

	}

	private static void sendLogout() throws Exception {
		URL obj = new URL(url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Body: " + printResponse(con));
	}

	private static String printResponse(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		System.out.println("Response Code: " + responseCode);
		return responseBuffer.toString();
	}

	private static void sendToServer(HttpURLConnection con, String json_output) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

	}

}
