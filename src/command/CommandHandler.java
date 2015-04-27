package command;

import authentication.Authenticate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import response.ErrorResponse;
import response.MinimalResponse;
import response.Response;
import response.StatusCode;
import server.Debug;
import server.ErrorLogger;

import java.util.Scanner;

public class CommandHandler implements HttpHandler {
	private Gson gson;

	public CommandHandler() {
		GsonBuilder builder = new GsonBuilder();
		builder.excludeFieldsWithoutExposeAnnotation();
		gson = builder.create();
	}

	@Override
	public void handle(HttpExchange exchange) {
		String context = exchange.getHttpContext().toString();
		String requestMethod = exchange.getRequestMethod().toString();
		Class<? extends Command> commandClass = CommandClasses.get(context +
				" " + requestMethod);

		/*Does the request match a command?*/
		if (commandClass == null) {
			Debug.log("Unrecognized command.");
			respond(createBadRequestResponse(), exchange);
			return;
		}

		//Todo MAY have to change to something with UTF-8
		String uri = exchange.getRequestURI().toString();

		/*Does the length of the URI check out?*/
		if (URILength.get(commandClass) != calculateURILength(uri)) {
			Debug.log("Bad format on command");
			respond(createBadRequestResponse(), exchange);
			return;
		}

		/*Is the user authorized?*/
		String uuid = getUUID(exchange);
		if (uuid == null) {
			Debug.log("Unauthorized request!");
			Response response = new MinimalResponse(StatusCode.UNAUTHORIZED);
			respond(response, exchange);
			return;
		}

		/*Read the json body and create the commmand.*/
		String json = readBody(exchange);
		Command command = gson.fromJson(json, commandClass);
		command.setFields(uri, uuid);

		/*Attempt to validate the command.*/
		try {
			command.validate();
		} catch (ValidateException e) {
			Debug.log(e.getMessage());
			ErrorLogger.log("ValidateException", e.getMessage());
			respond(new ErrorResponse(e.getCode(), e.getMessage()), exchange);
			return;
		}

		/*Execute the command.*/
		respond(command.execute(), exchange);
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

	/*Used to calculate the "length" of an URI.*/
	private int calculateURILength(String requestURI) {
		return requestURI.split("/").length-1;
	}

	private void respond(Response response, HttpExchange httpExchange) {

	}

	/*Returns the uuid of the connected user. Returns null if the user
	* is not authorized.*/
	private String getUUID(HttpExchange exchange) {
		String uuid = exchange.getRequestHeaders().get("Authorization").get(0);
		if (uuid != null && Authenticate.idExists(id)) {
			Authenticate.updateLatestRequest(id);
			return uuid;
		} else {
			return null;
		}
	}

	/*Creates a bad request ErrorResponse.*/
	private ErrorResponse createBadRequestResponse() {
		return new ErrorResponse(StatusCode.BAD_REQUEST, "Could not create a " +
				"command from request. Bad format on request.");
	}
}
