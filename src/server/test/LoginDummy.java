package server.test;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;




public class LoginDummy {

	public static final int port = 7000;
	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;
	public static Token token = null;
	public static String expName = "huggaboy7";
	public static String filename = "spluttfile662693";

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

	private static void sendLogout() throws Exception {
		URL obj = new URL(url + "/login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", token.getToken());

		System.out.println("\nSending 'DELETE' request to URL : " + url);
		System.out.println("Response Body: " + printResponse(con));
	}
}
