//package server;
///**
//* A Doorman-object is used to receive requests and send back responses to the client.
//* The doorman is listening for the following contexts:
//* /login
//* /experiment
//* /annotation
//* /file
//* /search
//* /user
//* /process
//* /sysadm
//* /genomeRelease
//* /token
//*
//* Whenever a request is received the Doorman checks what context is has and creates a
//* new Executor (on  a new thread) and afterwards continues listening for new requests.
//*
//*/
//
//
//import authentication.Authenticate;
//import com.sun.net.httpserver.*;
//import command.RequestHandler;
//import command.CommandType;
//import response.MinimalResponse;
//import response.Response;
//import response.StatusCode;
//
//import javax.net.ssl.*;
//import java.io.*;
//import java.net.InetSocketAddress;
//import java.net.URLDecoder;
//import java.security.*;
//import java.security.cert.CertificateException;
//import java.util.List;
//import java.util.Scanner;
//import java.util.concurrent.Executor;
//
//public class SecureDoorman {
//
//	private HttpsServer httpsServer;
//	private RequestHandler requestHandler;
//
//	/**
//	 * Constructor. Creates a HTTPServer (but doesn't start it) which listens on the given port.
//	 * @param requestHandler a commandHandler which will create and handle all commands.
//	 * @param port the listening port.
//	 * @throws java.io.IOException
//	 */
//	public SecureDoorman(RequestHandler requestHandler, int port) throws IOException {
//System.err.println("Https server starting...");
//		this.requestHandler = requestHandler;
//
//		httpsServer = HttpsServer.create(new InetSocketAddress(port),0);
//		httpsServer.setHttpsConfigurator(getHttpsConfiguration("baguette"));
//
//		httpsServer.createContext("/login", createHandler());
//		httpsServer.createContext("/experiment", createHandler());
//		httpsServer.createContext("/annotation", createHandler());
//		httpsServer.createContext("/file", createHandler());
//		httpsServer.createContext("/search", createHandler());
//		httpsServer.createContext("/user", createHandler());
//		httpsServer.createContext("/process", createHandler());
//		httpsServer.createContext("/sysadm", createHandler());
//		httpsServer.createContext("/genomeRelease", createHandler());
//		httpsServer.createContext("/token", createHandler());
//System.err.println("Https server started");
//		httpsServer.setExecutor(new Executor() {
//			@Override
//			public void execute(Runnable command) {
//
//				try {
//					new Thread(command).start();
//				} catch (Exception e) {
//					System.err.println("ERROR when creating new Executor." + e.getMessage());
//					ErrorLogger.log("SERVER", "ERROR when creating new Executor." + e.getMessage());
//				}
//			}
//		});
//	}
//
//
//	private HttpsConfigurator getHttpsConfiguration(String password){
//
//		SSLContext sslContext = null;
//		char[] charPassword = password.toCharArray();
//		String filename = "genoStore";
//		try {
//			//Initialize context
//			sslContext = SSLContext.getInstance("TLSv1.1");
//
//			//Initialize the key store
//			KeyStore keyStore = KeyStore.getInstance("JKS");
//			String keyStoreName = filename;
//			keyStore.load(new FileInputStream(keyStoreName),charPassword);
//
//			//Setup for the key manager factory
//			KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
//			kmf.init(keyStore, charPassword);
//
//			//Setup for the trust manager factory
//			TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
//			tmf.init(keyStore);
//
//			//Initialize the sslContext
//			sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),null);
//
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//			System.err.println("The provider for the KeyStore is not available.");
//			System.exit(1);
//		} catch (CertificateException e) {
//			e.printStackTrace();
//			System.err.println("Could not load the key from the KeyStore.");
//			System.exit(1);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			System.err.println("Could not find the KeyStore algorithm.");
//			System.exit(1);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.err.println("Could not find the file: " + filename + ".");
//			System.exit(1);
//		} catch (UnrecoverableKeyException e) {
//			e.printStackTrace();
//			System.err.println("The key could not be retrieved.\n Please check if the password is correct.");
//			System.exit(1);
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//			System.err.println("Could not initialize the SSL context for the server.");
//			System.exit(1);
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.err.println("Could not open the file: " + filename + ".");
//			System.exit(1);
//		}
//
//		//Creates the configurator object
//		HttpsConfigurator httpsConfig = new HttpsConfigurator(sslContext){
//
//			//Overrides the method in order to change the default configurations
//			@Override
//			public void configure (HttpsParameters params) {
//
//				try {
//					//Initialize the ssl context
//					SSLContext sslContext = SSLContext.getDefault();
//					SSLEngine sslEngine = sslContext.createSSLEngine();
//
//					//Set the Https parameters
//					SSLParameters sslParameters = new SSLParameters();
//					sslParameters.setNeedClientAuth(false); 				//Change to true for client authentication
//					sslParameters.setCipherSuites(sslEngine.getEnabledCipherSuites());
//					sslParameters.setProtocols(sslEngine.getEnabledProtocols());
//					params.setSSLParameters(sslParameters);
//
//				} catch (NoSuchAlgorithmException e) {
//					e.printStackTrace();
//					System.err.println("Could not find the KeyStore algorithm.");
//					System.exit(1);
//				}
//			}
//		};
//
//		return httpsConfig;
//	}
//
//
//	/**
//	 * Start the HTTPServer
//	 */
//	public void start() {
//		httpsServer.start();
//	}
//
//	/**
//	 * Determines the specific command which is to be created.
//	 * @return A HttpHandler for the commands
//	 */
//	private HttpHandler createHandler() {
//
//		return new HttpHandler() {
//
//			@Override
//			public void handle(HttpExchange exchange) throws IOException {
//
//				Debug.log("\n-----------------\nNEW EXCHANGE: " + exchange.getHttpContext().getPath());
//				switch(exchange.getRequestMethod()) {
//					case "GET":
//						switch(exchange.getHttpContext().getPath()) {
//							case "/experiment":
//								handleRequest(exchange, CommandType.GET_EXPERIMENT_COMMAND);
//								break;
//							case "/file":
//								handleRequest(exchange, CommandType.GET_FILE_FROM_EXPERIMENT_COMMAND);
//								break;
//							case "/search":
//								handleRequest(exchange, CommandType.SEARCH_FOR_EXPERIMENTS_COMMAND);
//								break;
//							case "/annotation":
//								handleRequest(exchange, CommandType.GET_ANNOTATION_INFORMATION_COMMAND);
//								break;
//							case "/genomeRelease":
//								if(exchange.getRequestURI().toString().startsWith("/genomeRelease/")){
//									handleRequest(exchange, CommandType.GET_GENOME_RELEASE_SPECIES_COMMAND);
//								}else{
//									handleRequest(exchange, CommandType.GET_ALL_GENOME_RELEASE_COMMAND);
//								}
//								break;
//							case "/sysadm":
//								handleRequest(exchange, CommandType.GET_ANNOTATION_PRIVILEGES_COMMAND);
//								break;
//							case "/process":
//								handleRequest(exchange, CommandType.GET_PROCESS_STATUS_COMMAND);
//								break;
//							case "/token":
//								handleRequest(exchange, CommandType.IS_TOKEN_VALID_COMMAND);
//						}
//						break;
//
//
//					case "PUT":
//						switch(exchange.getHttpContext().getPath()) {
//							case "/experiment":
//								handleRequest(exchange, CommandType.UPDATE_EXPERIMENT_COMMAND);
//								break;
//							case "/file":
//								handleRequest(exchange, CommandType.UPDATE_FILE_IN_EXPERIMENT_COMMAND);
//								break;
//							case "/process":
//
//								String processPath = exchange.getRequestURI().toString();
//
//								if (processPath.startsWith("/process/rawtoprofile")) {
//									handleRequest(exchange, CommandType.PROCESS_COMMAND);
//								}
//								break;
//							case "/annotation":
//								if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
//									handleRequest(exchange, CommandType.RENAME_ANNOTATION_FIELD_COMMAND);
//								} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
//									handleRequest(exchange, CommandType.RENAME_ANNOTATION_VALUE_COMMAND);
//								}
//								break;
//							case "/sysadm":
//								if (exchange.getRequestURI().toString().startsWith("/sysadm/annpriv")) {
//									handleRequest(exchange, CommandType.UPDATE_ANNOTATION_PRIVILEGES_COMMAND);
//								}
//								break;
//						}
//						break;
//
//
//					case "POST":
//						switch(exchange.getHttpContext().getPath()) {
//							case "/login":
//								handleRequest(exchange, CommandType.LOGIN_COMMAND);
//								break;
//							case "/experiment":
//								handleRequest(exchange, CommandType.ADD_EXPERIMENT_COMMAND);
//								break;
//							case "/file":
//								handleRequest(exchange, CommandType.ADD_FILE_TO_EXPERIMENT_COMMAND);
//								break;
//							case "/user":
//								handleRequest(exchange, CommandType.CREATE_USER_COMMAND);
//								break;
//							case "/annotation":
//								if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
//									handleRequest(exchange, CommandType.ADD_ANNOTATION_FIELD_COMMAND);
//								} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
//									handleRequest(exchange, CommandType.ADD_ANNOTATION_VALUE_COMMAND);
//								}
//								break;
//							case "/genomeRelease":
//								handleRequest(exchange, CommandType.ADD_GENOME_RELEASE_COMMAND);
//								break;
//						}
//						break;
//
//
//					case "DELETE":
//						switch(exchange.getHttpContext().getPath()) {
//							case "/login":
//								handleRequest(exchange, CommandType.LOGOUT_COMMAND);
//								break;
//							case "/experiment":
//								handleRequest(exchange, CommandType.DELETE_EXPERIMENT_COMMAND);
//								break;
//							case "/file":
//								handleRequest(exchange, CommandType.DELETE_FILE_FROM_EXPERIMENT_COMMAND);
//								break;
//							case "/user":
//								handleRequest(exchange, CommandType.DELETE_USER_COMMAND);
//								break;
//							case "/annotation":
//								if (exchange.getRequestURI().toString().startsWith("/annotation/field")) {
//									handleRequest(exchange, CommandType.REMOVE_ANNOTATION_FIELD_COMMAND);
//								} else if (exchange.getRequestURI().toString().startsWith("/annotation/value")) {
//									handleRequest(exchange, CommandType.DELETE_ANNOTATION_VALUE_COMMAND);
//								}
//								break;
//							case "/genomeRelease":
//								handleRequest(exchange, CommandType.DELETE_GENOME_RELEASE_COMMAND);
//								break;
//						}
//						break;
//				}
//			}
//		};
//	}
//
//	/**
//	 * Handles the request information from the client and creates a Command with CommandHandler.
//	 * @param exchange the HTTPExchange
//	 * @param type which specific type of command it is.
//	 */
//	private void handleRequest(HttpExchange exchange, CommandType type) {
//		InputStream bodyStream = exchange.getRequestBody();
//		Scanner scanner = new Scanner(bodyStream);
//		String body = "";
//		String uuid = null;
//		Debug.log("Exchange: " + type);
//
//		/** authorization */
//		if(type != CommandType.LOGIN_COMMAND) {
//			List<String> auth = exchange.getRequestHeaders().get("Authorization");
//			if (auth != null && Authenticate.idExists(auth.get(0))) {
//				uuid = auth.get(0);
//				Authenticate.updateLatestRequest(uuid);
//			} else {
//				Debug.log("Unauthorized request!");
//				Response errorResponse = new MinimalResponse(StatusCode.UNAUTHORIZED);
//				try {
//					respond(exchange, errorResponse);
//				} catch (IOException e1) {
//					Debug.log("Could not send response to client. " + e1.getMessage());
//				}
//				scanner.close();
//				return;
//			}
//		}
//
//		while(scanner.hasNext()) {
//			body = body.concat(" " + scanner.next());
//		}
//		scanner.close();
//
//		String username = null;
//		//username = Authenticate.getUsername(uuid);
//		Debug.log("Username: " + username + "\n");
//		Debug.log("Body from client: " + body);
//
//
//		Response response = null;
//		try {
//			String header = URLDecoder.decode(exchange.getRequestURI().toString(), "UTF-8");
//			response = requestHandler.processNewCommand(body, header, uuid, type);
//		} catch(Exception e ) {
//			Debug.log("Could not create/process new command " + e.getMessage());
//			ErrorLogger.log("SYSTEM", e);
//			e.printStackTrace();
//		}
//
//		//TODO Should there be some error checking?
//
//		try {
//			respond(exchange, response);
//		} catch (IOException e) {
//			Debug.log("IOError when sending response back to client. " + e.getMessage());
//			ErrorLogger.log("SYSTEM", e);
//		}
//	}
//
//	/**
//	 * Sends a response back to the client.
//	 * @param exchange the HTTPExchange-object.
//	 * @param response the response object containing information about the response.
//	 * @throws java.io.IOException
//	 */
//	private void respond(HttpExchange exchange, Response response) throws IOException {
//		if(response.getBody() == null || response.getBody().equals("")) {
//			exchange.sendResponseHeaders(response.getCode(), 0);
//		} else {
//			String body = response.getBody();
//			Debug.log("Response: " + body);
//			exchange.sendResponseHeaders(response.getCode(), body.getBytes().length);
//
//			OutputStream os = exchange.getResponseBody();
//			os.write(body.getBytes());
//			os.flush();
//			os.close();
//		}
//
//		Debug.log("END OF EXCHANGE\n------------------");
//	}
//}
