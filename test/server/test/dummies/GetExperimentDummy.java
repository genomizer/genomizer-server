package server.test.dummies;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Ignore;

@Ignore
public class GetExperimentDummy {
	public static final int port = 7000;


	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";


	public static String url = "http://" + host + ":" + port;
	public static server.test.Token token = null;

	public static String expName = "huggaboy7";
	public static String filename = "spluttfile662693";

	public static void main(String args[]) throws Exception {

		sendLogin();
//		sendProcessing();
	}



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
		token = gson.fromJson(response, server.test.Token.class);
		System.out.println(token.getToken());

	}



//	private static void sendProcessing() throws Exception {
//
//		System.out.println("sendprocessing");
//	/*	String username = "splutt";
////		String filename = filename;
//		String fileid = "1";
////		String expid = "Exp1";
//		String processtype = "rawtoprofile";
//		String parameters = "\"param1\"," +
//							"\"param2\"," +
//							"\"param3\"," +
//							"\"param4\"";
//		String metadata = "astringofmetadata";
//		String genomeRelease = "hg38";
//		String author = "yuri";*/
//
//
//		String username = "splutt";
//		String filename = "filename1234";
//		String fileid = "1";
//		String expid = "Exp1";
//		String processtype = "rawtoprofile";
//		String parameters = "\"param1\"," +
//				"\"param2\"," +
//				"\"param3\"," +
//				"\"param4\"," +
//				"\"param5\"," +
//				"\"param6\"," +
//				"\"param7\"," +
//				"\"param8\"";
//		String metadata = "astringofmetadata";
//		String genomeRelease = "hg38";
//		String author = "yuri";
//
//
//		URL obj = new URL(url + "/process");
//		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//		con.setRequestMethod("PUT");
//		con.setRequestProperty("Authorization", token.getToken());
//
//		String json = "{" +
//				"\"filename\": \"" + filename + "\"," +
//				"\"fileId\": \"" + fileid + "\"," +
//				"\"expid\": \"" + expName + "\"," +
//				"\"processtype\": \"" + processtype + "\"," +
//				"\"parameters\": [" + parameters + "]," +
//				"\"metadata\": \"" + metadata + "\"," +
//				"\"genomeRelease\": \"" + genomeRelease + "\"," +
//				"\"author\": \"" + author + "\"}";
//
//
//		sendToServer(con, json);
//		/*con.setDoOutput(true);
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		wr.write(json.getBytes());
//		wr.flush();
//		wr.close();*/
//
//		//int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'PUT' request to URL : " + url);
//		System.out.println("Reponse Body: " + printResponse(con));
//	}




	private static String printResponse(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer responseBuffer = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responseBuffer.append(inputLine);
		}
		in.close();
		System.out.println("Response Code: " + responseCode);
		return responseBuffer.toString();
	}

	private static void sendToServer(HttpURLConnection con, String json_output) throws IOException {
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();

	}

}
