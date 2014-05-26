package server.test.dummies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import server.ResponseLogger;
import server.test.dummies.Token;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Login {

	public static Token token = null;

	static void login(String username, String password) throws IOException {
		URL obj = new URL(testSettings.url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject jj=new JsonObject();
		jj.addProperty("username", username);
		jj.addProperty("password", password);

		testSettings.sendToServer(con, jj.toString());
		String response = null;
		try {
			response = testSettings.printResponse(con);
		}catch(Exception e) {
			e.printStackTrace();
		}

		System.out.println("\nSending 'GET' request to URL : " + testSettings.url + "/login");
		System.out.println("Response Body: " + response);

		Gson gson = new Gson();
		token = gson.fromJson(response, Token.class);
		//System.out.println(token.getToken());
	}

	static void logout() throws IOException {
		URL obj = new URL(testSettings.url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());
		System.out.println(token.getToken());
		System.out.println("\nSending 'DELETE' request to URL : " + testSettings.url + "/logout");
		System.out.println("Response Body: " + testSettings.printResponse(con));

	}

	static void sendTokenValidation() throws IOException {
		URL obj = new URL(testSettings.url + "/token");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken() + "h2hj");
		System.out.println(token.getToken());
		System.out.println("\nSending 'GET' request to URL : " + obj.toString());
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static String getToken() {
		return token.getToken();
	}

}
