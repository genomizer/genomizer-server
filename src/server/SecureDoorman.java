package server;
/**
* A Doorman-object is used to receive requests and send back responses to the client.
* The doorman is listening for the following contexts:
* /login
* /experiment
* /annotation
* /file
* /search
* /user
* /process
* /sysadm
* /genomeRelease
* /token
*
* Whenever a request is received the Doorman checks what context is has and creates a
* new Executor (on  a new thread) and afterwards continues listening for new requests.
*
*/



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;

import com.sun.net.httpserver.*;

import javax.net.ssl.*;

public class SecureDoorman {

    private HttpsServer httpsServer;

    /**
     * Constructs a HTTPs server (but doesn't start it) which listens on the
     * given port.
     *
     * @param port the listening port.
     * @throws IOException if the SecureDoorman object could not be created.
     */
    public SecureDoorman(int port) throws IOException {
        System.err.println("Https server starting...");

        RequestHandler requestHandler = new RequestHandler();
        httpsServer = HttpsServer.create(new InetSocketAddress(port), 0);
		httpsServer.setHttpsConfigurator(getHttpsConfiguration("baguette"));

        httpsServer.createContext("/login", requestHandler);
        httpsServer.createContext("/experiment", requestHandler);
        httpsServer.createContext("/annotation", requestHandler);
        httpsServer.createContext("/annotation/field", requestHandler);
        httpsServer.createContext("/annotation/value", requestHandler);
        httpsServer.createContext("/file", requestHandler);
        httpsServer.createContext("/search/", requestHandler);
        httpsServer.createContext("/user", requestHandler);
        httpsServer.createContext("/process/rawtoprofile", requestHandler);
        httpsServer.createContext("/sysadm", requestHandler);
        httpsServer.createContext("/sysadm/annpriv", requestHandler);
        httpsServer.createContext("/genomeRelease", requestHandler);
        httpsServer.createContext("/genomeRelease/", requestHandler);
        httpsServer.createContext("/token", requestHandler);
        httpsServer.createContext("/upload", requestHandler);
        httpsServer.createContext("/download", requestHandler);
        httpsServer.setExecutor(new Executor() {
            @Override
            public void execute(Runnable command) {
                try {
                    new Thread(command).start();
                } catch (Exception e) {
                    System.err.println("ERROR when creating new Executor." +
                            e.getMessage());
                    ErrorLogger.log("SERVER", "ERROR when creating " +
                            "new Executor." + e.getMessage());
                }
            }
        });
    }


    private HttpsConfigurator getHttpsConfiguration(String password) {

        SSLContext sslContext = null;
        char[] charPassword = password.toCharArray();
        String filename = "genoStore";
        try {
            //Initialize context
            sslContext = SSLContext.getInstance("TLSv1.1");

            //Initialize the key store
            KeyStore keyStore = KeyStore.getInstance("JKS");
            String keyStoreName = filename;
            keyStore.load(new FileInputStream(keyStoreName), charPassword);

            //Setup for the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
            kmf.init(keyStore, charPassword);

            //Setup for the trust manager factory
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
            tmf.init(keyStore);

            //Initialize the sslContext
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        } catch (KeyStoreException e) {
            e.printStackTrace();
            System.err.println("The provider for the KeyStore is not available.");
            System.exit(1);
        } catch (CertificateException e) {
            e.printStackTrace();
            System.err.println("Could not load the key from the KeyStore.");
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("Could not find the KeyStore algorithm.");
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Could not find the file: " + filename + ".");
            System.exit(1);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            System.err.println("The key could not be retrieved.\n Please check if the password is correct.");
            System.exit(1);
        } catch (KeyManagementException e) {
            e.printStackTrace();
            System.err.println("Could not initialize the SSL context for the server.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not open the file: " + filename + ".");
            System.exit(1);
        }

        //Creates the configurator object
        HttpsConfigurator httpsConfig = new HttpsConfigurator(sslContext) {

            //Overrides the method in order to change the default configurations
            @Override
            public void configure(HttpsParameters params) {

                try {
                    //Initialize the ssl context
                    SSLContext sslContext = SSLContext.getDefault();
                    SSLEngine sslEngine = sslContext.createSSLEngine();

                    //Set the Https parameters
                    SSLParameters sslParameters = new SSLParameters();
                    sslParameters.setNeedClientAuth(false);                //Change to true for client authentication
                    sslParameters.setCipherSuites(sslEngine.getEnabledCipherSuites());
                    sslParameters.setProtocols(sslEngine.getEnabledProtocols());
                    params.setSSLParameters(sslParameters);

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    System.err.println("Could not find the KeyStore algorithm.");
                    System.exit(1);
                }
            }
        };

        return httpsConfig;
    }

    /**
     * Starts the HttpsServer
     */
    public void start() {
        httpsServer.start();
        System.out.println("SecureDoorman started on port " + ServerSettings.
                genomizerPort);
    }
}