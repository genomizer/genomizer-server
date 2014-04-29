package transfer;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HTTPURLUpload {

	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// the file to be posted
		String textFile = "/home/walkma/Downloads/test1.txt";
		System.out.println("textFile: " + textFile);
		 
		// the URL where the file will be posted
		String postReceiverUrl = "http://130.239.178.22/cgi-bin/upload.php";
		System.out.println("postURL: " + postReceiverUrl);
		 
		// new HttpClient
		HttpClient httpClient = HttpClientBuilder.create().build();
		 
		// post header
		HttpPost httpPost = new HttpPost(postReceiverUrl);
		 
		File file = new File(textFile);
		 
		MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
		reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		reqEntity.addTextBody("data", "/var/www/walkma.no-ip.info/results/test111.txt");
		
		reqEntity.addBinaryBody("uploadfile", file);
		httpPost.setEntity(reqEntity.build());
		 
		// execute HTTP post request
		HttpResponse response;
		try {
			response = httpClient.execute(httpPost);
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

}
