package server.test.dummies;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Administrator {

	static void sendGetAnnotationPrevileges(String name) throws Exception {
		/*URL obj = new URL(testSettings.url + "/annotation/field");
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

		System.out.println("\nSending 'POST' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + testSettings.printResponse(con));*/

	}
}
