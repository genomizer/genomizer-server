package server.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.*;

public class ClientDummy {

	public static void main(String args[]) throws Exception {
		sendLogin();
	}

	private static void sendLogin() throws Exception {

		String url = "http://localhost:8080/login";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("POST");

		//add request header
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", UUID.randomUUID().toString());

		JsonObject jj=new JsonObject();
		jj.addProperty("username", "jonas");
		jj.addProperty("password", "losenord");

		System.out.println(String.valueOf(jj.toString().getBytes().length));
//		con.setRequestProperty("Content-Length", String.valueOf(jj.toString().getBytes().length));

		System.out.println(jj.toString());

		String json_output = jj.toString();

		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.write(json_output.getBytes());
		wr.flush();
		wr.close();


		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
}