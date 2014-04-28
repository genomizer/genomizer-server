package transfer.Test;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by c11epm on 4/25/14.
 */
public class HttpClientTest {
    DefaultHttpClient c;
    HttpURLConnection connection;

    @Test
    public void shouldStartClient(){

        String url = "http://username:password@localhost/uploaded.txt";

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost  httppost = new HttpPost(url);
        MultipartEntity entity = new MultipartEntity();

        ContentBody body = new FileBody(
                new File("C:/Users/username/Desktop/myfile.txt"),
                ContentType.APPLICATION_OCTET_STREAM
        );

        entity.addPart("file", body);
        httppost.setEntity(entity);

        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            System.out.println(response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
