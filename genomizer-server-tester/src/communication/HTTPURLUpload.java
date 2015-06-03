package communication;

import model.ErrorLogger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// TODO: byt till vettigt namn.
// TODO: not thread safe?
/**
 * Class for handling file uploads.
 *
 * @version 1.0
 * 03 June 2015
 */
public class HTTPURLUpload {

    private String filePath;
    private String uploadPath;
    private String fileName;
    private float currentProgress;
    private int responseCode;

    /**
     * Constructs a new HTTPURLUpload object
     *
     * @param uploadPath
     *            the target location
     * @param filePath
     *            the source location
     * @param fileName
     *            the filename
     */
    public HTTPURLUpload(String uploadPath, String filePath, String fileName) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.uploadPath = uploadPath;

    }

    /**
     * Uploads a file to
     *
     * @param userID
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    public void sendFile(String userID) throws IllegalArgumentException, IOException {
        uploadPath = uploadPath.replaceFirst("\\u003d", "=");
        String path;
        if (uploadPath.contains("=")) {
            path = uploadPath.split("=")[1];
        } else {
            path = uploadPath;
        }

        // new HttpClient

        // Ignore differences between given hostname and certificate hostname

        HttpClientBuilder hcBuilder = HttpClients.custom();

        if (uploadPath.startsWith("https://")) {
            hcBuilder.setSslcontext(SSLTool.getSslContext())
                    .setHostnameVerifier(SSLTool.getHostnameVerifier());
        }

        CloseableHttpClient httpClient = hcBuilder.build();

        HttpClientContext localContext = HttpClientContext.create();

        // post header
        File file = new File(filePath);
        try {
            String encodedFileName = URLEncoder.encode(file.getName(), "UTF-8");
            uploadPath = uploadPath.replaceFirst(file.getName(),
                    encodedFileName);
        } catch (UnsupportedEncodingException e) {
            ErrorLogger.log(e);
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(uploadPath);
        httpPost.addHeader("Authorization", userID);

        MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // add the location on the server where the file should be saved
        reqEntity.addTextBody("path", path);

        reqEntity.addBinaryBody("uploadfile", file);
        ProgressHttpEntityWrapper.ProgressCallback progressCallback = new ProgressHttpEntityWrapper.ProgressCallback() {

            @Override
            public void progress(float progress) {
                if (progress != -1) {
                    currentProgress = progress;
                }
            }

        };
        httpPost.setEntity(new ProgressHttpEntityWrapper(reqEntity.build(),
                progressCallback));

        try {
            HttpResponse response;
            // execute HTTP post request
            response = httpClient.execute(httpPost, localContext);
            responseCode = response.getStatusLine().getStatusCode();
            if (responseCode >= 300) {
                //TODO Skapa ett vettigare felmeddelande CF
                throw new IOException("SKAPA ETT B�TTRE FELMEDDELANDE");
            }
        } catch (IOException e) {
            ErrorLogger.log(e);
            //TODO Fixa b�ttre felmeddelande CF
            throw new IOException("",e);
        }
    }

    /**
     * Returns the filename of the file to upload
     *
     * @return the filename
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Return the current progress of the file upload
     *
     * @return the current progress
     */
    public float getCurrentProgress() {
        return currentProgress;
    }

    /**
     * Returns the response code of the upload request
     *
     * @return the response code of the upload request
     */
    public int getResponseCode() {
        return responseCode;
    }
}
