package server;
/**
 * A Doorman-object is used to receive requests and send back responses to the
 * client. The doorman is listening for the following contexts:
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
 * /upload
 * /download
 *
 * Whenever a request is received the Doorman checks what context is has and
 * creates a new Executor (on  a new thread) and afterwards continues listening
 * for new requests.
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

import response.MinimalResponse;
import response.Response;
import response.StatusCode;

import authentication.Authenticate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import command.CommandHandler;
import command.CommandType;

public class Doorman {

	private HttpServer httpServer;
	private CommandHandler commandHandler;
	private UploadHandler   uploadHandler;
	private DownloadHandler downloadHandler;

	/**
	 * Constructor. Creates a HTTPServer (but doesn't start it) which listens on
     * the given port.
	 * @param commandHandler a commandHandler which will create and handle all
     *                       commands.
	 * @param port the listening port.
	 * @throws IOException
	 */
	public Doorman(CommandHandler commandHandler, int port) throws IOException {

		this.commandHandler = commandHandler;
		// TODO: Don't hard-code the upload and temp directories' locations.
		this.uploadHandler   = new UploadHandler("/upload", "resources/", "/tmp");
		this.downloadHandler = new DownloadHandler("/download", "resources/");

		httpServer = HttpServer.create(new InetSocketAddress(port),0);
		httpServer.createContext("/login", createHandler());
		httpServer.createContext("/experiment", createHandler());
		httpServer.createContext("/annotation", createHandler());
		httpServer.createContext("/file", createHandler());
		httpServer.createContext("/search", createHandler());
		httpServer.createContext("/user", createHandler());
		httpServer.createContext("/process", createHandler());
		httpServer.createContext("/sysadm", createHandler());
		httpServer.createContext("/genomeRelease", createHandler());
		httpServer.createContext("/token", createHandler());
		httpServer.createContext("/upload", createHandler());
		httpServer.createContext("/download", createHandler());

		httpServer.setExecutor(new Executor() {
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
	 * Start the HTTPServer.
	 */
	public void start() {
		httpServer.start();
		System.out.println("Doorman started on port " +
						   ServerSettings.genomizerPort);
	}

	/**
	 * Determines the specific command which is to be created.
	 * @return
	 */
	HttpHandler createHandler() {
		return new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {

			    try {
				String method = exchange.getRequestMethod();
				String requestPath = exchange.getHttpContext().getPath();
				Debug.log("\n-----------------\nNEW EXCHANGE: " + method
						+ " " + requestPath);
				switch(method) {
				case "GET":
					switch(requestPath) {
					case "/experiment":
						handleRequest(exchange, CommandType.
                                GET_EXPERIMENT_COMMAND);
						break;
					case "/file":
						handleRequest(exchange, CommandType.
                                GET_FILE_FROM_EXPERIMENT_COMMAND);
						break;
					case "/search":
						handleRequest(exchange, CommandType.
                                SEARCH_FOR_EXPERIMENTS_COMMAND);
						break;
					case "/annotation":
						handleRequest(exchange, CommandType.
                                GET_ANNOTATION_INFORMATION_COMMAND);
						break;
					case "/genomeRelease":
						if(exchange.getRequestURI().toString().
                                startsWith("/genomeRelease/")) {
							handleRequest(exchange, CommandType.
                                    GET_GENOME_RELEASE_SPECIES_COMMAND);
						} else {
							handleRequest(exchange, CommandType.
                                    GET_ALL_GENOME_RELEASE_COMMAND);
						}
						break;
					case "/sysadm":
						handleRequest(exchange, CommandType.
                                GET_ANNOTATION_PRIVILEGES_COMMAND);
						break;
					case "/process":
						handleRequest(exchange, CommandType.
                                GET_PROCESS_STATUS_COMMAND);
						break;
					case "/token":
						handleRequest(exchange, CommandType.
                                IS_TOKEN_VALID_COMMAND);
						break;
					case "/upload":
						if (performAuthorization(exchange) != null) {
							uploadHandler.handleGET(exchange);
						}
						break;
					case "/download":
						if (performAuthorization(exchange) != null) {
							downloadHandler.handleGET(exchange);
						}
						break;
					default:
						Debug.log("HTTP 404 Not Found: " + method + " " + requestPath);
						respond(exchange, new MinimalResponse(StatusCode.NOT_FOUND));
						break;
					}
					break;

				case "PUT":
					switch(requestPath) {
					case "/experiment":
						handleRequest(exchange, CommandType.
                                UPDATE_EXPERIMENT_COMMAND);
						break;
					case "/file":
						handleRequest(exchange, CommandType.
                                UPDATE_FILE_IN_EXPERIMENT_COMMAND);
						break;
					case "/process":

						String processPath = exchange.getRequestURI().
                                toString();

						if (processPath.startsWith("/process/rawtoprofile")) {
							handleRequest(exchange, CommandType.
                                    PROCESS_COMMAND);
						}
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().
                                startsWith("/annotation/field")) {
							handleRequest(exchange, CommandType.
                                    RENAME_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().
                                startsWith("/annotation/value")) {
							handleRequest(exchange, CommandType.
                                    RENAME_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/sysadm":
						if (exchange.getRequestURI().toString().
                                startsWith("/sysadm/annpriv")) {
							handleRequest(exchange, CommandType.
                                    UPDATE_ANNOTATION_PRIVILEGES_COMMAND);
						}
						break;
					default:
						Debug.log("HTTP 404 Not Found: " + method + " " + requestPath);
						respond(exchange, new MinimalResponse(StatusCode.NOT_FOUND));
						break;
					}
					break;

				case "POST":
					switch(requestPath) {
					case "/login":
						handleRequest(exchange, CommandType.LOGIN_COMMAND);
						break;
					case "/experiment":
						handleRequest(exchange, CommandType.
                                ADD_EXPERIMENT_COMMAND);
						break;
					case "/file":
						handleRequest(exchange, CommandType.
                                ADD_FILE_TO_EXPERIMENT_COMMAND);
						break;
					case "/user":
						handleRequest(exchange, CommandType.CREATE_USER_COMMAND);
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().
                                startsWith("/annotation/field")) {
							handleRequest(exchange, CommandType.
                                    ADD_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().
                                startsWith("/annotation/value")) {
							handleRequest(exchange, CommandType.
                                    ADD_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/genomeRelease":
						handleRequest(exchange, CommandType.
                                ADD_GENOME_RELEASE_COMMAND);
						break;
					case "/upload":
						if (performAuthorization(exchange) != null) {
							uploadHandler.handlePOST(exchange);
						}
						break;
					default:
						Debug.log("HTTP 404 Not Found: " + method + " " + requestPath);
						respond(exchange, new MinimalResponse(StatusCode.NOT_FOUND));
						break;
					}
					break;

				case "DELETE":
					switch(requestPath) {
					case "/login":
						handleRequest(exchange, CommandType.LOGOUT_COMMAND);
						break;
					case "/experiment":
						handleRequest(exchange, CommandType.
                                DELETE_EXPERIMENT_COMMAND);
						break;
					case "/file":
						handleRequest(exchange, CommandType.
                                DELETE_FILE_FROM_EXPERIMENT_COMMAND);
						break;
					case "/user":
						handleRequest(exchange, CommandType.
                                DELETE_USER_COMMAND);
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().
                                startsWith("/annotation/field")) {
							handleRequest(exchange, CommandType.
                                    REMOVE_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().
                                startsWith("/annotation/value")) {
							handleRequest(exchange, CommandType.
                                    DELETE_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/genomeRelease":
						handleRequest(exchange, CommandType.
                                DELETE_GENOME_RELEASE_COMMAND);
						break;
					default:
						Debug.log("HTTP 404 Not Found: " + method + " " + requestPath);
						respond(exchange, new MinimalResponse(StatusCode.NOT_FOUND));
						break;
					}
					break;

				case "OPTIONS":
					//TODO: Not all resources actually support all methods.
					exchange.getResponseHeaders().set("Allow", "GET, PUT, " +
                            "POST, DELETE");
					respond(exchange, new MinimalResponse(200));
					break;

				default:
					Debug.log("Unsupported HTTP method: " + method);
					respond(exchange, new MinimalResponse(StatusCode.METHOD_NOT_ALLOWED));
					break;
				}
		    }
		    catch (Exception ex) {
				Debug.log("Internal server error " + ex.getMessage());
				ErrorLogger.log("SYSTEM", ex);
				ex.printStackTrace();
				respond(exchange, new MinimalResponse(StatusCode.INTERNAL_SERVER_ERROR));
		    }
		    }
		};
	}

	/**
	 * Actually perform authorization.
	 *
	 * @param exchange the HTTPExchange
	 * @return         null in case of error, name of the logged in user otherwise.
	 */
	private String performAuthorization(HttpExchange exchange) {
		List<String> auth = exchange.getRequestHeaders().
				get("Authorization");
		String uuid = null;

		if (auth != null && Authenticate.idExists(auth.get(0))) {
			uuid = auth.get(0);
			Authenticate.updateLatestRequest(uuid);
		} else {
			Debug.log("Unauthorized request!");
			Response errorResponse = new MinimalResponse(StatusCode.
					UNAUTHORIZED);
			try {
				respond(exchange, errorResponse);
			} catch (IOException e1) {
				Debug.log("Could not send response to client. " + e1.
						getMessage());
			}
		}

		return uuid;
	}

	/**
	 * Handles the request information from the client and creates a Command
     * with CommandHandler.
	 * @param exchange the HTTPExchange
	 * @param type which specific type of command it is.
	 */
	private void handleRequest(HttpExchange exchange, CommandType type) {
		InputStream bodyStream = exchange.getRequestBody();
		String uuid = null;
		Debug.log("Exchange: " + type);

		/** authorization */
		if(type != CommandType.LOGIN_COMMAND) {
			uuid = performAuthorization(exchange);
			if (uuid == null)
				return;
		}

		Scanner scanner = new Scanner(bodyStream);
		String body = "";
		while(scanner.hasNext()) {
			body = body.concat(" " + scanner.next());
		}
		scanner.close();

		String username = null;
		username = Authenticate.getUsernameByID(uuid);
		Debug.log("Username: " + username + "\n");
		Debug.log("Body from client: " + body);

		Response response = null;
		try {
			String header = URLDecoder.decode(exchange.getRequestURI().
                    toString(), "UTF-8");
			response = commandHandler.processNewCommand(body, header, uuid,
                    type);
		} catch(Exception e ) {
			Debug.log("Could not create/process new command " + e.getMessage());
			ErrorLogger.log("SYSTEM", e);
			e.printStackTrace();
		}

		//TODO Should there be some error checking?
		try {
			respond(exchange, response);
		} catch (IOException e) {
			Debug.log("IOError when sending response back to client. " +
                    e.getMessage());
			ErrorLogger.log("SYSTEM", e);
		}
	}

	/**
	 * Sends a response back to the client.
	 * @param exchange the HTTPExchange-object.
	 * @param response the response object containing information about the
     *                 response.
	 * @throws IOException
	 */
	private void respond(HttpExchange exchange, Response response)
            throws IOException {
		if(response.getBody() == null || response.getBody().equals("")) {
			exchange.sendResponseHeaders(response.getCode(), 0);
		} else {
			String body = response.getBody();
			Debug.log("Response: " + body);
			exchange.getResponseHeaders().set("Content-Type", "application/json");
			exchange.sendResponseHeaders(response.getCode(), body.getBytes().
                    length);
			OutputStream os = exchange.getResponseBody();
			os.write(body.getBytes());
			os.flush();
			os.close();
		}
		Debug.log("END OF EXCHANGE\n------------------");
	}
}
