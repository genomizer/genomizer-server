package server;

import authentication.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import command.*;
import command.connection.PostLoginCommand;
import command.process.PutProcessCommand;
import database.subClasses.UserMethods.UserType;
import response.ErrorResponse;
import response.HttpStatusCode;
import response.ProcessResponse;
import response.Response;
import transfer.DownloadHandler;
import transfer.UploadHandler;
import transfer.Util;

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
        String requestMethod = exchange.getRequestMethod();
		String context = exchange.getHttpContext().getPath();
        String key = requestMethod + " " + context;
        Class<? extends Command> commandClass = CommandClasses.get(key);

        /*Authenticate the user and send the appropriate response if needed.*/
        String uuid = Authenticate.AuthenticateAuthorization(exchange);

        if(uuid == null && !commandClass.equals(PostLoginCommand.class)){
            Debug.log("User could not be authenticated");
            respond(new ErrorResponse(HttpStatusCode.UNAUTHORIZED,
                    "User could not be authenticated"), exchange);
        } else if (commandClass == null && !key.equals("GET /download") &&
                !key.equals("GET /upload") && !key.equals("POST /upload")){
            Debug.log("Unrecognized command: " + exchange.getRequestMethod()
                    + " " + exchange.getRequestURI());
            respond(createBadRequestResponse(), exchange);
        }

        Debug.log("User " + Authenticate.getUsernameByID(uuid) + " authenticated successfully.");

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
        if (json.equals("") || json.isEmpty()) {
            json = "{}";
        }
        Debug.log("Request body: \n" + json);

        Command command = gson.fromJson(json, commandClass);

        String uri = removeTimeStamp(exchange.getRequestURI().toString());

        if (command.getExpectedNumberOfURIFields() != (uri.split("/").length-1)) {
            Debug.log("Bad format on command: " + exchange.getRequestMethod()
                    + " " + exchange.getRequestURI());
            respond(createBadRequestResponse(), exchange);
            return;
        }

		command.setFields(uri, uuid, UserType.ADMIN);

		try {
			command.validate();
		} catch (ValidateException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log("ValidateException", e.getMessage());
			respond(new ErrorResponse(e.getCode(), e.getMessage()), exchange);
		}
        if (commandClass.equals(PutProcessCommand.class)) {
            Doorman.getWorkPool().addWork((PutProcessCommand) command);
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
        return new ErrorResponse(HttpStatusCode.BAD_REQUEST, "Could not create a " +
                "command from request. Bad format on request.");
    }

    /* Finds the timestamp and removes it.*/
    private String removeTimeStamp(String uri){
        if (!uri.contains("_="))
            return uri;

        int pos = uri.lastIndexOf("_=");
        int length = uri.length();
        int end = pos +2;

        if (length <= end && '0' > uri.charAt(end) || '9' < uri.charAt(end)){
            return uri;
        }

        if (pos > 0 && uri.charAt(pos-1) == '&') {
            pos -= 1;
        }

        while(length > end && '0' <= uri.charAt(end) && '9' >= uri.charAt(end)){
            end++;
        }

        return uri.substring(0,pos) + uri.substring(end);
    }
}
