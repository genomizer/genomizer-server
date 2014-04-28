package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import server.test.CommandHandler;



public class Doorman {

	private HttpServer httpServer;
	private CommandHandler commandHandler;

	public static final int LOGIN_COMMAND = 1;
	public static final int EXPERIMENT_COMMAND = 2;
	public static final int FILE_COMMAND = 3;
	public static final int SEARCH_COMMAND = 4;
	public static final int USER_COMMAND = 5;
	public static final int PROCESS_COMMAND = 6;
	public static final int SYSADM_COMMAND = 7;

	public Doorman(CommandHandler commandHandler, int port) throws IOException {

		this.commandHandler = commandHandler;

		httpServer = HttpServer.create(new InetSocketAddress(port),0);
		//httpServer.createContext("/", rootHandler());
		httpServer.createContext("/login", rootHandler(LOGIN_COMMAND));
		httpServer.createContext("/experiment", rootHandler(EXPERIMENT_COMMAND));
		httpServer.createContext("/file", rootHandler(FILE_COMMAND));
		httpServer.createContext("/search", rootHandler(SEARCH_COMMAND));
		httpServer.createContext("/user", rootHandler(USER_COMMAND));
		httpServer.createContext("/process", rootHandler(PROCESS_COMMAND));
		httpServer.createContext("/sysadm", rootHandler(SYSADM_COMMAND));

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

	HttpHandler rootHandler(int code) {
		return new HttpHandler() {
			@Override
			public void handle(HttpExchange arg0) throws IOException {
				commandHandler.doStuff(code);
			}
		};
	}
}
