package command;

import authentication.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import server.ErrorLogger;
import server.Util;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Handles a request sent from a client. An incoming request is used to create a
 * Command object which is then executed, subsequently the produced response is
 * sent back to the client.
 */
public class RequestHandler implements HttpHandler {
	private Gson gson;

    /**
     * Constructs a RequestHandler.
     */
	public RequestHandler() {
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
	}

	@Override
	public void handle(HttpExchange exchange) {

        /*Log the fact that the request parsing has started.*/
		logRequest(exchange);

        /*Extract the request method and the context. Together they form a
        * key that is used to retrieve the appropriate command from a
        * hash map of all existing commands.*/
        String requestMethod = exchange.getRequestMethod();
		String context = exchange.getHttpContext().getPath();
		Class<? extends Command> commandClass = CommandClasses.
				get(requestMethod + " " + context);

		/*Authenticate the user and send the appropriate response*/
		String uuid = performAuthorization(exchange);
		if (commandClass == null) {
			if (uuid == null) {
				Debug.log("User could not be authenticated!");
				Response response = new MinimalResponse(StatusCode.
						UNAUTHORIZED);
				respond(response, exchange);
				return;
			} else {
				Debug.log("Unrecognized command.");
				respond(createBadRequestResponse(), exchange);
				return;
			}
		} else if (uuid == null && !commandClass.equals(LoginCommand.class)) {
			Debug.log("User could not be authenticated!");
			Response response = new MinimalResponse(StatusCode.
					UNAUTHORIZED);
			respond(response, exchange);
			return;
		}

        /*Retrieve the URI part of the request header.*/
		String uri = exchange.getRequestURI().toString();

		/*Does the length of the URI match the needed length??*/
		if (URILength.get(commandClass) != calculateURILength(uri)) {
			Debug.log("Bad format on command");
			respond(createBadRequestResponse(), exchange);
			return;
		}

        /*Log the user.*/
		logUser(uuid);

		/*TODO: Get the current user's user right level*/
		UserType userType = UserType.ADMIN;

		/*Read the json body and create the command.*/
		String json = readBody(exchange);
		logRequestBody(json);
		Command command = gson.fromJson(json, commandClass);
		command.setFields(uri, uuid, userType);

		/*Attempt to validate the command.*/
		try {
			command.validate();
		} catch (ValidateException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log("ValidateException", e.getMessage());
			respond(new ErrorResponse(e.getCode(), e.getMessage()), exchange);
			return;
		}

		/*Execute the command and respond.*/
		respond(command.execute(), exchange);
	}

    /*Used to send a response back to the client*/
    private void respond(Response response, HttpExchange exchange) {
        try {
            if (response.getBody() == null || response.getBody().equals("")) {
                exchange.sendResponseHeaders(response.getCode(), 0);
            } else {
                String body = response.getBody();
                logResponseBody(body);
                exchange.sendResponseHeaders(response.getCode(),
                        body.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(body.getBytes());
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            Debug.log("IOError when sending response back to client. " +
                    e.getMessage());
            ErrorLogger.log("SYSTEM", e);
        }

        Debug.log("END OF EXCHANGE\n------------------");
    }

    /*Used to calculate the "length" of an URI.*/
    private int calculateURILength(String requestURI) {
        return requestURI.split("/").length-1;
    }

    /*Returns the uuid of the connected user. Returns null if the user
	* is not authorized.*/
    private String getUUID(HttpExchange exchange) {
        String uuid = exchange.getRequestHeaders().get("Authorization").get(0);
        if (uuid != null && Authenticate.idExists(uuid)) {
            Authenticate.updateLatestRequest(uuid);
            return uuid;
        } else {
            return null;
        }
    }

	private String performAuthorization(HttpExchange exchange) {
		String uuid = null;

		// Get the value of the 'Authorization' header.
		List<String> authHeader = exchange.getRequestHeaders().
				get("Authorization");
		if (authHeader != null)
			uuid = authHeader.get(0);

		// Get the value of the 'token' parameter.
		String uuid2;
		HashMap<String, String> reqParams = new HashMap<>();
		Util.parseURI(exchange.getRequestURI(), reqParams);
		if (reqParams.containsKey("token")) {
			uuid2 = reqParams.get("token");
			if (uuid2 != null) {
				if (uuid == null || uuid.equals(uuid2)) {
					uuid = uuid2;
				} else {
					Debug.log("Authorization header "
							+ "and token parameter values differ!");
					return null;
				}
			}
		}

		// Actual authentication.
		Debug.log("Trying to authenticate token " + uuid + "...");
		if (uuid != null && Authenticate.idExists(uuid)) {
			Authenticate.updateLatestRequest(uuid);
			Debug.log("User " + Authenticate.getUsernameByID(uuid)
					+ " authenticated successfully.");
			return uuid;
		} else {
			Debug.log("Unauthorized request!");
			Response errorResponse = new MinimalResponse(StatusCode.
					UNAUTHORIZED);
			return null;
		}
	}

    /*Returns the body of the request (a json).*/
    private String readBody(HttpExchange exchange) {
        Scanner scanner = new Scanner(exchange.getRequestBody());
        String body = "";
        while(scanner.hasNext()) {
            body = body.concat(" " + scanner.next());
        }

        scanner.close();
        return body;
    }

	/*Creates a bad request ErrorResponse.*/
	private ErrorResponse createBadRequestResponse() {
		return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not create a " +
				"command from request. Bad format on request.");
	}

	/*Used to log a request.*/
	private void logRequest(HttpExchange exchange) {
		Debug.log("\n-----------------\nNEW EXCHANGE: " + exchange.
				getRequestMethod() + " " + exchange.getRequestURI().
                toString());
	}

    /*Used to log the body of a request.*/
    private void logRequestBody(String body) {
        Debug.log("Request body: ");
        Debug.log(body);
    }

    /*Used to log the body of a response.*/
	private void logResponseBody(String body) {
		Debug.log("Response body: ");
		Debug.log(body);
	}

	/*Used to a log that a user was authenticated.*/
	private void logUser(String uuid) {
		String username = Authenticate.getUsernameByID(uuid);
		Debug.log("Username: " + username + "\n");
	}
}
