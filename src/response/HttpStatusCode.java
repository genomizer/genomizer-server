package response;

/**
 * Contains all HTTP response codes that are used by the server.
 * Currently, 403 and 429 are not used.
 *
 * @author ens10olm
 * @version 1.0
 */
public class HttpStatusCode {

	public static final int OK = 200;
	public static final int CREATED = 201;
	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int INTERNAL_SERVER_ERROR = 500;
	public static final int NOT_IMPLEMENTED = 501;

	public static String getMessage(int code) {
		switch (code) {
			case OK:
				return "OK";
			case CREATED:
				return "Created";
			case BAD_REQUEST:
				return "Bad Request";
			case UNAUTHORIZED:
				return "Unauthorized";
			case FORBIDDEN:
				return "Forbidden";
			case NOT_FOUND:
				return "Not Found";
			case INTERNAL_SERVER_ERROR:
				return "Internal Server Error";
			case NOT_IMPLEMENTED:
				return "Not Implemented";
			default:
				return "Unknown";
		}
	}
}
