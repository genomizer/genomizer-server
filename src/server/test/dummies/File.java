package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

public class File {

	private static void sendAddFileToExperiment(String expName) throws Exception {

		URL obj = new URL(testSettings.url + "/file");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject ja=new JsonObject();
		ja.addProperty("experimentID", expName);
		ja.addProperty("fileName", "hugofiltest.txt");
		ja.addProperty("type", "raw");
		ja.addProperty("metaData", "not impl. yet");
		ja.addProperty("author", "Jonas M");
		ja.addProperty("uploader", "Jonas M");
		ja.addProperty("isPrivate", false);
		ja.addProperty("grVersion", "rn5");

		String json_output = ja.toString();
		System.out.println("JSON: " + json_output);
		testSettings.sendToServer(con, json_output);

		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

}
