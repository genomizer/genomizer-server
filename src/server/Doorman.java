package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.Executor;

import sun.misc.IOUtils;



import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import command.CommandHandler;
import command.CommandType;
import command.Response;

public class Doorman {

	private HttpServer httpServer;
	private CommandHandler commandHandler;

	public Doorman(CommandHandler commandHandler, int port) throws IOException {

		this.commandHandler = commandHandler;

		httpServer = HttpServer.create(new InetSocketAddress(port),0);
		httpServer.createContext("/", createHandler()); // SHOULD BE CHANGED!!!
		httpServer.createContext("/login", createHandler());
		httpServer.createContext("/experiment", createHandler());
		httpServer.createContext("/file", createHandler());
		httpServer.createContext("/search", createHandler());
		httpServer.createContext("/user", createHandler());
		httpServer.createContext("/process", createHandler());
		httpServer.createContext("/sysadm", createHandler());

		httpServer.setExecutor(new Executor() {
			@Override
			public void execute(Runnable command) {
				new Thread(command).start();
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

				System.out.println("HEJ " + exchange.getHttpContext().getPath());

				if(exchange.getRequestMethod().equals("GET")) {
					System.out.println("GET");

				} else if(exchange.getRequestMethod().equals("POST")) {
					System.out.println("POST");

					if(exchange.getHttpContext().getPath().equals("/login")) {
						System.out.println("login");
						exchange(exchange, CommandType.LOGIN_COMMAND);
					}

				} else if(exchange.getRequestMethod().equals("DELETE")) {
					System.out.println("DELETE");

					if(exchange.getHttpContext().getPath().equals("/login")) {
						exchange(exchange, CommandType.LOGOUT_COMMAND);
					}
				}
			}
		};
	}

	private void exchange(HttpExchange exchange, CommandType type) throws IOException {
		InputStream bodyStream = exchange.getRequestBody();
		Scanner scanner = new Scanner(bodyStream);
		String body = "";

		String uuid = null;

		if(!(type == CommandType.LOGIN_COMMAND)) {
			try {
				uuid =  exchange.getRequestHeaders().get("Authorization").get(0);
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}

		while(scanner.hasNext()) {
			body = body.concat(scanner.next());
		}

		Response response = commandHandler.doStuff(body, exchange.getRequestURI().toString(), uuid, type);

		respond(exchange, response);


	}

	private void respond(HttpExchange exchange, Response response) throws IOException {
		System.out.println("1");
		String body = response.getBody();
		System.out.println("2");
		System.out.println("RESPOND BODY: " + body);
		exchange.sendResponseHeaders(response.getCode(), body.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
        os.flush();
        os.close();
	}



	public static void main(String args[]) {
		try {
			new Doorman(new CommandHandler(), 8080).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
}