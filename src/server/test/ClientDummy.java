package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.*;

public class ClientDummy {

	public static Token token = null;
	public static final int port = 7000;

	public static void main(String args[]) throws Exception {
		sendLogin();
		sendGetAnnotationInformation();
		sendAddFileToExperiment();
		//sendAddExperiment();
		sendLogout();
	}



	private static void sendLogin() throws Exception {

		String url = "http://scratchy.cs.umu.se:"+ port +"/login";

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

		System.out.println("TOKEN: " + token.getToken());


	}

	private static void sendGetAnnotationInformation() throws Exception {
		String url = "http://scratchy.cs.umu.se:"+port+"/annotation";

		URL obj = new URL(url);
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

	private static void sendAddExperiment() throws Exception {

		String url = "http://scratchy.cs.umu.se:"+port+"/experiment";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		//con.setRequestProperty("Authorization", UUID.randomUUID().toString());
		JsonObject ja=new JsonObject();
//		JsonObject name=new JsonObject();
		ja.addProperty("name", "Jonas_Experiment1");
//		ja.add(name);


//		JsonObject createdBy=new JsonObject();
		ja.addProperty("createdBy", "jonas");
//		ja.add(createdBy);

		JsonArray annotations = new JsonArray();

		JsonObject ann1=new JsonObject();
		//ann1.addProperty("id", 1);
		ann1.addProperty("name", "pubmedId");
		ann1.addProperty("value", "123");
		annotations.add(ann1);

		//JsonObject ann2=new JsonObject();
		//ann2.addProperty("id", 2);
		//ann2.addProperty("name", "type");
		//ann2.addProperty("value", "raw");
		//annotations.add(ann2);

		JsonObject ann3=new JsonObject();
		//ann3.addProperty("id", 3);
		ann3.addProperty("name", "specie");
		ann3.addProperty("value", "human");
		annotations.add(ann3);


		JsonObject ann4=new JsonObject();
		//ann4.addProperty("id", 4);
		ann4.addProperty("name", "genome release");
		ann4.addProperty("value", "v.123");
		annotations.add(ann4);


		JsonObject ann5=new JsonObject();
		//ann5.addProperty("id", 5);
		ann5.addProperty("name", "cell line");
		ann5.addProperty("value", "yes");
		annotations.add(ann5);

		JsonObject ann6=new JsonObject();
		//ann6.addProperty("id", 6);
		ann6.addProperty("name", "development stage");
		ann6.addProperty("value", "larva");
		annotations.add(ann6);

		JsonObject ann7=new JsonObject();
		//ann7.addProperty("id", 7);
		ann7.addProperty("name", "sex");
		ann7.addProperty("value", "male");
		annotations.add(ann7);

		JsonObject ann8=new JsonObject();
		//ann8.addProperty("id", 8);
		ann8.addProperty("name", "tissue");
		ann8.addProperty("value", "eye");
		annotations.add(ann8);

		ja.add("annotations", annotations);


		String json_output = ja.toString();
		//System.out.println(toPrettyFormat(json_output));

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

	private static void sendAddFileToExperiment() throws Exception {

		String url = "http://scratchy.cs.umu.se:"+port+"/file";
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

		ja.addProperty("experimentID", "Exp1");
		ja.addProperty("fileName", "test1234567.txt");
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
		String url = "http://scratchy.cs.umu.se:"+port+"/login";

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