package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
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
	private static ProcessPool processPool;

	/**
	 * Constructs a HTTP server (but doesn't start it) which listens on the
	 * given port.
	 * @throws IOException
	 */
	public Doorman(ProcessPool processPool) throws IOException {
		Doorman.processPool = processPool;

		httpServer = HttpServer.create(
				new InetSocketAddress(ServerSettings.genomizerPort),0);
		createContextsAndSetExecutor(httpServer);
	}

	private void createContextsAndSetExecutor(HttpServer server) {
		RequestHandler requestHandler = new RequestHandler();

		server.createContext("/login", requestHandler);
		server.createContext("/token", requestHandler);
		server.createContext("/experiment", requestHandler);
		server.createContext("/experiment/", requestHandler);
		server.createContext("/file", requestHandler);
		server.createContext("/file/", requestHandler);
		server.createContext("/convertfile", requestHandler);
		server.createContext("/search/", requestHandler);
		server.createContext("/user", requestHandler);
		server.createContext("/admin/user", requestHandler);
		server.createContext("/admin/user/", requestHandler);
		server.createContext("/process", requestHandler);
		server.createContext("/process/dummy", requestHandler);
		server.createContext("/process/rawtoprofile", requestHandler);
		server.createContext("/process/cancelprocess", requestHandler);
		server.createContext("/annotation", requestHandler);
		server.createContext("/annotation/field", requestHandler);
		server.createContext("/annotation/field/", requestHandler);
		server.createContext("/annotation/value", requestHandler);
		server.createContext("/annotation/value/", requestHandler);
		server.createContext("/genomeRelease", requestHandler);
		server.createContext("/genomeRelease/", requestHandler);
		server.createContext("/geo", requestHandler);
		server.createContext("/geo/", requestHandler);
		server.createContext("/upload", requestHandler);
		server.createContext("/download", requestHandler);
		server.createContext("/process/processCommands", requestHandler);

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

	/**
	 * Starts the HTTPServer
	 */
	public void start() {
		httpServer.start();
		System.out.println("Doorman started on port " +
				ServerSettings.genomizerPort);
	}

	public static ProcessPool getProcessPool(){
		return processPool;
	}
}