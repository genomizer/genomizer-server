package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Experiment {

	static void sendGetExperiment(String expName) throws Exception {

		URL obj = new URL(testSettings.url + "/experiment/" + expName);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());

		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendAddExperiment(String expName) throws Exception {

		URL obj = new URL(testSettings.url + "/experiment");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject ja=new JsonObject();
		JsonObject name=new JsonObject();
		ja.addProperty("name", expName);
//		JsonObject createdBy=new JsonObject();
//		ja.addProperty("createdBy", "jonas");
		JsonArray annotations = new JsonArray();
		JsonObject ann1=new JsonObject();
		ann1.addProperty("id", 1);
		ann1.addProperty("name", "Development Stage");
		ann1.addProperty("value", "aster");
		annotations.add(ann1);
		ja.add("annotations", annotations);
		testSettings.sendToServer(con, ja.toString());

		System.out.println("\nSending 'POST' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendDeleteExperiment(String expName) throws Exception {

		URL obj = new URL(testSettings.url + "/experiment/" + expName);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

}
