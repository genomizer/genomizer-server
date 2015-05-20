package server;

import authentication.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import command.*;
import command.connection.PostLoginCommand;
import command.process.PutProcessCommand;
import database.DatabaseAccessor;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import transfer.DownloadHandler;
import transfer.UploadHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.io.OutputStream;
import java.net.URI;
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
    private UploadHandler uploadHandler;
    private DownloadHandler downloadHandler;

    /**
     * Constructs a RequestHandler.
     */
	public RequestHandler() {
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
        uploadHandler = new UploadHandler("/upload", ServerSettings.
                fileLocation, System.getProperty("java.io.tmpdir"));
        downloadHandler = new DownloadHandler("/download", ServerSettings.
                fileLocation);
	}

	@Override
	public void handle(HttpExchange exchange) {
        Debug.log("\n-----------------\nNEW EXCHANGE: "
                + exchange.getRequestMethod() + " "
                + exchange.getRequestURI().toString());

        /*Extract the request method and the context. Together they form a
        * key that is used to retrieve the appropriate command from a
        * hash map of all existing commands.*/
        String key = exchange.getRequestMethod() + " "
                + exchange.getHttpContext().getPath();
        Class<? extends Command> commandClass = CommandClasses.get(key);

        String uuid = Authenticate.performAuthentication(exchange);

        if(uuid == null && !commandClass.equals(PostLoginCommand.class)){
            Debug.log("User could not be authenticated");
            respond(new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
                    "User could not be authenticated"), exchange);
            return;
        } else if (commandClass == null && !key.equals("GET /download") &&
                !key.equals("GET /upload") && !key.equals("POST /upload")){
            Debug.log("Unrecognized command: " + exchange.getRequestMethod()
                    + " " + exchange.getRequestURI());
            respond(new ErrorResponse(HttpStatusCode.BAD_REQUEST,
                    "Could not create a command from request. Bad format on " +
                            "request."), exchange);
            return;
        }

        try {
            switch (key) {
                case ("GET /download"):
                    downloadHandler.handleGET(exchange);
                    return;
                case ("GET /upload"):
                    uploadHandler.handleGET(exchange);
                    return;
                case ("POST /upload"):
                    uploadHandler.handlePOST(exchange);
                    return;
            }
        } catch (Exception e) {
            Debug.log("Could not handle upload/download");
            return;
        }

		String json = readBody(exchange);
        if (json.isEmpty())
            json = "{}";
        Debug.log("Request body: \n" + json);

        Command command = gson.fromJson(json, commandClass);

        /*Retrieve the URI part of the request header.*/
        HashMap<String, String> query = new HashMap<>();
        String uri = Util.parseURI(exchange.getRequestURI(), query);

		/*Read the json body and create the command.*/
		String json = readBody(exchange);
		logRequestBody(json);
        Command command;
        try {
            command = fetchCommand(commandClass, json);
        } catch (InstantiationException | IllegalAccessException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatusCode.
                    INTERNAL_SERVER_ERROR, "Could not create command from " +
                    "request");
            respond(errorResponse, exchange);
            return;
        }

        /*Does the length of the URI match the needed length?*/
        if (command.getExpectedNumberOfURIFields() != calculateURILength(uri)) {
            Debug.log("Bad format on command: " + exchange.getRequestMethod()
                    + " " + exchange.getRequestURI());
            Debug.log("URI fields mismatch. Expected: "
                    + command.getExpectedNumberOfURIFields()
                    + ", Received: " + calculateURILength(uri));
            respond(new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not create a "
                    + "command from request. Bad format on request."), exchange);
            return;
        }

		/*Get the user's role from the databaseAccessor.*/
        UserType userType = UserType.UNKNOWN;
        try (DatabaseAccessor db = Command.initDB()) {
            userType = db.getRole(Authenticate.getUsernameByID(uuid));
        } catch (SQLException e) {
            Debug.log(e.toString());
            ErrorResponse errorResponse = new ErrorResponse(HttpStatusCode.
                    INTERNAL_SERVER_ERROR, "Could not retrieve the user " +
                    "information.");
            respond(errorResponse, exchange);
        } catch (IOException e) {
            Debug.log(e.toString());
            ErrorResponse errorResponse = new ErrorResponse(HttpStatusCode.
                    INTERNAL_SERVER_ERROR, "Could not retrieve the user " +
                    "information.");
            respond(errorResponse, exchange);
        }

        command.setFields(uri, query, uuid, userType);

		try {
			command.validate();
		} catch (ValidateException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log("ValidateException", e.getMessage());
			respond(new ErrorResponse(e.getCode(), e.getMessage()), exchange);
			return;
		}
        if (commandClass.equals(PutProcessCommand.class)) {
            Doorman.getProcessPool().addProcess((PutProcessCommand) command);
            respond(new ProcessResponse(HttpStatusCode.OK), exchange);
        } else {
            respond(command.execute(), exchange);
        }
	}

    /*Used to send a response back to the client*/
    private void respond(Response response, HttpExchange exchange) {
        try {
            if (response.getBody() == null || response.getBody().equals("")) {
                exchange.sendResponseHeaders(response.getCode(), 0);
            } else {
                String body = response.getBody();
                Debug.log("Response body: \n" + body);

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

    /*Performs authorization, returns null if the user could not be authorized,
    * else it returns the uuid.*/
	private String performAuthorization(HttpExchange exchange) {
		String uuid = null;

		// Get the value of the 'Authorization' header.
		List<String> authHeader = exchange.getRequestHeaders().
				get("Authorization");
		if (authHeader != null)
			uuid = authHeader.get(0);

        //If the uuid could not be retrieved from the header, do this.
        if (uuid == null) {
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
                        Debug.log("Authorization header and token parameter " +
                                "values differ!");
                        return null;
                    }
                }
            }
        }

		// Actual authentication.
		Debug.log("Trying to authenticate token " + uuid + "...");
		if (uuid != null && Authenticate.idExists(uuid)) {
			Authenticate.updateLatestRequest(uuid);
            return uuid;
		} else {
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

    /*Attempts to create a command given a class object and a json string.*/
    private Command fetchCommand(Class<? extends Command> commandClass,
                                 String json) throws InstantiationException,
            IllegalAccessException {
        Command command = gson.fromJson(json, commandClass);
        if (command == null) {
            return commandClass.newInstance();
        } else {
            return command;
        }
    }

	/*Creates a bad request ErrorResponse.*/
	private ErrorResponse createBadRequestResponse() {
		return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not create a " +
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
	private void logUser(String username) {
        Debug.log("User " + username + " authenticated successfully.");
	}

    /*Sends a authentication failure response. Logs the event.*/
    private void respondWithAuthenticationFailure(HttpExchange exchange) {
        Debug.log("User could not be authenticated!");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatusCode.
                INTERNAL_SERVER_ERROR, "Could not create command from " +
                "request");
        respond(errorResponse, exchange);
    }
}
