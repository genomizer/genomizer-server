package transfer;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by c11epm on 4/28/14.
 */
public class HTTPURLDownload {
    //TODO create a definitionfile
    private final int TRANSFERCHUNKSIZE = 1024;
    private static String localFilePath = "/home/tobias/Dokument/testfile.html";
    private BufferedInputStream in = null;
    private FileOutputStream fout = null;
    private File outputFile;
	private URL url;
    
    private static HTTPURLDownload dummy;

    public static void main(String[] args) {
        dummy = new HTTPURLDownload("http://10.42.0.91:8090/html/index.html", localFilePath);
        dummy.openStreams();
        dummy.readData();
//    	put();
    }
    


    private static void put() {
    	try {
            URL targetUrl = new URL("http://10.42.0.91:8090/html/asd1.html");
            HttpURLConnection connection = (HttpURLConnection)
                    targetUrl.openConnection();
            connection.setDoOutput(true);
            connection.setReadTimeout(1000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Authorization", "pvt");
            PrintWriter outputStream = new PrintWriter(
                    connection.getOutputStream(), true);
            File file = new File(localFilePath);
            BufferedReader reader = new BufferedReader(
                    new FileReader(file));
            while (reader.ready()) {
//            	System.out.println("Sending data");
                outputStream.println(reader.readLine());
                outputStream.flush();
            }
            int responseCode;
            if ((responseCode = connection.getResponseCode()) != 200) {
                System.out.println("Error wrong response code: "
                        + responseCode);
            }
            System.out.println(responseCode);
            connection.disconnect();
            reader.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(FileNotFoundException e) {
            System.out.println("File was not found: " + localFilePath);
        } catch (IOException e) {
            System.out.println("Connection error: " + e.getMessage() + " " + "http://10.42.0.91:8090/html/asd1.html");
        }
    }
    
    public HTTPURLDownload(String url, String localFilePath){
        try {
            this.url = new URL(url);
            HTTPURLDownload.localFilePath = localFilePath;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public boolean openFile(String filePath){
        outputFile = new File(filePath);
        if(!outputFile.exists()){
            try {
                if(!outputFile.createNewFile()){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public boolean openOutputStream(){
        try {
            fout = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean openInputStream(){
        try {
            in = new BufferedInputStream(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean openStreams() {
        return openFile(localFilePath) &&
                openInputStream() && openOutputStream();
    }


    public boolean readData() {
        final byte data[] = new byte[TRANSFERCHUNKSIZE];
        int count;
        try {
            System.out.println("Reading data...");
            while((count = in.read(data,0,TRANSFERCHUNKSIZE)) != -1){
                fout.write(data, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Download complete");
        return true;

    }
    public String getURL() {
        return url.toString();
    }

    public boolean changeURL(URL url) {
        assert url != null;
        this.url = url;
        return openStreams();
    }

}
