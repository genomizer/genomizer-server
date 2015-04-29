package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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

//TODO Make the upload handling and download handling into commands.

public class Doorman {
	private HttpServer httpServer;
	private UploadHandler uploadHandler;
	private DownloadHandler downloadHandler;

	/**
	 * Constructs a HTTP server (but doesn't start it) which listens on the
     * given port.
	 * @param port the listening port.
	 * @throws IOException if the Doorman object could not be created.
     */
    public Doorman(int port) throws IOException {
		uploadHandler   = new UploadHandler("/upload", "resources/", "/tmp");
		downloadHandler = new DownloadHandler("/download", "resources/");
		CustomRequestHandler customRequestHandler = new CustomRequestHandler();

		RequestHandler requestHandler = new RequestHandler();
		httpServer = HttpServer.create(new InetSocketAddress(port),0);
        httpServer.createContext("/login", requestHandler);
		httpServer.createContext("/experiment", requestHandler);
		httpServer.createContext("/annotation", requestHandler);
		httpServer.createContext("/annotation/field", requestHandler);
		httpServer.createContext("/annotation/value", requestHandler);
		httpServer.createContext("/file", requestHandler);
		httpServer.createContext("/search", requestHandler);
		httpServer.createContext("/user", requestHandler);
		httpServer.createContext("/process/rawtoprofile", requestHandler);
		httpServer.createContext("/sysadm", requestHandler);
		httpServer.createContext("/sysadm/annpriv", requestHandler);
		httpServer.createContext("/genomeRelease", requestHandler);
		httpServer.createContext("/genomeRelease/", requestHandler);
		httpServer.createContext("/token", requestHandler);

		httpServer.createContext("/upload", customRequestHandler);
		httpServer.createContext("/download", customRequestHandler);

        httpServer.setExecutor(new Executor() {
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

	/**
	 * Starts the HTTPServer
	 */
	public void start() {
		httpServer.start();
		System.out.println("Doorman started on port " + ServerSettings.
				genomizerPort);
	}

	//TODO Currently uses NO authentication

	private class CustomRequestHandler implements HttpHandler {
		public void handle(HttpExchange exchange) {
			String requestMethod = exchange.getRequestMethod();
			String requestPath = exchange.getHttpContext().getPath();
			String context = requestMethod + " " + requestPath;

			try {
				switch (context) {
					case ("GET /download"):
						downloadHandler.handleGET(exchange);
						break;
					case ("GET /upload"):
						uploadHandler.handleGET(exchange);
						break;
					case ("POST /upload"):
						uploadHandler.handlePOST(exchange);
						break;
				}
			} catch (Exception e) {
				Debug.log("Could not handle upload/download");
			}

		}
	}
}
