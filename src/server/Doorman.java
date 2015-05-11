package server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import com.sun.net.httpserver.HttpsServer;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.concurrent.Executor;
/**
 * Used to receive requests and forward them to a request handler (which will
 * then process them and return the appropriate response). This implementation
 * will start a new thread for each incoming request. The doorman is listening
 * for the following contexts:
 *
 * /login
 * /experiment
 * /annotation
 * /annotation/field
 * /annotation/value
 * /file
 * /search
 * /user
 * /process
 * /process/rawtoprofile
 * /sysadm
 * /sysadm/annpriv
 * /genomeRelease
 * /genomeRelease/
 * /token
 * /upload
 * /download
 *
 * Whenever a request is received the Doorman checks what context is has and
 * creates a new Executor (on  a new thread) and afterwards continues listening
 * for new requests.
 */
public class Doorman {
	private HttpServer httpServer;
	private HttpsServer httpsServer;
	private static WorkPool workPool;

	/**
	 * Constructs a HTTP server (but doesn't start it) which listens on the
     * given port.
	 * @throws IOException
	 */
	public Doorman(WorkPool pool, int port) throws IOException {
		Doorman.workPool = pool;

		if (ServerSettings.genomizerHttpPort > 0) {
			httpServer = HttpServer.create(
					new InetSocketAddress(ServerSettings.genomizerHttpPort),0);
			createContextsAndSetExecutor(httpServer);
		}

		if (ServerSettings.genomizerHttpsPort > 0) {
			httpsServer = HttpsServer.create(
					new InetSocketAddress(ServerSettings.genomizerHttpsPort), 0);
			httpsServer.setHttpsConfigurator(getHttpsConfiguration("baguette"));
			createContextsAndSetExecutor(httpsServer);
		}
	}

	private void createContextsAndSetExecutor(HttpServer server) {
		RequestHandler requestHandler = new RequestHandler();

		server.createContext("/login", requestHandler);
		server.createContext("/experiment", requestHandler);
		server.createContext("/annotation", requestHandler);
		server.createContext("/annotation/field", requestHandler);
		server.createContext("/annotation/value", requestHandler);
		server.createContext("/file", requestHandler);
		server.createContext("/search", requestHandler);
		server.createContext("/user", requestHandler);
		server.createContext("/process/rawtoprofile", requestHandler);
		server.createContext("/sysadm", requestHandler);
	    server.createContext("/sysadm/annpriv", requestHandler);
		server.createContext("/genomeRelease", requestHandler);
		server.createContext("/genomeRelease/", requestHandler);
		server.createContext("/token", requestHandler);
		server.createContext("/upload", requestHandler);
		server.createContext("/download", requestHandler);

		server.setExecutor(new Executor() {
			@Override
			public void execute(Runnable command) {
				try {
				    new Thread(command).start();
				} catch(Exception e) {
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
			keyStore.load(new FileInputStream(keyStoreName),charPassword);

			//Setup for the key manager factory
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
			kmf.init(keyStore, charPassword);

			//Setup for the trust manager factory
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
			tmf.init(keyStore);

			//Initialize the sslContext
			sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),null);

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
		HttpsConfigurator httpsConfig = new HttpsConfigurator(sslContext){

			//Overrides the method in order to change the default configurations
			@Override
			public void configure (HttpsParameters params) {

				try {
					//Initialize the ssl context
					SSLContext sslContext = SSLContext.getDefault();
					SSLEngine sslEngine = sslContext.createSSLEngine();

					//Set the Https parameters
					SSLParameters sslParameters = new SSLParameters();
					sslParameters.setNeedClientAuth(false); 				//Change to true for client authentication
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
	 * Starts the HTTPServer
	 */
	public void start() {
		if (httpsServer != null) {
			httpsServer.start();
			System.out.println("Doorman started on HTTPS port " +
					ServerSettings.genomizerHttpsPort);
		}
		if (httpServer != null) {
			httpServer.start();
			System.out.println("Doorman started on HTTP port " +
					ServerSettings.genomizerHttpPort);
		}
	}

	public static WorkPool getWorkPool(){
		return workPool;
	}
}
