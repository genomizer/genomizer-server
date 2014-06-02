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

	/**
	 * Constructor. Creates a HTTPServer (but doesn't start it) which listens on the given port.
	 * @param commandHandler a commandHandler which will create and handle all commands.
	 * @param port the listening port.
	 * @throws IOException
	 */
	public Doorman(CommandHandler commandHandler, int port) throws IOException {

		this.commandHandler = commandHandler;

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

		httpServer.setExecutor(new Executor() {
			@Override
			public void execute(Runnable command) {

				try {
				new Thread(command).start();
				} catch(Exception e) {
					System.err.println("ERROR when creating new Executor." + e.getMessage());
					ErrorLogger.log("SERVER", "ERROR when creating new Executor." + e.getMessage());
				}
			}
		});
	}

	/**
	 * Start the HTTPServer
	 */
	public void start() {
		httpServer.start();
	}

	/**
	 * Determines the specific command which is to be created.
	 * @return
	 */
	HttpHandler createHandler() {
		return new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {

				Debug.log("\n-----------------\nNEW EXCHANGE: " + exchange.getHttpContext().getPath());
				switch(exchange.getRequestMethod()) {
				case "GET":
					switch(exchange.getHttpContext().getPath()) {
					case "/experiment":
						exchange(exchange, CommandType.GET_EXPERIMENT_COMMAND);
						break;
					case "/file":
						exchange(exchange, CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND);
						break;
					case "/search":
						exchange(exchange, CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND);
						break;
					case "/annotation":
						exchange(exchange, CommandType.GET_ANNOTATION_INFORMATION_COMMAND);
						break;
					case "/genomeRelease":
						if(exchange.getRequestURI().toString().startsWith("/genomeRelease/")){
							exchange(exchange, CommandType.GET_GENOME_RELEASE_SPECIES_COMMAND);
						}else{
							exchange(exchange, CommandType.GET_ALL_GENOME_RELEASE_COMMAND);
						}
						break;
					case "/sysadm":
						exchange(exchange, CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND);
						break;
					case "/process":
						exchange(exchange, CommandType.GET_PROCESS_STATUS_COMMAND);
						break;
					case "/token":
						exchange(exchange, CommandType.IS_TOKEN_VALID_COMMAND);
					}
					break;


				case "PUT":
					switch(exchange.getHttpContext().getPath()) {
					case "/experiment":
						exchange(exchange, CommandType.UPDATE_EXPERIMENT_COMMAND);
						break;
					case "/file":
						exchange(exchange, CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND);
						break;
					case "/process":

						String processPath = exchange.getRequestURI().toString();

						if (processPath.startsWith("/process/rawtoprofile")) {
							exchange(exchange, CommandType.PROCESS_COMMAND);
						}
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
							exchange(exchange, CommandType.RENAME_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
							exchange(exchange, CommandType.RENAME_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/sysadm":
						if (exchange.getRequestURI().toString().startsWith("/sysadm/annpriv")) {
							exchange(exchange, CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND);
						} else if (exchange.getRequestURI().toString().startsWith("/sysadm/usrpriv")) {
							exchange(exchange, CommandType.UPDATE_USER_PRIVILEGES_COMMAND);
						}
						break;
					}
					break;


				case "POST":
					switch(exchange.getHttpContext().getPath()) {
					case "/login":
						exchange(exchange, CommandType.LOGIN_COMMAND);
						break;
					case "/experiment":
						exchange(exchange, CommandType.ADD_EXPERIMENT_COMMAND);
						break;
					case "/file":
						exchange(exchange, CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND);
						break;
					case "/user":
						exchange(exchange, CommandType.CREATE_USER_COMMAND);
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
							exchange(exchange, CommandType.ADD_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
							exchange(exchange, CommandType.ADD_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/genomeRelease":
						exchange(exchange, CommandType.ADD_GENOME_RELEASE_COMMAND);
						break;
					}
					break;


				case "DELETE":
					switch(exchange.getHttpContext().getPath()) {
					case "/login":
						exchange(exchange, CommandType.LOGOUT_COMMAND);
						break;
					case "/experiment":
						exchange(exchange, CommandType.DELETE_EXPERIMENT_COMMAND);
						break;
					case "/file":
						exchange(exchange, CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND);
						break;
					case "/user":
						exchange(exchange, CommandType.DELETE_USER_COMMAND);
						break;
					case "/annotation":
						if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
							exchange(exchange, CommandType.REMOVE_ANNOTATION_FIELD_COMMAND);
						} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
							exchange(exchange, CommandType.DELETE_ANNOTATION_VALUE_COMMAND);
						}
						break;
					case "/genomeRelease":
						exchange(exchange, CommandType.DELETE_GENOME_RELEASE_COMMAND);
						break;
					}
					break;
				}
			}
		};
	}

	/**
	 * Handles the request information from the client and creates a Command with CommandHandler.
	 * @param exchange the HTTPExchange
	 * @param type which specific type of command it is.
	 */
	private void exchange(HttpExchange exchange, CommandType type) {
		InputStream bodyStream = exchange.getRequestBody();
		Scanner scanner = new Scanner(bodyStream);
		String body = "";
		String uuid = null;
		String username = null;
		Debug.log("Exchange: " + type);

		if(type != CommandType.LOGIN_COMMAND) {
			List<String> auth = exchange.getRequestHeaders().get("Authorization");
			if (auth != null && Authenticate.idExists(auth.get(0))) {
				uuid = auth.get(0);
				Authenticate.updateLatestRequest(uuid);
			} else {
				Debug.log("Unauthorized request!");
				Response errorResponse = new MinimalResponse(StatusCode.UNAUTHORIZED);
				try {
					respond(exchange, errorResponse);
					scanner.close();
				} catch (IOException e1) {
					Debug.log("Could not send response to client. " + e1.getMessage());
				}
				scanner.close();
				return;
			}
		}
		while(scanner.hasNext()) {
			body = body.concat(" " + scanner.next());
		}
		scanner.close();

		Response response = null;


		//username = Authenticate.getUsername(uuid);
		Debug.log("Username: " + username + "\n");


		Debug.log("Body from client: " + body);

		try {
			String header = URLDecoder.decode(exchange.getRequestURI().toString(), "UTF-8");
			response = commandHandler.processNewCommand(body, header, uuid, type);

		} catch(Exception e ) {
			Debug.log("Could not create/process new command " + e.getMessage());
			ErrorLogger.log("SYSTEM", e);
			e.printStackTrace();
		}

		//TODO Should there be some error checking?


		try {
			respond(exchange, response);
		} catch (IOException e) {
			Debug.log("IOError when sending response back to client. " + e.getMessage());
			ErrorLogger.log("SYSTEM", e);
		}
	}

	/**
	 * Sends a response back to the client.
	 * @param exchange the HTTPExchange-object.
	 * @param response the response object containting information about the response.
	 * @throws IOException
	 */
	private void respond(HttpExchange exchange, Response response) throws IOException {
		String body = null;
		if(response.getBody() == null || response.getBody().equals("")) {
			exchange.sendResponseHeaders(response.getCode(), 0);

		} else {
			body = response.getBody();
			Debug.log("Response: " + body.toString());
			exchange.sendResponseHeaders(response.getCode(), body.getBytes().length);

			OutputStream os = exchange.getResponseBody();
			os.write(body.getBytes());
			os.flush();
			os.close();
		}

		Debug.log("END OF EXCHANGE\n------------------");
	}
}
