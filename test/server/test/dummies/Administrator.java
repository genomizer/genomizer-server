package server.test.dummies;

import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;

public class Administrator {

	static void sendUpdateUserPrivileges(String username, String newprivileges) throws Exception {
		URL obj = new URL(testSettings.url + "/sysadm/usrpriv/" + username);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", Login.getToken());
		con.setRequestProperty("Content-Type", "application/json");
		JsonObject jj=new JsonObject();
		jj.addProperty("new_privileges", newprivileges);

		testSettings.sendToServer(con, jj.toString());

		System.out.println("\nSending 'POST' request to URL : " + testSettings.url);
		System.out.println("Response Body: " + testSettings.printResponse(con));
	}
}
