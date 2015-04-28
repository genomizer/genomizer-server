package transfer;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import server.ServerSettings;

public class HTTPURLUpload {

	private String filePath;
	private String uploadPath;

	public HTTPURLUpload(String filePath, String uploadPath) {
		this.filePath = filePath;
		this.uploadPath = uploadPath;
	}

	public void sendFile() {
		// the URL where the file will be posted
		String postReceiverUrl = "";//ServerSettings.webUrlUpload;

		// new HttpClient
		HttpClientBuilder hcBuilder = HttpClients.custom();

		CloseableHttpClient httpClient = hcBuilder.build();

		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("pvt:pvt"));
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setCredentialsProvider(credentialsProvider);



		// post header
		HttpPost httpPost = new HttpPost(postReceiverUrl);

		File file = new File(filePath);

		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		//add the location on the server where the file should be saved
		reqEntity.addTextBody("data", uploadPath);

		reqEntity.addBinaryBody("uploadfile", file);
		httpPost.setEntity(reqEntity.build());

		// execute HTTP post request


		HttpResponse response;
		try {
			response = httpClient.execute(httpPost, localContext);
			HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {

			    String responseStr = EntityUtils.toString(resEntity).trim();
			    System.out.println("Response: " +  responseStr);

			    // you can add an if statement here and do other actions based on the response
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HTTPURLUpload uploader = new HTTPURLUpload("/home/walkma/Downloads/test1.txt", "/var/www/walkma.no-ip.info/results/test111.txt");
		uploader.sendFile();
	}

}
