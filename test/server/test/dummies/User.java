package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

public class User {

	static void sendCreateUser(String username, String password, String privileges, String fullname, String email) throws Exception {
		URL obj = new URL(testSettings.url + "/user");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		JsonObject jj=new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);
		jj.addProperty("privileges", privileges);
		jj.addProperty("name", fullname);
		jj.addProperty("email", email);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + testSettings.printResponse(con));

	}

	static void sendDeleteUser(String username) throws Exception {
		URL obj = new URL(testSettings.url + "/user/" + username);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");
//		JsonObject jj=new JsonObject();

		//testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + testSettings.printResponse(con));

	}
}
