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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

import command.ValidateException;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;

import authentication.Authenticate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import transfer.DownloadHandler;
import transfer.UploadHandler;
import transfer.Util;


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
		RequestHandler requestHandler = new RequestHandler();
		httpServer = HttpServer.create(new InetSocketAddress(port),0);
        httpServer.createContext("/login", requestHandler);
		httpServer.createContext("/experiment", requestHandler);
		httpServer.createContext("/annotation", requestHandler);
		httpServer.createContext("/annotation/field", requestHandler);
		httpServer.createContext("/annotation/value", requestHandler);
		httpServer.createContext("/file", requestHandler);
		httpServer.createContext("/search/", requestHandler);
		httpServer.createContext("/user", requestHandler);
		httpServer.createContext("/process/rawtoprofile", requestHandler);
		httpServer.createContext("/sysadm", requestHandler);
		httpServer.createContext("/sysadm/annpriv", requestHandler);
		httpServer.createContext("/genomeRelease", requestHandler);
		httpServer.createContext("/genomeRelease/", requestHandler);
		httpServer.createContext("/token", requestHandler);
		httpServer.createContext("/upload", requestHandler);
		httpServer.createContext("/download", requestHandler);
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
}
