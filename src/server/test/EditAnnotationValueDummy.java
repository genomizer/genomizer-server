package server.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

import server.test.ClientDummy;
import server.test.EditAnnotationFieldDummy;

public class EditAnnotationValueDummy {

	public static final int port = 7000;


	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;
	public static Token token = null;

	public void main(String args[]) throws Exception {
		ClientDummy.sendLogin();
		EditAnnotationFieldDummy.sendAddAnnotation();
		sendAddAnnotationValue();
		sendRenameAnnotationValue();
		sendDeleteAnnotationValue();
		EditAnnotationFieldDummy.sendDeleteAnnotation();
	}

	static void sendAddAnnotationValue() throws IOException {
		URL obj = new URL(url + "/annotation/value");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("name", "testanno1");
		jj.addProperty("value", "testvalue1");

		ServerCmd.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Response Body: " + ServerCmd.printResponse(con));
	}

	static void sendRenameAnnotationValue() throws IOException {
		URL obj = new URL(url + "/annotation/value");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("name", "testanno1");
		jj.addProperty("oldValue", "testvalue1");
		jj.addProperty("newValue", "testvalue2");

		ServerCmd.sendToServer(con, jj.toString());

		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("Response Body: " + ServerCmd.printResponse(con));

	}

	static void sendDeleteAnnotationValue() throws IOException {
		URL obj = new URL(url + "/annotation/value/testanno1/testvalue2");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'PUT' request to URL : " + url);
		System.out.println("Response Body: " + ServerCmd.printResponse(con));
	}



}