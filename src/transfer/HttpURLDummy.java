package transfer;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLDummy {
    //TODO create a definitionfile
    private final int TRANSFERCHUNKSIZE = 1024;
    private URL url;
    private String localFilePath;
    private BufferedInputStream in = null;
    private FileOutputStream fout = null;
    private File outputFile;

    public HttpURLDummy(String url, String localFilePath){
        try {
            this.url = new URL(url);
            this.localFilePath = localFilePath;
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
