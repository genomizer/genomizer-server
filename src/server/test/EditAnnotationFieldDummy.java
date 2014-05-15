package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EditAnnotationFieldDummy {

	public static final int port = 7000;
	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;
	public static Token token = null;
	public static String expName = "huggaboy7";
	public static String filename = "spluttfile662693";

	public static void main(String args[]) throws Exception {
		sendLogin();
		sendRenameAnnotationField();
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

	private static void sendRenameAnnotationField() throws Exception {

		URL obj = new URL(url + "/annotation/field");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		JsonObject jj=new JsonObject();
		jj.addProperty("oldName", "field123");
		jj.addProperty("newName", "Field123");

		System.out.println(jj.toString());

		sendToServer(con, jj.toString());

		System.out.println("\nSending 'GET' request to URL : " + url);
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

	static void sendAddAnnotation() throws Exception {
		URL obj = new URL(url + "/annotation/field");
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
