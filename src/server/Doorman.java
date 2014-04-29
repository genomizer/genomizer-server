package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

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
		//httpServer.createContext("/", rootHandler());
		httpServer.createContext("/login", createHandler(CommandType.LOGIN_COMMAND));
		httpServer.createContext("/experiment", createHandler(CommandType.EXPERIMENT_COMMAND));
		httpServer.createContext("/file", createHandler(CommandType.FILE_COMMAND));
		httpServer.createContext("/search", createHandler(CommandType.SEARCH_COMMAND));
		httpServer.createContext("/user", createHandler(CommandType.USER_COMMAND));
		httpServer.createContext("/process", createHandler(CommandType.PROCESS_COMMAND));
		httpServer.createContext("/sysadm", createHandler(CommandType.SYSADM_COMMAND));

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

	HttpHandler createHandler(CommandType type) {
		return new HttpHandler() {
			@Override
			public void handle(HttpExchange arg0) throws IOException {
//				commandHandler.doStuff(arg0.getRequestBody(), type);
			}
		};
	}
}
