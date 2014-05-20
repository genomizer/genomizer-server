package server.test.dummies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Annotations {

	static Token token;

	static void sendGetAnnotationInformation() throws Exception {

		URL obj = new URL(testSettings.url + "/annotation");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());

		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Body: " +testSettings. printResponse(con));
	}

	static void sendAddAnnotation(String name) throws Exception {
		URL obj = new URL(testSettings.url + "/annotation/field");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		JsonObject jj=new JsonObject();
		jj.addProperty("name", name);
		JsonArray ja = new JsonArray();
		JsonPrimitive element = new JsonPrimitive("val1");
		ja.add(element);
		jj.add("type", ja);
		jj.addProperty("default", "val1");
		jj.addProperty("forced", false);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));

	}

	static void sendRenameAnnotationField(String oldName, String newName) throws Exception {

		URL obj = new URL(testSettings.url + "/annotation/field");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());

		JsonObject jj=new JsonObject();
		jj.addProperty("oldName", oldName);
		jj.addProperty("newName", newName);

		System.out.println(jj.toString());

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'PUT' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendDeleteAnnotation(String name) throws Exception {
		URL obj = new URL(testSettings.url + "/annotation/field/" + URLEncoder.encode(name, "UTF-8"));
		//URL obj = new URL(testSettings.url + "/annotation/field/" + name);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();


		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendAddAnnotationValue(String name, String value) throws IOException {
		URL obj = new URL(testSettings.url + "/annotation/value");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("name", name);
		jj.addProperty("value", value);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendRenameAnnotationValue(String name, String oldValue, String newValue) throws IOException {
		URL obj = new URL(testSettings.url + "/annotation/value");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("name", name);
		jj.addProperty("oldValue", oldValue);
		jj.addProperty("newValue", newValue);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'PUT' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendDeleteAnnotationValue(String name, String value) throws IOException {
		URL obj = new URL(testSettings.url + "/annotation/value/" + URLEncoder.encode(name, "UTF-8") + "/" + URLEncoder.encode(value, "UTF-8"));
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}
}
