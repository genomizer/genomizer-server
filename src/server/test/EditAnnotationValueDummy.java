package server.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import server.test.ClientDummy;
import server.test.EditAnnotationFieldDummy;

public class EditAnnotationValueDummy {

	public static final int port = 7000;


	public static String host = "localhost";
//	public static String host = "scratchy.cs.umu.se";
	public static String url = "http://" + host + ":" + port;
	public static Token token = null;

	public void main(String args[]) throws Exception {
		ClientDummy.sendLogin();
		EditAnnotationFieldDummy.sendAddAnnotation();




		ServerCmd.printResponse();


	}

}