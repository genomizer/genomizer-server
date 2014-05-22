package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;

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

		httpServer.setExecutor(new Executor() {
			@Override
			public void execute(Runnable command) {

				try {
				new Thread(command).start();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void start() {
		httpServer.start();
	}

	HttpHandler createHandler() {
		return new HttpHandler() {
			@Override
			public void handle(HttpExchange exchange) throws IOException {

				System.out.println("\n-----------------\nNEW EXCHANGE: " + exchange.getHttpContext().getPath());
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
					case "/user":
						exchange(exchange, CommandType.UPDATE_USER_COMMAND);
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

	private void exchange(HttpExchange exchange, CommandType type) {
		InputStream bodyStream = exchange.getRequestBody();
		Scanner scanner = new Scanner(bodyStream);
		String body = "";
		String uuid = null;
		String username = null;
		System.out.println("Exchange: " + type);

		if(type != CommandType.LOGIN_COMMAND) {
			try {
				uuid =  exchange.getRequestHeaders().get("Authorization").get(0);
			} catch(NullPointerException e) {
				System.out.println("Unauthorized request!");
				Response errorResponse = new MinimalResponse(StatusCode.UNAUTHORIZED);
				try {
					respond(exchange, errorResponse);
					scanner.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				scanner.close();
				return;
			}
		} else {
		}
		while(scanner.hasNext()) {
			body = body.concat(" " + scanner.next());
		}
		scanner.close();

		Response response = null;

		try {
		username = Authenticate.getUsername(uuid);
		System.err.println("Username: " + username + "\n");
		} catch(Exception e ) {
			e.printStackTrace();
		}

		System.out.println("Body from client: " + body);

		try {
			String header = URLDecoder.decode(exchange.getRequestURI().toString(), "UTF-8");
			response = commandHandler.processNewCommand(body, header, username, type);

		} catch(Exception e ) {
			e.printStackTrace();
		}

		//TODO Should there be some error checking?


		try {
			respond(exchange, response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void respond(HttpExchange exchange, Response response) throws IOException {
		String body = null;
		if(response.getBody() == null || response.getBody().equals("")) {
			exchange.sendResponseHeaders(response.getCode(), 0);

		} else {
			body = response.getBody();
			System.out.println("Response: " + body.toString());
			exchange.sendResponseHeaders(response.getCode(), body.getBytes().length);

			OutputStream os = exchange.getResponseBody();
			os.write(body.getBytes());
			os.flush();
			os.close();
		}
		System.out.println("END OF EXCHANGE\n------------------");
	}
}
