package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import org.junit.Ignore;

@Ignore
public class File {

	static void sendAddFileToExperiment(String expName) throws Exception {

		URL obj = new URL(testSettings.url + "/file");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		JsonObject ja=new JsonObject();
		ja.addProperty("experimentID", expName);
		ja.addProperty("fileName", "hugofiltest2.txt");
		ja.addProperty("type", "raw");
		ja.addProperty("metaData", "not impl. yet");
		ja.addProperty("author", "Jonas M");
		ja.addProperty("uploader", "Jonas M");
		ja.addProperty("isPrivate", false);
		ja.addProperty("grVersion", "fb5");

		String json_output = ja.toString();
		testSettings.sendToServer(con, json_output);

		System.out.println("\nSending 'POST' request to URL : " + testSettings.url + "/file");
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}

	static void sendDeleteFileFromExperiment(String fileId) throws Exception {

		URL obj = new URL(testSettings.url + "/file" + "/" + fileId);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("DELETE");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");

		System.out.println("\nSending 'DELETE' request to URL : " + testSettings.url + "/file");
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}
}
