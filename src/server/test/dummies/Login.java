package server.test.dummies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import server.test.dummies.Token;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Login {

	public static Token token = null;

	static void login() throws IOException {
		URL obj = new URL(testSettings.url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "test");
		jj.addProperty("password", "losenord");

		testSettings.sendToServer(con, jj.toString());
		String response = testSettings.printResponse(con);

		System.out.println("\nSending 'GET' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + response);

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);
		System.out.println(token.getToken());
	}

	static void logout() throws IOException {
		URL obj = new URL(testSettings.url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		System.out.println("\nSending 'DELETE' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static String getToken() {
		return token.toString();
	}

}
