package transfer;

<<<<<<< HEAD
=======
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
>>>>>>> af507414c0aca23277bf41e552511efb6997a97c
import java.net.URL;

/**
 * Created by c11epm on 4/28/14.
 */
public class HttpURLDummy {
    private URL conn;
    private BufferedInputStream in = null;
    private FileOutputStream fout = null;

    public HttpURLDummy(String url){
        try {
            conn = new URL(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public String getResponse() {
        return null;
    }

    public boolean connect() {
        File f = new File("/home/c11/c11epm/testfile.jpg");
        try {
            if(f.createNewFile()){
                return false;
            }
            in = new BufferedInputStream(conn.openStream());
            fout = new FileOutputStream(f);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

<<<<<<< HEAD
    public HttpURLDummy(){
=======
    public boolean readData() {
        final byte data[] = new byte[1024];
        int count;
        try {
            while((count = in.read(data,0,1024)) != -1){
                System.out.println("Reading data...");
                fout.write(data, 0, count);
                System.out.println("Print Data");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
>>>>>>> af507414c0aca23277bf41e552511efb6997a97c

    }
}
